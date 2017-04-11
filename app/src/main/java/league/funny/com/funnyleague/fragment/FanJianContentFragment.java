package league.funny.com.funnyleague.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.FunnyLeagueApplication;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.adapter.FanJianCommentRecyclerAdapter;
import league.funny.com.funnyleague.bean.CommentBean;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.util.GlideCircleTransform;
import league.funny.com.funnyleague.util.HttpUrlUtil;
import league.funny.com.funnyleague.util.Util;
import league.funny.com.funnyleague.view.RecycleViewDivider;

import static com.ashokvarma.bottomnavigation.utils.Utils.getScreenWidth;

/**
 * A simple {@link Fragment} subclass.
 */
public class FanJianContentFragment extends Fragment {

    @BindView(R.id.userName_fanjian)
    public TextView userName;

    @BindView(R.id.userImage_fanjian)
    public ImageView userImage;

    @BindView(R.id.itemContent_fanjian)
    public TextView itemContent;

    @BindView(R.id.PuTongCommentCount_fanjian)
    public TextView puTongCommentCount;

    @BindView(R.id.putong_comment_layout)
    public LinearLayout putongCommentLayout;

    @BindView(R.id.putong_comment_recyclerView)
    public RecyclerView putongCommentRecyclerView;

    @BindView(R.id.commentLinearLayout_fanjian)
    public LinearLayout commentLinearLayout;

    @BindView(R.id.comment_wait)
    public RelativeLayout commentWait;

    @BindView(R.id.itemTitle_fanjian)
    TextView itemTitle;

    @BindView(R.id.ding)
    TextView ding;

    @BindView(R.id.cai)
    TextView cai;

    @BindView(R.id.comment)
    TextView comment;

    @BindView(R.id.itemImage_fanjian)
    public ImageView itemImage_fanjian;

    private View view = null;
    private FanJianCommentRecyclerAdapter fanjianCommentRecyclerAdapter;
    private ArrayList<CommentBean> CommentList = new ArrayList<>();
    private ItemBean itemBean;

    private String commentCount = "0";

    public FanJianContentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_content_fanjian, container, false);
        ButterKnife.bind(this, view);
        initData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        putongCommentRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), RecycleViewDivider.VERTICAL_LIST, R.drawable.small_divider));
        putongCommentRecyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    public void setItemBean(ItemBean itemBean) {
        this.itemBean = itemBean;
    }

    public void initData() {

        Runnable networkTask = new Runnable() {
            @Override
            public void run() {
                getData();
            }
        };

        new Thread(networkTask).start();

        userName.setText(itemBean.getUserName());
        Glide.with(FunnyLeagueApplication.getApplication()).load(itemBean.getUserImage()).transform(new GlideCircleTransform(FunnyLeagueApplication.getApplication(), 45)).into(userImage);
        itemContent.setText(itemBean.getItemContent());
        if(itemBean.getItemContentTitle() == null || "".equals(itemBean.getItemContentTitle())){
            itemTitle.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams=
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            layoutParams.setMargins(Util.dp2px(getActivity(),10),0,0,0);
            userName.setLayoutParams(layoutParams);
        }else{
            itemTitle.setVisibility(View.VISIBLE);
            itemTitle.setText(itemBean.getItemContentTitle());
            TextPaint tp = itemTitle.getPaint();
            tp.setFakeBoldText(true);
        }

        ding.setText(itemBean.getDing());
        cai.setText(itemBean.getCai());
        comment.setText(itemBean.getCommentCount());

        if (itemBean.getItemContent() != null && !"".equals(itemBean.getItemContent())) {
            itemContent.setVisibility(View.VISIBLE);
            itemContent.setText(itemBean.getItemContent());
        } else {
            itemContent.setVisibility(View.GONE);
        }

        if (itemBean.getItemImage() != null && !"".equals(itemBean.getItemImage())) {
            itemImage_fanjian.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = itemImage_fanjian.getLayoutParams();
            int screenWidth = getScreenWidth(FunnyLeagueApplication.getApplication());
            params.width = screenWidth * 11 / 12;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            itemImage_fanjian.setLayoutParams(params);
            itemImage_fanjian.setMaxWidth(screenWidth);

            Glide.with(FunnyLeagueApplication.getApplication()).load(itemBean.getItemImage())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate().error(R.drawable.imageload).placeholder(R.drawable.imageload)
                    .into(itemImage_fanjian);

        } else {
            itemImage_fanjian.setVisibility(View.GONE);
        }
    }

    private void getData() {
        try {
            Document doc = Jsoup.connect(itemBean.getItemContentUrl())
                    .userAgent(HttpUrlUtil.USER_AGENT)
                    .timeout(HttpUrlUtil.TIMEOUT).get();

            Elements elements = doc.select(".comment-list");

            if (elements.text() != null && !"".equals(elements.text())) {
                commentCount = doc.getElementById("anchor-comment").select("i").text();
                Elements elementsComments = elements.select(".comment-item");

                for (int i = 0; i < elementsComments.size(); i++) {
                    CommentBean commentBean = new CommentBean();
                    String userUrl = elementsComments.get(i).select(".comment-head").get(0).select("a").attr("href");
                    commentBean.setUserId(userUrl.replace(HttpUrlUtil.FAN_JIAN_USER, ""));
                    commentBean.setUserUrl(userUrl);
                    commentBean.setUserName(Util.replaceHtmlSign(elementsComments.get(i).select(".comment-author").select(".fc-gblue").text()));
                    commentBean.setUserImage(elementsComments.get(i).select(".comment-head").get(0).select("img").attr("data-src"));
                    commentBean.setGoodCount(elementsComments.get(i).select(".clike").select(".fc-gray").select("i").text());
                    commentBean.setCommentContent(Util.replaceHtmlSign(elementsComments.get(i).select(".comment-content").text()));
                    commentBean.setFloor(elementsComments.get(i).select(".comment-floor").get(0).select("i").text());
                    Elements elementsCommentReplys = elementsComments.get(i).select(".comment-reply-item");
                    if(elementsCommentReplys != null){
                        ArrayList<CommentBean> commentSubBeanList = new ArrayList<CommentBean>();
                        for(int j = 0; j < elementsCommentReplys.size(); j++){
                            CommentBean commentSubBean = new CommentBean();
                            userUrl = elementsCommentReplys.get(j).select("comment-reply-content").select("a").attr("href");
                            commentSubBean.setUserId(userUrl.replace(HttpUrlUtil.FAN_JIAN_USER, ""));
                            commentSubBean.setUserUrl(userUrl);
                            commentSubBean.setUserName(Util.replaceHtmlSign(elementsCommentReplys.get(j).select(".comment-reply-content").select(".comment-reply-author").text()));
                            commentSubBean.setUserImage(elementsCommentReplys.get(j).select(".user-head").select("img").attr("data-src"));
                            commentSubBean.setGoodCount(elementsCommentReplys.get(j).select(".clike").select(".fc-gray").select("i").text());
                            commentSubBean.setCommentContent(Util.replaceHtmlSign(elementsCommentReplys.get(j).select(".comment-reply-txt").text()));
                            commentSubBean.setReplyUser(Util.replaceHtmlSign(elementsCommentReplys.get(j).select(".comment-reply-who").text()));
                            commentSubBeanList.add(commentSubBean);
                        }
                        commentBean.setCommentbeanList(commentSubBeanList);
                    }

                    CommentList.add(commentBean);
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

    Handler HtmlHandler = new Handler() {
        public void handleMessage(Message msg) {

            if (CommentList.size() <= 0) {
                putongCommentLayout.setVisibility(View.GONE);
            } else {
                putongCommentLayout.setVisibility(View.VISIBLE);
                fanjianCommentRecyclerAdapter = new FanJianCommentRecyclerAdapter(getActivity(), CommentList);
                putongCommentRecyclerView.setAdapter(fanjianCommentRecyclerAdapter);
                puTongCommentCount.setText(getResources().getText(R.string.putong_comment)
                        + "(" + commentCount + ")");
            }

            commentWait.setVisibility(View.GONE);
            commentLinearLayout.setVisibility(View.VISIBLE);
        }
    };

}
