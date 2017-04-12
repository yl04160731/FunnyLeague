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
import league.funny.com.funnyleague.adapter.BaiSiJieTextImageRecyclerAdapter;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.util.HttpUrlUtil;
import league.funny.com.funnyleague.util.Util;
import league.funny.com.funnyleague.view.LoadMoreViewFooter;
import league.funny.com.funnyleague.view.RecycleViewDivider;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaiSiJieTextImageFragment extends BaseFragment {

    private View view = null;
    private int page = 1;
    private ArrayList<ItemBean> itemBeanArrayList = new ArrayList<>();
    private BaiSiJieTextImageRecyclerAdapter adapter;

    private int type = 0;

    private boolean onFreshFlg = false;

    private SwipeRefreshHelper mSwipeRefreshHelper;
    private RecyclerAdapterWithHF mAdapter;

    @BindView(R.id.swipeRefreshLayout_text)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView_text)
    RecyclerView recyclerView;

    public BaiSiJieTextImageFragment() {
        // Required empty public constructor
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_text_image_baisijie, container, false);
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
        adapter = new BaiSiJieTextImageRecyclerAdapter(getActivity(), itemBeanArrayList);
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
                URL = HttpUrlUtil.BAI_SI_JIE_TEXT + page;
            } else {
                URL = HttpUrlUtil.BAI_SI_JIE_IMGAE + page;
            }

            Document doc = Jsoup.connect(URL)
                    .userAgent(HttpUrlUtil.USER_AGENT)
                    .timeout(HttpUrlUtil.TIMEOUT).get();

            Elements elementsUser = doc.select(".j-list-user");
            if (elementsUser != null && elementsUser.size() > 0) {
                if (onFreshFlg) itemBeanArrayList.clear();

                Elements elementsContent = doc.select(".j-r-list-c-desc");
                Elements elementsImage = doc.select(".j-r-list-c-img");
                Elements elementsDingCai = doc.select(".j-r-list-tool-l");
                Elements elementsComment = doc.select(".j-r-list-tool-r");

                for (int i = 0; i < elementsUser.size(); i++) {
                    ItemBean itemBean = new ItemBean();
                    Elements elementsAuthor = elementsUser.get(i).select(".u-img");

                    String userUrl = elementsAuthor.select("a").attr("href");
                    itemBean.setUserId(HttpUrlUtil.BAI_SI_JIE_HOME + userUrl);
                    itemBean.setUserUrl(userUrl);
                    itemBean.setUserName(Util.replaceHtmlSign(elementsAuthor.select("img").attr("alt")));
                    itemBean.setUserImage(elementsAuthor.select("img").attr("data-original"));

                    if(elementsImage != null && elementsImage.size() > 0){
                        itemBean.setItemImage(elementsImage.get(i).select("img").attr("data-original"));
                    }

                    itemBean.setItemContentUrl(HttpUrlUtil.BAI_SI_JIE_HOME + elementsContent.get(i).select("a").attr("href"));
                    itemBean.setItemContent(Util.replaceHtmlSign(elementsContent.get(i).select("a").text()));
                    itemBean.setCommentCount(elementsComment.get(i).select(".comment-counts").text());
                    itemBean.setDing(elementsDingCai.get(i).select(".j-r-list-tool-l-up").select("span").text());
                    itemBean.setCai(elementsDingCai.get(i).select(".j-r-list-tool-l-down").select("span").text());

                    itemBeanArrayList.add(itemBean);
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
