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
import league.funny.com.funnyleague.adapter.QiuBaiTextRecyclerAdapter;
import league.funny.com.funnyleague.bean.QiuBaiItemBean;
import league.funny.com.funnyleague.util.HttpUrlUtil;
import league.funny.com.funnyleague.util.Util;
import league.funny.com.funnyleague.view.LoadMoreViewFooter;
import league.funny.com.funnyleague.view.RecycleViewDivider;

public class QiuBaiTextFragment extends BaseFragment {

    private View view = null;
    private int page = 1;
    private ArrayList<QiuBaiItemBean> qiuBaiItemBeanArrayList = new ArrayList<>();
    private QiuBaiTextRecyclerAdapter adapter;

    private boolean onFreshFlg = false;

    private SwipeRefreshHelper mSwipeRefreshHelper;
    private RecyclerAdapterWithHF mAdapter;

    @BindView(R.id.swipeRefreshLayout_text)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView_text)
    RecyclerView recyclerView;

    public QiuBaiTextFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_text_qiubai, container, false);
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
        adapter = new QiuBaiTextRecyclerAdapter(getActivity(), qiuBaiItemBeanArrayList);
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
            String URL = HttpUrlUtil.QIU_BAI_TEXT_PAGE + page + HttpUrlUtil.SPRIT;
            Document doc = Jsoup.connect(URL)
                    .userAgent(HttpUrlUtil.USER_AGENT)
                    .timeout(15000).get();

            Elements elementsArticle = doc.select(".article").select(".block");
            if (elementsArticle != null && elementsArticle.size() <= 0) {
                return;
            }

            if(onFreshFlg) qiuBaiItemBeanArrayList.clear();

            for (int i = 0; i < elementsArticle.size(); i++) {
                QiuBaiItemBean qiuBaiItemBean = new QiuBaiItemBean();
                Elements elementsAuthor = elementsArticle.get(i).select(".author");

                String userUrl = elementsAuthor.select("a").attr("href");
                qiuBaiItemBean.setUserId(userUrl.replace("/","").replace("users",""));
                qiuBaiItemBean.setUserUrl(HttpUrlUtil.QIU_BAI_HOME + userUrl);
                qiuBaiItemBean.setUserName(Util.replaceHtmlSign(elementsAuthor.select("h2").html()));
                qiuBaiItemBean.setUserImage(elementsAuthor.select("img").attr("src"));
                qiuBaiItemBean.setUserAge(elementsAuthor.select(".articleGender").html());

                if(elementsAuthor.html().contains("manIcon")){
                    qiuBaiItemBean.setUserSex("man");
                }else if(elementsAuthor.html().contains("womenIcon")){
                    qiuBaiItemBean.setUserSex("women");
                }

                qiuBaiItemBean.setItemContentUrl(HttpUrlUtil.QIU_BAI_HOME + elementsArticle.get(i).select(".contentHerf").attr("href"));
                qiuBaiItemBean.setItemContent(Util.replaceHtmlSign(elementsArticle.get(i).select(".content").select("span").html()));
                qiuBaiItemBean.setSmileCount(elementsArticle.get(i).select(".stats-vote").select(".number").html());
                qiuBaiItemBean.setCommentCount(elementsArticle.get(i).select(".stats-comments").select(".number").html());
                qiuBaiItemBean.setCommentGoodName(Util.replaceHtmlSign(elementsArticle.get(i).select(".cmt-name").html()));
                String likeNum = elementsArticle.get(i).select(".likenum").text();
                qiuBaiItemBean.setCommentGoodContent(Util.replaceHtmlSign(elementsArticle.get(i).select(".main-text").text().replace(" " + likeNum,"")));
                qiuBaiItemBean.setCommentGoodCount(elementsArticle.get(i).select(".likenum").text());

                qiuBaiItemBeanArrayList.add(qiuBaiItemBean);
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
