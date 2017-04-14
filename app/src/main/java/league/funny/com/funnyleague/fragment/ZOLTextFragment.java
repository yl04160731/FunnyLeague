package league.funny.com.funnyleague.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.loadmore.SwipeRefreshHelper;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.adapter.ZOLTextRecyclerAdapter;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.util.HttpUrlUtil;
import league.funny.com.funnyleague.util.Util;
import league.funny.com.funnyleague.view.LoadMoreViewFooter;
import league.funny.com.funnyleague.view.RecycleViewDivider;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZOLTextFragment extends BaseFragment {

    private View view = null;
    private int page = 1;
    private ArrayList<ItemBean> ZOLItemBeanArrayList = new ArrayList<>();
    private ZOLTextRecyclerAdapter adapter;

    private int type = 0;

    private boolean onFreshFlg = false;

    private SwipeRefreshHelper mSwipeRefreshHelper;
    private RecyclerAdapterWithHF mAdapter;

    @BindView(R.id.swipeRefreshLayout_text)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView_text)
    RecyclerView recyclerView;

    public ZOLTextFragment() {
        // Required empty public constructor
    }

    public void setType(int type){
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_text_zol, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    public void initView() {

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(),RecycleViewDivider.VERTICAL_LIST,R.drawable.divider));
    }

    public void initData() {
        adapter = new ZOLTextRecyclerAdapter(getActivity(), ZOLItemBeanArrayList);
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
                if(ZOLItemBeanArrayList == null || ZOLItemBeanArrayList.size() <= 0){
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
        try {
            String URL = null;

            if(type == HttpUrlUtil.TYPE_ZOL_CHENG_REN){
                URL = HttpUrlUtil.ZOL_CHENG_REN_HOME + page + HttpUrlUtil.HTML;
            }else{
                URL = HttpUrlUtil.ZOL_NAN_NV_HOME + page + HttpUrlUtil.HTML;
            }

            Document doc = Jsoup.connect(URL)
                    .userAgent(HttpUrlUtil.USER_AGENT)
                    .timeout(HttpUrlUtil.TIMEOUT).get();

            Elements elementsArticle = doc.select(".article-summary");
            if (elementsArticle != null && elementsArticle.size() > 0) {
                if(onFreshFlg) ZOLItemBeanArrayList.clear();

                for (int i = 0; i < elementsArticle.size(); i++) {
                    ItemBean itemBean = new ItemBean();

                    itemBean.setItemContentUrl(HttpUrlUtil.ZOL_HOME + elementsArticle.get(i).select(".article-title").select("a").attr("href"));
                    itemBean.setItemContent(Util.replaceHtmlSign(elementsArticle.get(i).select(".summary-text").html()));
                    itemBean.setItemContentTitle(Util.replaceHtmlSign(elementsArticle.get(i).select(".article-title").text()));
                    ZOLItemBeanArrayList.add(itemBean);
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

}
