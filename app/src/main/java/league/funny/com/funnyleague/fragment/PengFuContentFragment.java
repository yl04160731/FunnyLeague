package league.funny.com.funnyleague.fragment;


import android.content.Intent;
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
import butterknife.OnClick;
import league.funny.com.funnyleague.FunnyLeagueApplication;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.activity.PengFuUserActivity;
import league.funny.com.funnyleague.adapter.PengFuCommentRecyclerAdapter;
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
public class PengFuContentFragment extends Fragment {


    @BindView(R.id.userName_pengfu)
    public TextView userName;

    @BindView(R.id.userImage_pengfu)
    public ImageView userImage;

    @BindView(R.id.itemContent_pengfu)
    public TextView itemContent;

    @BindView(R.id.PuTongCommentCount_pengfu)
    public TextView puTongCommentCount;

    @BindView(R.id.putong_comment_layout)
    public LinearLayout putongCommentLayout;

    @BindView(R.id.putong_comment_recyclerView)
    public RecyclerView putongCommentRecyclerView;

    @BindView(R.id.commentLinearLayout_pengfu)
    public LinearLayout commentLinearLayout;

    @BindView(R.id.comment_wait)
    public RelativeLayout commentWait;

    @BindView(R.id.itemTitle_pengfu)
    TextView itemTitle;

    @BindView(R.id.ding)
    TextView ding;

    @BindView(R.id.cai)
    TextView cai;

    @BindView(R.id.comment)
    TextView comment;

    @BindView(R.id.itemImage_pengfu)
    public ImageView itemImage_pengfu;

    private View view = null;
    private PengFuCommentRecyclerAdapter pengFuCommentRecyclerAdapter;
    private ArrayList<CommentBean> CommentList = new ArrayList<>();
    private ItemBean itemBean;

    public PengFuContentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_content_pengfu, container, false);
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

    @OnClick(R.id.userImage_pengfu)
    public void toHomeUser0() {
        toUserInfo();
    }

    @OnClick(R.id.userName_pengfu)
    public void toHomeUser1() {
        toUserInfo();
    }

    public void toUserInfo() {
        if (itemBean.getUserUrl() == null || "".equals(itemBean.getUserUrl().replace(HttpUrlUtil.QIU_BAI_HOME, ""))) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(getActivity(), PengFuUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("itemBean", itemBean);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
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
        itemTitle.setText(itemBean.getItemContentTitle());
        TextPaint tp = itemTitle.getPaint();
        tp.setFakeBoldText(true);

        ding.setText(itemBean.getDing());
        cai.setText(itemBean.getCai());
        comment.setText(itemBean.getCommentCount());

        if(itemBean.getItemContent() != null && !"".equals(itemBean.getItemContent())){
            itemContent.setVisibility(View.VISIBLE);
            itemContent.setText(itemBean.getItemContent());
        }else{
            itemContent.setVisibility(View.GONE);
        }

        if (itemBean.getItemImage() != null && !"".equals(itemBean.getItemImage())) {
            itemImage_pengfu.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = itemImage_pengfu.getLayoutParams();
            int screenWidth = getScreenWidth(FunnyLeagueApplication.getApplication());
            params.width = screenWidth * 11 / 12;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            itemImage_pengfu.setLayoutParams(params);
            itemImage_pengfu.setMaxWidth(screenWidth);

            Glide.with(FunnyLeagueApplication.getApplication()).load(itemBean.getItemImage())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate().error(R.drawable.imageload).placeholder(R.drawable.imageload)
                    .into(itemImage_pengfu);

        } else {
            itemImage_pengfu.setVisibility(View.GONE);
        }
    }

    private void getData() {
        try {
            Document doc = Jsoup.connect(itemBean.getItemContentUrl())
                    .userAgent(HttpUrlUtil.USER_AGENT)
                    .timeout(HttpUrlUtil.TIMEOUT).get();

            String noComment = doc.select(".noComment").text();

            if (noComment == null || "".equals(noComment)) {

                Elements elementsComments = doc.select(".comment-list").select("li");

                if (elementsComments != null && !"".equals(elementsComments)) {
                    for (int i = 0; i < elementsComments.size(); i++) {
                        CommentBean commentBean = new CommentBean();
                        String userUrl = elementsComments.get(i).select(".mem-header").attr("href");
                        commentBean.setUserId(userUrl.replace(HttpUrlUtil.PENG_FU_USER, ""));
                        commentBean.setUserUrl(userUrl);

                        commentBean.setUserName(Util.replaceHtmlSign(elementsComments.get(i).select(".mem-header").select("img").attr("alt")));
                        commentBean.setUserImage(elementsComments.get(i).select(".mem-header").select("img").attr("src"));

                        commentBean.setFloor(elementsComments.get(i).select(".f12").select(".gray2").select(".dp-i-b").text());
                        commentBean.setCommentContent(Util.replaceHtmlSign(elementsComments.get(i).select(".comment-content").text()));
                        CommentList.add(commentBean);
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

    Handler HtmlHandler = new Handler() {
        public void handleMessage(Message msg) {

            if (CommentList.size() <= 0) {
                putongCommentLayout.setVisibility(View.GONE);
            } else {
                putongCommentLayout.setVisibility(View.VISIBLE);
                pengFuCommentRecyclerAdapter = new PengFuCommentRecyclerAdapter(getActivity(), CommentList);
                putongCommentRecyclerView.setAdapter(pengFuCommentRecyclerAdapter);
                puTongCommentCount.setText(getResources().getText(R.string.putong_comment)
                        + "(" + CommentList.size() + ")");
            }

            commentWait.setVisibility(View.GONE);
            commentLinearLayout.setVisibility(View.VISIBLE);
        }
    };
}
