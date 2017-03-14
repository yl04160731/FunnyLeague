package league.funny.com.funnyleague.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.adapter.QiuBaiUserTextImageRecyclerAdapter;
import league.funny.com.funnyleague.bean.QiuBaiItemBean;
import league.funny.com.funnyleague.util.HttpUrlUtil;
import league.funny.com.funnyleague.util.Util;
import league.funny.com.funnyleague.view.RecycleViewDivider;

public class userQiuBaiTextImageActivity extends AppCompatActivity {

    private View view = null;
    private int page = 1;
    private boolean isLoading;
    private String userUrl;
    private QiuBaiItemBean qiuBaiItemBean;
    private ArrayList<QiuBaiItemBean> qiuBaiItemBeanArrayList = new ArrayList<>();
    private QiuBaiUserTextImageRecyclerAdapter adapter;

    @BindView(R.id.swipeRefreshLayout_text)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView_text)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_image_user_qiubai);
        ButterKnife.bind(this);
        initView();
        initData();
        if(Build.VERSION.SDK_INT>=21){
            getSupportActionBar().setElevation(0);
        }
    }

    public void initView() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_content);//自定义ActionBar布局
        ImageButton imageButton = (ImageButton)actionBar.getCustomView().findViewById(R.id.contentBack);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new QiuBaiUserTextImageRecyclerAdapter(this, qiuBaiItemBeanArrayList);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Runnable networkTask = new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);

                        page = 1;
                        getData(true);
                    }
                };
                new Thread(networkTask).start();
            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(this,RecycleViewDivider.VERTICAL_LIST,R.drawable.divider));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {

                    boolean isRefreshing = swipeRefreshLayout.isRefreshing();
                    if (isRefreshing) {
//                        adapter.notifyItemRemoved(adapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        Runnable networkTask = new Runnable() {
                            @Override
                            public void run() {
                                page = page + 1;
                                getData(false);
                                isLoading = false;
                            }
                        };
                        new Thread(networkTask).start();
                    }
                }
            }
        });
    }

    public void initData() {
        Intent intent = this.getIntent();
        qiuBaiItemBean = (QiuBaiItemBean) intent.getSerializableExtra("qiuBaiItemBean");
        userUrl = qiuBaiItemBean.getUserUrl() + HttpUrlUtil.QIU_BAI_USER_ARTICLES;
        Runnable networkTask = new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getData(false);
            }
        };

        new Thread(networkTask).start();
    }

    Handler HtmlHandler = new Handler() {
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
            adapter.notifyItemRemoved(adapter.getItemCount());
        }
    };

    /**
     * 获取测试数据
     */
    private void getData(boolean clearFlg) {
        try {
            String URL = userUrl + HttpUrlUtil.QIU_BAI_USER_PAGE + page + HttpUrlUtil.SPRIT;
            Document doc = Jsoup.connect(URL)
                    .userAgent(HttpUrlUtil.USER_AGENT)
                    .timeout(15000).get();

            Elements elementsArticle = doc.select(".user-block").select(".user-article");
            if (elementsArticle != null && elementsArticle.size() <= 0) {
                return;
            }

            if(clearFlg) qiuBaiItemBeanArrayList.clear();

            for (int i = 0; i < elementsArticle.size(); i++) {
                QiuBaiItemBean qiuBaiItemTempBean = new QiuBaiItemBean();

                qiuBaiItemTempBean.setUserId(qiuBaiItemBean.getUserId());
                qiuBaiItemTempBean.setUserUrl(qiuBaiItemBean.getUserUrl());
                qiuBaiItemTempBean.setUserName(qiuBaiItemBean.getUserName());
                qiuBaiItemTempBean.setUserImage(qiuBaiItemBean.getUserImage());
                qiuBaiItemTempBean.setUserAge(qiuBaiItemBean.getUserAge());
                qiuBaiItemTempBean.setUserSex(qiuBaiItemBean.getUserSex());

                qiuBaiItemTempBean.setItemContentUrl(HttpUrlUtil.QIU_BAI_HOME + elementsArticle.get(i).select(".user-article-text").select("a").attr("href"));
                qiuBaiItemTempBean.setItemContent(Util.replaceHtmlSign(elementsArticle.get(i).select(".user-article-text").select("a").text()));
                qiuBaiItemTempBean.setItemImage(elementsArticle.get(i).select(".user-article-pic").select("img").attr("src"));
                String text = elementsArticle.get(i).select(".user-article-stat").text();
                String smileCount = "";
                String commentCount = "";
                if(text != null){
                    smileCount = text.substring(0,text.indexOf("好笑")).trim();
                    commentCount = text.substring(text.indexOf("⋅") + 1,text.indexOf("评论")).trim();
                }

                qiuBaiItemTempBean.setSmileCount(smileCount);
                qiuBaiItemTempBean.setCommentCount(commentCount);

                qiuBaiItemBeanArrayList.add(qiuBaiItemTempBean);
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
