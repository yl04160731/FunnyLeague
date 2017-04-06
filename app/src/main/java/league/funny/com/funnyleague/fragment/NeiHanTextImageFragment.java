package league.funny.com.funnyleague.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.loadmore.SwipeRefreshHelper;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.adapter.NeiHanTextImageRecyclerAdapter;
import league.funny.com.funnyleague.api.ApiManage;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.bean.neihan.NeiHanResponse;
import league.funny.com.funnyleague.bean.neihan.NeiHanSubData;
import league.funny.com.funnyleague.util.HttpUrlUtil;
import league.funny.com.funnyleague.util.Util;
import league.funny.com.funnyleague.view.LoadMoreViewFooter;
import league.funny.com.funnyleague.view.RecycleViewDivider;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class NeiHanTextImageFragment extends BaseFragment {

    private View view = null;
    private ArrayList<ItemBean> neiHanItemBeanArrayList = new ArrayList<>();
    private NeiHanTextImageRecyclerAdapter adapter;

    private Subscription subscription;

    private int type = 0;
    private boolean onFreshFlg = false;

    private SwipeRefreshHelper mSwipeRefreshHelper;
    private RecyclerAdapterWithHF mAdapter;

    @BindView(R.id.swipeRefreshLayout_text)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView_text)
    RecyclerView recyclerView;

    public NeiHanTextImageFragment() {
        // Required empty public constructor
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_text_image_neihan, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    public void initView() {

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_main);//自定义ActionBar布局

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), RecycleViewDivider.VERTICAL_LIST, R.drawable.divider));
    }

    public void initData() {
        adapter = new NeiHanTextImageRecyclerAdapter(getActivity(), neiHanItemBeanArrayList);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        recyclerView.setAdapter(mAdapter);
        mSwipeRefreshHelper = new SwipeRefreshHelper(swipeRefreshLayout);
        mSwipeRefreshHelper.setFooterView(new LoadMoreViewFooter());

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshHelper.autoRefresh();
            }
        });

        mSwipeRefreshHelper.setOnSwipeRefreshListener(new SwipeRefreshHelper.OnSwipeRefreshListener() {
            @Override
            public void onfresh() {
                Runnable networkTask = new Runnable() {
                    @Override
                    public void run() {
                        onFreshFlg = true;
                        getData();
                    }
                };

                new Thread(networkTask).start();
            }
        });

        mSwipeRefreshHelper.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                Runnable networkTask = new Runnable() {
                    @Override
                    public void run() {
                        onFreshFlg = false;
                        getData();
                    }
                };

                new Thread(networkTask).start();
            }
        });
    }

    Handler HtmlHandler = new Handler() {
        public void handleMessage(Message msg) {
            mAdapter.notifyDataSetChanged();
            if (onFreshFlg) {
                mSwipeRefreshHelper.refreshComplete();
                if (neiHanItemBeanArrayList == null || neiHanItemBeanArrayList.size() <= 0) {
                    mSwipeRefreshHelper.setLoadMoreEnable(false);
                } else {
                    mSwipeRefreshHelper.setLoadMoreEnable(true);
                }
            } else {
                mSwipeRefreshHelper.loadMoreComplete(true);
            }
        }
    };

    /**
     * 获取测试数据
     */
    private void getData() {

        Observable<NeiHanResponse> obNeiHanResponse = null;

        if (type == HttpUrlUtil.TYPE_TEXT) {
            obNeiHanResponse = ApiManage.getInstence().getNeiHanApiService()
                    .getText();
        } else {
            obNeiHanResponse = ApiManage.getInstence().getNeiHanApiService()
                    .getImage();
        }

        subscription = obNeiHanResponse
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NeiHanResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(NeiHanResponse neiHanResponse) {
                        try {
                            if (neiHanResponse != null) {
                                if (onFreshFlg) neiHanItemBeanArrayList.clear();
                                for (int i = 0; i < neiHanResponse.getData().getSubData().size(); i++) {
                                    ItemBean itemBean = new ItemBean();
                                    NeiHanSubData neiHanSubData = neiHanResponse.getData().getSubData().get(i);

                                    itemBean.setUserId(neiHanSubData.getGroups().getUser().getUserId());
                                    itemBean.setUserUrl(HttpUrlUtil.NEI_HAN_USER + neiHanSubData.getGroups().getUser().getUserId() + HttpUrlUtil.SPRIT);
                                    itemBean.setUserName(Util.replaceHtmlSign(neiHanSubData.getGroups().getUser().getUserName()));
                                    itemBean.setUserImage(neiHanSubData.getGroups().getUser().getUserImage());
                                    itemBean.setItemContentUrl(HttpUrlUtil.NEI_HAN_CONTENT + neiHanSubData.getGroups().getId());
                                    if(neiHanSubData.getGroups().getImage() != null){
                                        itemBean.setItemImage(neiHanSubData.getGroups().getImage().getNeiHanUrlList().get(0).getUrl());
                                    }else{
                                        itemBean.setItemImage("");
                                    }

                                    itemBean.setItemContent(Util.replaceHtmlSign(neiHanSubData.getGroups().getContent()));
                                    itemBean.setCommentCount(neiHanSubData.getGroups().getComment_count());
                                    itemBean.setDing(neiHanSubData.getGroups().getDigg_count());
                                    itemBean.setCai(neiHanSubData.getGroups().getBury_count());
                                    itemBean.setRepin(neiHanSubData.getGroups().getRepin_count());
                                    itemBean.setShare(neiHanSubData.getGroups().getShare_count());
                                    neiHanItemBeanArrayList.add(itemBean);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            Message message = new Message();
                            message.what = 1;
                            HtmlHandler.sendMessage(message);
                        }

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}
