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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.adapter.TextRecyclerAdapter;
import league.funny.com.funnyleague.bean.QiuBaiItemBean;
import league.funny.com.funnyleague.util.HttpUrlUtil;
import league.funny.com.funnyleague.view.RecycleViewDivider;

/**
 * A simple {@link Fragment} subclass.
 */
public class TextSubFragment extends Fragment {

    private int pid;
    private View view = null;

    private int page = 1;

    boolean isLoading;
    private ArrayList<QiuBaiItemBean> qiuBaiItemBeanArrayList = new ArrayList<>();
    private TextRecyclerAdapter adapter;
    private Handler handler = new Handler();

    @BindView(R.id.swipeRefreshLayout_text)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView_text)
    RecyclerView recyclerView;

    public TextSubFragment() {
        // Required empty public constructor
    }

    public TextSubFragment(int pid) {
        this.pid = pid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_text_sub, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    public void initView() {
        adapter = new TextRecyclerAdapter(getActivity(), qiuBaiItemBeanArrayList);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        qiuBaiItemBeanArrayList.clear();
                        getData();
                    }
                }, 2000);
            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(),RecycleViewDivider.VERTICAL_LIST));

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
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData();
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }
        });

        //添加点击事件
        adapter.setOnItemClickListener(new TextRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    public void initData() {
        Runnable networkTask = new Runnable() {
            @Override
            public void run() {
                getData();
                Message message = new Message();
                message.what = 1;
                HtmlHandler.sendMessage(message);
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
    private void getData() {
        try {
            String URL = HttpUrlUtil.QIU_BAI_TEXT_PAGE + page + HttpUrlUtil.SPRIT;
            Document doc = Jsoup.connect(URL)
                    .userAgent("Mozilla/5.0 (Windows NT 5.1; zh-CN) AppleWebKit/535.12 (KHTML, like Gecko) Chrome/22.0.1229.79 Safari/535.12")
                    .timeout(6000).get();

            Elements elementsArticle = doc.select(".article").select(".block");
            if (elementsArticle != null && elementsArticle.size() <= 0) {
                return;
            }
            for (int i = 0; i < elementsArticle.size(); i++) {
                QiuBaiItemBean qiuBaiItemBean = new QiuBaiItemBean();
                Elements elementsAuthor = elementsArticle.get(i).select(".author");

                String userUrl = elementsAuthor.select("a").attr("href");
                qiuBaiItemBean.setUserId(userUrl.replace("/","").replace("users",""));
                qiuBaiItemBean.setUserUrl(HttpUrlUtil.QIU_BAI_HOME + userUrl);
                qiuBaiItemBean.setUserName(elementsAuthor.select("h2").html());
                qiuBaiItemBean.setUserImage(elementsAuthor.select("img").attr("src"));
                qiuBaiItemBean.setUserAge(elementsAuthor.select(".articleGender").html());

                if(elementsAuthor.html().contains("manIcon")){
                    qiuBaiItemBean.setUserSex("man");
                }else{
                    qiuBaiItemBean.setUserSex("women");
                }
                qiuBaiItemBean.setItemContentUrl(HttpUrlUtil.QIU_BAI_HOME + elementsArticle.get(i).select(".contentHerf").attr("href"));
                qiuBaiItemBean.setItemContent(elementsArticle.get(i).select(".content").select("span").html().replace("<br/>",System.getProperty("line.separator")).replace("<br>",System.getProperty("line.separator")));
                qiuBaiItemBean.setSmileCount(elementsArticle.get(i).select(".stats-vote").select(".number").html());
                qiuBaiItemBean.setCommentCount(elementsArticle.get(i).select(".stats-comments").select(".number").html());
                qiuBaiItemBean.setCommentGoodName(elementsArticle.get(i).select(".cmt-name").html());
                String likeNum = elementsArticle.get(i).select(".likenum").text();
                qiuBaiItemBean.setCommentGoodContent(elementsArticle.get(i).select(".main-text").text().replace(" " + likeNum,""));
                qiuBaiItemBean.setCommentGoodCount(elementsArticle.get(i).select(".likenum").text());

                qiuBaiItemBeanArrayList.add(qiuBaiItemBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
