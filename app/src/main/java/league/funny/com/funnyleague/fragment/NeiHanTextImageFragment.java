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
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.view.LoadMoreViewFooter;
import league.funny.com.funnyleague.view.RecycleViewDivider;
import rx.Subscription;

/**
 * A simple {@link Fragment} subclass.
 */
public class NeiHanTextImageFragment extends BaseFragment {

    private View view = null;
    private int page = 1;
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

    public void setType(int type){
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
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(),RecycleViewDivider.VERTICAL_LIST,R.drawable.divider));
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
                        page = 1;
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
                        page = page + 1;
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
            if(onFreshFlg) {
                mSwipeRefreshHelper.refreshComplete();
                if(neiHanItemBeanArrayList == null || neiHanItemBeanArrayList.size() <= 0){
                    mSwipeRefreshHelper.setLoadMoreEnable(false);
                }else{
                    mSwipeRefreshHelper.setLoadMoreEnable(true);
                }
            }else{
                mSwipeRefreshHelper.loadMoreComplete(true);
            }
        }
    };

    /**
     * 获取测试数据
     */
    private void getData() {

//        subscription = ApiManage.getInstence().getNeiHanApiService().getImage()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<NeiHanResponse>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onNext(NeiHanResponse neiHanResponse) {
//                        if (neiHanResponse != null) {
//                        } else {
//                            return;
//                        }
//
//                    }
//                });


        try {
//            String URL = null;
//
//            if(type == HttpUrlUtil.TYPE_TEXT){
//                URL = HttpUrlUtil.NEI_HAN_TEXT + System.currentTimeMillis();
//            }else{
//                URL = HttpUrlUtil.NEI_HAN_IMAGE + System.currentTimeMillis();
//            }
//
//            Document doc = Jsoup.connect(URL)
//                    .userAgent(HttpUrlUtil.USER_AGENT)
//                    .timeout(15000).get();

//            Elements elementsArticle = doc.select(".list-item");
//            if (elementsArticle != null && elementsArticle.size() > 0) {
//                if(onFreshFlg) neiHanItemBeanArrayList.clear();
//
//                for (int i = 0; i < elementsArticle.size(); i++) {
//                    ItemBean itemBean = new ItemBean();
//                    Elements elementsAuthor = elementsArticle.get(i).select("dl");
//                    Elements elementDt = elementsAuthor.select("dt");
//                    Elements elementDd = elementsAuthor.select("dd");
//
//                    String userUrl = elementDt.select("a").attr("href");
//                    itemBean.setUserId(elementDt.attr("id"));
//                    itemBean.setUserUrl(userUrl);
//                    itemBean.setUserName(Util.replaceHtmlSign(elementDt.select("a").select("img").attr("alt")));
//                    itemBean.setUserImage(elementDt.select("img").attr("src"));
//                    itemBean.setItemContentUrl(elementDd.select(".dp-b").select("a").attr("href"));
//                    String gifSrc = elementsArticle.get(i).select(".content-img").select("img").attr("gifsrc");
//                    if(gifSrc != null && !"".equals(gifSrc)){
//                        itemBean.setItemImage(gifSrc);
//                    }else{
//                        itemBean.setItemImage(elementsArticle.get(i).select(".content-img").select("img").attr("src"));
//                    }
//                    itemBean.setItemContent(Util.replaceHtmlSign(elementDd.select(".content-img").text()));
//                    itemBean.setItemContentTitle(Util.replaceHtmlSign(elementDd.select(".dp-b").select("a").text()));
//
//                    itemBean.setCommentCount(elementsArticle.get(i).select(".fl").select(".commentClick").select("em").text());
//                    itemBean.setDing(elementsArticle.get(i).select(".fl").select(".ding").select("em").text());
//                    itemBean.setCai(elementsArticle.get(i).select(".fl").select(".cai").select("em").text());
//                    neiHanItemBeanArrayList.add(itemBean);
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Message message = new Message();
            message.what = 1;
            HtmlHandler.sendMessage(message);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}
