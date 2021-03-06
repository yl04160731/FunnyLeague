package league.funny.com.funnyleague.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import league.funny.com.funnyleague.adapter.QiuBaiTextImageRecyclerAdapter;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.util.HttpUrlUtil;
import league.funny.com.funnyleague.util.Util;
import league.funny.com.funnyleague.view.LoadMoreViewFooter;
import league.funny.com.funnyleague.view.RecycleViewDivider;

public class QiuBaiTextImageFragment extends BaseFragment {

    private View view = null;
    private int page = 1;
    private ArrayList<ItemBean> itemBeanArrayList = new ArrayList<>();
    private QiuBaiTextImageRecyclerAdapter adapter;

    private int type = 0;

    private boolean onFreshFlg = false;

    private SwipeRefreshHelper mSwipeRefreshHelper;
    private RecyclerAdapterWithHF mAdapter;

    @BindView(R.id.swipeRefreshLayout_text)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView_text)
    RecyclerView recyclerView;

    public QiuBaiTextImageFragment() {
        // Required empty public constructor
    }

    public void setType(int type){
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_text_image_qiubai, container, false);
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
        adapter = new QiuBaiTextImageRecyclerAdapter(getActivity(), itemBeanArrayList);
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
                if(itemBeanArrayList == null || itemBeanArrayList.size() <= 0){
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
            if(type == HttpUrlUtil.TYPE_TEXT){
                URL = HttpUrlUtil.QIU_BAI_TEXT_PAGE + page + HttpUrlUtil.SPRIT;
            }else{
                URL = HttpUrlUtil.QIU_BAI_IMAGE_PAGE + page + HttpUrlUtil.SPRIT;
            }

            Document doc = Jsoup.connect(URL)
                    .userAgent(HttpUrlUtil.USER_AGENT)
                    .timeout(HttpUrlUtil.TIMEOUT).get();

            Elements elementsArticle = doc.select(".article").select(".block");
            if (elementsArticle != null && elementsArticle.size() > 0) {
                if(onFreshFlg) itemBeanArrayList.clear();

                for (int i = 0; i < elementsArticle.size(); i++) {
                    ItemBean itemBean = new ItemBean();
                    Elements elementsAuthor = elementsArticle.get(i).select(".author");

                    String userUrl = elementsAuthor.select("a").attr("href");
                    itemBean.setUserId(userUrl.replace("/","").replace("users",""));
                    itemBean.setUserUrl(HttpUrlUtil.QIU_BAI_HOME + userUrl);
                    itemBean.setUserName(Util.replaceHtmlSign(elementsAuthor.select("h2").html()));
                    itemBean.setUserImage("http://" + elementsAuthor.select("img").attr("src").substring(2));
                    itemBean.setUserAge(elementsAuthor.select(".articleGender").html());

                    if(elementsAuthor.html().contains("manIcon")){
                        itemBean.setUserSex("man");
                    }else if(elementsAuthor.html().contains("womenIcon")){
                        itemBean.setUserSex("women");
                    }

                    itemBean.setItemContentUrl(HttpUrlUtil.QIU_BAI_HOME + elementsArticle.get(i).select(".contentHerf").attr("href"));
                    itemBean.setItemContent(Util.replaceHtmlSign(elementsArticle.get(i).select(".content").select("span").html()));
                    if(elementsArticle.get(i).select(".thumb").select("img").attr("src") != null &&
                            !"".equals(elementsArticle.get(i).select(".thumb").select("img").attr("src"))){
                        itemBean.setItemImage("http://" + elementsArticle.get(i).select(".thumb").select("img").attr("src").substring(2));
                    }
                    itemBean.setSmileCount(elementsArticle.get(i).select(".stats-vote").select(".number").html());
                    itemBean.setCommentCount(elementsArticle.get(i).select(".stats-comments").select(".number").html());
                    itemBean.setCommentGoodName(Util.replaceHtmlSign(elementsArticle.get(i).select(".cmt-name").html()));
                    String likeNum = elementsArticle.get(i).select(".likenum").text();
                    itemBean.setCommentGoodContent(Util.replaceHtmlSign(elementsArticle.get(i).select(".main-text").text().replace(" " + likeNum,"")));
                    itemBean.setCommentGoodCount(elementsArticle.get(i).select(".likenum").text());

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
