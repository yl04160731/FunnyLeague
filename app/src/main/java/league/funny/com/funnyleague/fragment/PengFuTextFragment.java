package league.funny.com.funnyleague.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.adapter.PengFuTextRecyclerAdapter;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.util.HttpUrlUtil;
import league.funny.com.funnyleague.util.Util;
import league.funny.com.funnyleague.view.LoadMoreViewFooter;
import league.funny.com.funnyleague.view.RecycleViewDivider;

public class PengFuTextFragment extends BaseFragment {

    private View view = null;
    private int page = 1;
    private ArrayList<ItemBean> pengFuItemBeanArrayList = new ArrayList<>();
    private PengFuTextRecyclerAdapter adapter;

    private boolean onFreshFlg = false;

    private SwipeRefreshHelper mSwipeRefreshHelper;
    private RecyclerAdapterWithHF mAdapter;

    @BindView(R.id.swipeRefreshLayout_text)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView_text)
    RecyclerView recyclerView;

    public PengFuTextFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_text_pengfu, container, false);
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
        adapter = new PengFuTextRecyclerAdapter(getActivity(), pengFuItemBeanArrayList);
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
                mSwipeRefreshHelper.setLoadMoreEnable(true);
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
            String URL = HttpUrlUtil.PENG_FU_TEXT + page + HttpUrlUtil.HTML;
            Document doc = Jsoup.connect(URL)
                    .userAgent(HttpUrlUtil.USER_AGENT)
                    .timeout(15000).get();

            Elements elementsArticle = doc.select(".list-item");
            if (elementsArticle != null && elementsArticle.size() > 0) {
                if(onFreshFlg) pengFuItemBeanArrayList.clear();

                for (int i = 0; i < elementsArticle.size(); i++) {
                    ItemBean itemBean = new ItemBean();
                    Elements elementsAuthor = elementsArticle.get(i).select("dl");
                    Elements elementDt = elementsAuthor.select("dt");
                    Elements elementDd = elementsAuthor.select("dd");

                    String userUrl = elementDt.select("a").attr("href");
                    itemBean.setUserId(elementDt.attr("id"));
                    itemBean.setUserUrl(userUrl);
                    itemBean.setUserName(Util.replaceHtmlSign(elementDt.select("a").select("img").attr("alt")));
                    itemBean.setUserImage(elementDt.select("img").attr("src"));

                    itemBean.setItemContentUrl(elementDd.select(".dp-b").select("a").attr("href"));
                    itemBean.setItemContent(Util.replaceHtmlSign(elementDd.select(".content-img").text()));
                    itemBean.setItemContentTitle(Util.replaceHtmlSign(elementDd.select(".dp-b").select("a").text()));

                    itemBean.setCommentCount(elementsArticle.get(i).select(".fl").select(".commentClick").select("em").text());
                    itemBean.setDing(elementsArticle.get(i).select(".fl").select(".ding").select("em").text());
                    itemBean.setCai(elementsArticle.get(i).select(".fl").select(".cai").select("em").text());
                    pengFuItemBeanArrayList.add(itemBean);
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
