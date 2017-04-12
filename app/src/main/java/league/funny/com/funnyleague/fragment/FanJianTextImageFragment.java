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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.adapter.FanJianTextImageRecyclerAdapter;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.util.HttpUrlUtil;
import league.funny.com.funnyleague.util.Util;
import league.funny.com.funnyleague.view.LoadMoreViewFooter;
import league.funny.com.funnyleague.view.RecycleViewDivider;

/**
 * A simple {@link Fragment} subclass.
 */
public class FanJianTextImageFragment extends Fragment {

    private View view = null;
    private int page = 1;
    private ArrayList<ItemBean> itemBeanArrayList = new ArrayList<>();
    private FanJianTextImageRecyclerAdapter adapter;

    private int type = 0;

    private boolean onFreshFlg = false;

    private SwipeRefreshHelper mSwipeRefreshHelper;
    private RecyclerAdapterWithHF mAdapter;

    @BindView(R.id.swipeRefreshLayout_text)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView_text)
    RecyclerView recyclerView;

    public FanJianTextImageFragment() {
        // Required empty public constructor
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_text_image_fanjian, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    public void initView() {

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), RecycleViewDivider.VERTICAL_LIST, R.drawable.divider));
    }

    public void initData() {
        adapter = new FanJianTextImageRecyclerAdapter(getActivity(), itemBeanArrayList);
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
            if (onFreshFlg) {
                mSwipeRefreshHelper.refreshComplete();
                if (itemBeanArrayList == null || itemBeanArrayList.size() <= 0) {
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
        try {
            String URL = null;
            if (type == HttpUrlUtil.TYPE_TEXT) {
                URL = HttpUrlUtil.FAN_JIAN_TEXT + HttpUrlUtil.HYPHEN + page;
                Document doc = Jsoup.connect(URL)
                        .userAgent(HttpUrlUtil.USER_AGENT)
                        .timeout(HttpUrlUtil.TIMEOUT).get();

                Elements elementsArticle = doc.select(".cont-item");
                if (elementsArticle != null && elementsArticle.size() > 0) {
                    if (onFreshFlg) itemBeanArrayList.clear();

                    for (int i = 0; i < elementsArticle.size(); i++) {
                        ItemBean itemBean = new ItemBean();
                        Elements elementsAuthor = elementsArticle.get(i).select(".joke-list-editor").select(".cont-list-reward");

                        String userUrl = elementsAuthor.select("a").attr("href");
                        itemBean.setUserId(userUrl.replace(HttpUrlUtil.FAN_JIAN_USER, ""));
                        itemBean.setUserUrl(userUrl);
                        itemBean.setUserName(Util.replaceHtmlSign(elementsAuthor.select("a").attr("title")));
                        itemBean.setUserImage(elementsAuthor.select("img").attr("data-src"));

                        itemBean.setItemContentUrl(elementsArticle.get(i).select(".joke-list-in").select("a").attr("href"));
                        itemBean.setItemContent(Util.replaceHtmlSign(elementsArticle.get(i).select(".joke-list-txt").text()));
                        itemBean.setCommentCount(elementsArticle.get(i).select(".fc-gray").get(2).select("i").text());
                        itemBean.setDing(elementsArticle.get(i).select(".fc-gray").select(".like").select("i").text());
                        itemBean.setCai(elementsArticle.get(i).select(".fc-gray").select(".unlike").select("i").text());

                        itemBeanArrayList.add(itemBean);
                    }
                }
            } else {
                URL = HttpUrlUtil.FAN_JIAN_IMAGE + HttpUrlUtil.HYPHEN + page;
                Document doc = Jsoup.connect(URL)
                        .userAgent(HttpUrlUtil.USER_AGENT)
                        .timeout(HttpUrlUtil.TIMEOUT).get();

                Elements elementsArticle = doc.select(".cont-item");
                if (elementsArticle != null && elementsArticle.size() > 0) {
                    if (onFreshFlg) itemBeanArrayList.clear();
                    for (int i = 0; i < elementsArticle.size(); i++) {

                        String title = elementsArticle.get(i).select(".cont-list-title").text();
                        if (title == null || "".equals(title)) {
                            continue;
                        }else{
                            ItemBean itemBean = new ItemBean();
                            itemBean.setItemContentTitle(title);

                            Elements elementsAuthor = elementsArticle.get(i).select(".cont-list-reward");

                            String userUrl = elementsAuthor.select("a").attr("href");
                            itemBean.setUserId(userUrl.replace(HttpUrlUtil.FAN_JIAN_USER, ""));
                            itemBean.setUserUrl(userUrl);
                            itemBean.setUserName(Util.replaceHtmlSign(elementsAuthor.select("a").attr("title")));
                            itemBean.setUserImage(elementsAuthor.select("img").attr("src"));

                            itemBean.setItemContentUrl(elementsArticle.get(i).select(".cont-list-in").select("a").attr("href"));
                            itemBean.setItemContent(Util.replaceHtmlSign(elementsArticle.get(i).select(".cont-list-main").text()));
                            String tempSrc = elementsArticle.get(i).select(".cont-list-main").select("img").attr("data-src");
                            if(tempSrc != null && !"".equals(tempSrc)){
                                itemBean.setItemImage(tempSrc);
                            }else{
                                itemBean.setItemImage(elementsArticle.get(i).select(".cont-list-main").select("img").attr("src"));
                            }

                            String tag = elementsArticle.get(i).select(".cont-list-tags").text();

                            if(tag != null && !"".equals(tag)){
                                tag = tag.substring(1,tag.length());
                            }

                            itemBean.setSortTag(tag);
                            itemBean.setSortTagUrl(elementsArticle.get(i).select(".cont-list-tags").select("a").attr("href"));
                            itemBean.setCommentCount(elementsArticle.get(i).select(".fc-gray").get(7).select("i").text());
                            itemBean.setDing(elementsArticle.get(i).select(".fc-gray").select(".like").select("i").text());
                            itemBean.setCai(elementsArticle.get(i).select(".fc-gray").select(".unlike").select("i").text());

                            itemBeanArrayList.add(itemBean);

                        }
                    }
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
