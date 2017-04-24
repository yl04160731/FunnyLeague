package league.funny.com.funnyleague.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.loadmore.SwipeRefreshHelper;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.FunnyLeagueApplication;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.adapter.QiuBaiUserTextImageRecyclerAdapter;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.util.HttpUrlUtil;
import league.funny.com.funnyleague.util.Util;
import league.funny.com.funnyleague.view.LoadMoreViewFooter;
import league.funny.com.funnyleague.view.RecycleViewDivider;

public class QiuBaiUserTextImageActivity extends BaseActivity {

    private int page = 1;
    private String userUrl;
    private ItemBean itemBean;
    private ArrayList<ItemBean> itemBeanArrayList = new ArrayList<>();
    private QiuBaiUserTextImageRecyclerAdapter adapter;

    private boolean onFreshFlg = false;
    private boolean loadMore = true;

    private SwipeRefreshHelper mSwipeRefreshHelper;
    private RecyclerAdapterWithHF mAdapter;

    @BindView(R.id.swipeRefreshLayout_text)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView_text)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_image_user_qiubai);
        ButterKnife.bind(this);
        ((FunnyLeagueApplication)FunnyLeagueApplication.getContext()).addActivity(this);
        initView();
        initData();
        if (Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(0);
        }
    }

    public void initView() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_content);//自定义ActionBar布局
        ImageButton imageButton = (ImageButton) actionBar.getCustomView().findViewById(R.id.contentBack);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST, R.drawable.divider));
    }

    public void initData() {
        adapter = new QiuBaiUserTextImageRecyclerAdapter(this, itemBeanArrayList);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        recyclerView.setAdapter(mAdapter);
        mSwipeRefreshHelper = new SwipeRefreshHelper(swipeRefreshLayout);
        mSwipeRefreshHelper.setFooterView(new LoadMoreViewFooter());

        Intent intent = this.getIntent();
        itemBean = (ItemBean) intent.getSerializableExtra("itemBean");
        userUrl = itemBean.getUserUrl() + HttpUrlUtil.QIU_BAI_USER_ARTICLES;

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
            if (onFreshFlg) {
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshHelper.refreshComplete();
                mSwipeRefreshHelper.setLoadMoreEnable(true);
            } else {
                if(loadMore) {
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshHelper.loadMoreComplete(true);
                }else{
                    mSwipeRefreshHelper.loadMoreComplete(false);
                }
            }
        }
    };

    /**
     * 获取测试数据
     */
    private void getData() {
        try {
            String URL = userUrl + HttpUrlUtil.QIU_BAI_USER_PAGE + page + HttpUrlUtil.SPRIT;
            Document doc = Jsoup.connect(URL)
                    .userAgent(HttpUrlUtil.USER_AGENT)
                    .timeout(HttpUrlUtil.TIMEOUT).get();

            Elements elementsArticle = doc.select(".user-block").select(".user-article");
            if (elementsArticle != null && elementsArticle.size() > 0) {
                if (onFreshFlg) itemBeanArrayList.clear();

                page = page + 1;

                for (int i = 0; i < elementsArticle.size(); i++) {
                    ItemBean qiuBaiItemTempBean = new ItemBean();

                    qiuBaiItemTempBean.setUserId(itemBean.getUserId());
                    qiuBaiItemTempBean.setUserUrl(itemBean.getUserUrl());
                    qiuBaiItemTempBean.setUserName(itemBean.getUserName());
                    qiuBaiItemTempBean.setUserImage(itemBean.getUserImage());
                    qiuBaiItemTempBean.setUserAge(itemBean.getUserAge());
                    qiuBaiItemTempBean.setUserSex(itemBean.getUserSex());

                    qiuBaiItemTempBean.setItemContentUrl(HttpUrlUtil.QIU_BAI_HOME + elementsArticle.get(i).select(".user-article-text").select("a").attr("href"));
                    qiuBaiItemTempBean.setItemContent(Util.replaceHtmlSign(elementsArticle.get(i).select(".user-article-text").select("a").text()));

                    if(elementsArticle.get(i).select(".user-article-pic").select("img").attr("src") != null &&
                            !"".equals(elementsArticle.get(i).select(".user-article-pic").select("img").attr("src"))){
                        qiuBaiItemTempBean.setItemImage("http://" + elementsArticle.get(i).select(".user-article-pic").select("img").attr("src").substring(2));
                    }


                    String text = elementsArticle.get(i).select(".user-article-stat").text();
                    String smileCount = "";
                    String commentCount = "";
                    if (text != null) {
                        smileCount = text.substring(0, text.indexOf("好笑")).trim();
                        commentCount = text.substring(text.indexOf("⋅") + 1, text.indexOf("评论")).trim();
                    }

                    qiuBaiItemTempBean.setSmileCount(smileCount);
                    qiuBaiItemTempBean.setCommentCount(commentCount);

                    itemBeanArrayList.add(qiuBaiItemTempBean);
                    loadMore = true;
                }
            }else{
                loadMore = false;
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
