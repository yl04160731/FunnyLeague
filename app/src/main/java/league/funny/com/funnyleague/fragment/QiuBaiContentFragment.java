package league.funny.com.funnyleague.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import league.funny.com.funnyleague.FunnyLeagueApplication;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.activity.QiuBaiUserActivity;
import league.funny.com.funnyleague.adapter.QiuBaiCommentRecyclerAdapter;
import league.funny.com.funnyleague.bean.QiuBaiCommentBean;
import league.funny.com.funnyleague.bean.QiuBaiItemBean;
import league.funny.com.funnyleague.util.GlideCircleTransform;
import league.funny.com.funnyleague.util.HttpUrlUtil;
import league.funny.com.funnyleague.util.Util;
import league.funny.com.funnyleague.view.RecycleViewDivider;

/**
 * A simple {@link Fragment} subclass.
 */
public class QiuBaiContentFragment extends BaseFragment {

    @BindView(R.id.userName_qiubai)
    public TextView userName;

    @BindView(R.id.userImage_qiubai)
    public ImageView userImage;

    @BindView(R.id.userSex_qiubai)
    public LinearLayout userSex;

    @BindView(R.id.userAge_qiubai)
    public TextView userAge;

    @BindView(R.id.itemContent_qiubai)
    public TextView itemContent;

    @BindView(R.id.smileCount_qiubai)
    public TextView smileCount;

    @BindView(R.id.commentCount_qiubai)
    public TextView commentCount;

    @BindView(R.id.shenCommentCount_qiubai)
    public TextView shenCommentCount;

    @BindView(R.id.PuTongCommentCount_qiubai)
    public TextView puTongCommentCount;

    @BindView(R.id.hot_comment_layout)
    public LinearLayout hotCommentLayout;

    @BindView(R.id.putong_comment_layout)
    public LinearLayout putongCommentLayout;

    @BindView(R.id.shen_comment_recyclerView)
    public RecyclerView shenCommentRecyclerView;

    @BindView(R.id.putong_comment_recyclerView)
    public RecyclerView putongCommentRecyclerView;

    @BindView(R.id.scrollView)
    public ScrollView scrollView;

    @BindView(R.id.commentLinearLayout_qiubai)
    public LinearLayout commentLinearLayout;

    @BindView(R.id.comment_wait)
    public RelativeLayout commentWait;

    private View view = null;
    private QiuBaiCommentRecyclerAdapter qiuBaiCommentRecyclerAdapter;
    private ArrayList<QiuBaiCommentBean> shenCommentList = new ArrayList<>();
    private ArrayList<QiuBaiCommentBean> CommentList = new ArrayList<>();
    private QiuBaiItemBean qiuBaiItemBean;

    public QiuBaiContentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_content_qiubai, container, false);
        ButterKnife.bind(this, view);
        initData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        putongCommentRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(),RecycleViewDivider.VERTICAL_LIST,R.drawable.small_divider));
        shenCommentRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(),RecycleViewDivider.VERTICAL_LIST,R.drawable.small_divider));

        putongCommentRecyclerView.setLayoutManager(linearLayoutManager);
        shenCommentRecyclerView.setLayoutManager(linearLayoutManager1);
        return view;
    }

    @OnClick(R.id.userImage_qiubai)
    public void toHomeUser0(){
        toUserInfo();
    }

    @OnClick(R.id.userName_qiubai)
    public void toHomeUser1(){
        toUserInfo();
    }

    public void toUserInfo(){
        if(qiuBaiItemBean.getUserUrl() == null || "".equals(qiuBaiItemBean.getUserUrl().replace(HttpUrlUtil.QIU_BAI_HOME,""))){
            return;
        }
        Intent intent = new Intent();
        intent.setClass(getActivity(), QiuBaiUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("qiuBaiItemBean",qiuBaiItemBean);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    public void setQiuBaiItemBean(QiuBaiItemBean qiuBaiItemBean){
        this.qiuBaiItemBean = qiuBaiItemBean;
    }

    public void initData(){

        Runnable networkTask = new Runnable() {
            @Override
            public void run() {
                getData();
            }
        };

        new Thread(networkTask).start();

        userName.setText(qiuBaiItemBean.getUserName());

        if (!qiuBaiItemBean.getUserImage().contains("qiushibaike")) {
            qiuBaiItemBean.setUserImage(HttpUrlUtil.QIU_BAI_DEFAULT_USER_IMAGE);
        }
        Glide.with(FunnyLeagueApplication.getApplication()).load(qiuBaiItemBean.getUserImage()).transform(new GlideCircleTransform(FunnyLeagueApplication.getApplication(), 45)).into(userImage);
        if (qiuBaiItemBean.getUserSex() != null && !"".equals(qiuBaiItemBean.getUserSex())
                && qiuBaiItemBean.getUserAge() != null && !"".equals(qiuBaiItemBean.getUserAge())) {
            userSex.setBackgroundResource("man".equals(qiuBaiItemBean.getUserSex()) ? R.drawable.man : R.drawable.women);
            userAge.setText(qiuBaiItemBean.getUserAge());
            userSex.setVisibility(View.VISIBLE);
        }else{
            userSex.setVisibility(View.GONE);
        }

        itemContent.setText(qiuBaiItemBean.getItemContent());
        smileCount.setText(qiuBaiItemBean.getSmileCount());
        commentCount.setText(qiuBaiItemBean.getCommentCount());
    }

    private void getData() {
        try {
            Document doc = Jsoup.connect(qiuBaiItemBean.getItemContentUrl())
                    .userAgent(HttpUrlUtil.USER_AGENT)
                    .timeout(15000).get();

            Elements elementsShenComments = doc.select(".comments-table");

            if(elementsShenComments != null && !"".equals(elementsShenComments)){
                for(int i = 0;i < elementsShenComments.size();i++){
                    QiuBaiCommentBean qiuBaiCommentBean = new QiuBaiCommentBean();
                    String userUrl = elementsShenComments.get(i).select(".comments-table-main").attr("href");
                    qiuBaiCommentBean.setUserUrl(HttpUrlUtil.QIU_BAI_HOME + userUrl);
                    qiuBaiCommentBean.setUserId(userUrl.replace("/","").replace("users",""));

                    qiuBaiCommentBean.setUserName(Util.replaceHtmlSign(elementsShenComments.get(i).select(".cmt-name").html()));
                    qiuBaiCommentBean.setUserImage(elementsShenComments.get(i).select(".avatar").attr("src"));
                    qiuBaiCommentBean.setUserAge(elementsShenComments.get(i).select(".articleGender").html());
                    if(elementsShenComments.get(i).html().contains("manIcon")){
                        qiuBaiCommentBean.setUserSex("man");
                    }else if(elementsShenComments.get(i).html().contains("womenIcon")){
                        qiuBaiCommentBean.setUserSex("women");
                    }

                    qiuBaiCommentBean.setCommentContent(Util.replaceHtmlSign(elementsShenComments.get(i).select(".main-text").html()));
                    qiuBaiCommentBean.setGoodCount(elementsShenComments.get(i).select(".likenum").text());
                    shenCommentList.add(qiuBaiCommentBean);
                }
            }

            String commentCountAll = doc.getElementById("comments-num") == null?"":doc.getElementById("comments-num").text();

            Elements elementsComments = doc.select(".comment-block");

            if(elementsComments != null && !"".equals(elementsComments)){
                for(int i = 0;i < elementsComments.size();i++){
                    QiuBaiCommentBean qiuBaiCommentBean = new QiuBaiCommentBean();
                    String userUrl = elementsComments.get(i).select(".avatars").select("a").attr("href");
                    qiuBaiCommentBean.setUserId(userUrl.replace("/","").replace("users",""));
                    qiuBaiCommentBean.setUserUrl(HttpUrlUtil.QIU_BAI_HOME + userUrl);

                    String age = elementsComments.get(i).select(".articleCommentGender").html();
                    qiuBaiCommentBean.setUserAge(age);
                    qiuBaiCommentBean.setUserName(Util.replaceHtmlSign(elementsComments.get(i).select(".userlogin").text().replace(" " + age,"")));
                    qiuBaiCommentBean.setUserImage(elementsComments.get(i).select(".avatars").select("img").attr("src"));

                    if(elementsComments.get(i).html().contains("manIcon")){
                        qiuBaiCommentBean.setUserSex("man");
                    }else if(elementsComments.get(i).html().contains("womenIcon")){
                        qiuBaiCommentBean.setUserSex("women");
                    }

                    qiuBaiCommentBean.setFloor(elementsComments.get(i).select(".report").text());
                    qiuBaiCommentBean.setCommentContent(Util.replaceHtmlSign(elementsComments.get(i).select(".replay").select(".body").text()));
                    CommentList.add(qiuBaiCommentBean);
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

            if(shenCommentList.size() <= 0){
                hotCommentLayout.setVisibility(View.GONE);
            }else{
                hotCommentLayout.setVisibility(View.VISIBLE);
                shenCommentCount.setText(getResources().getText(R.string.hot_comment)
                        + "(" + shenCommentList.size() + ")");
                QiuBaiCommentRecyclerAdapter qiuBaishenCommentListAdapter = new QiuBaiCommentRecyclerAdapter(getActivity(),shenCommentList);
                shenCommentRecyclerView.setAdapter(qiuBaishenCommentListAdapter);
            }

            if(CommentList.size() <= 0){
                putongCommentLayout.setVisibility(View.GONE);
            }else{
                putongCommentLayout.setVisibility(View.VISIBLE);
                qiuBaiCommentRecyclerAdapter = new QiuBaiCommentRecyclerAdapter(getActivity(),CommentList);
                putongCommentRecyclerView.setAdapter(qiuBaiCommentRecyclerAdapter);
                puTongCommentCount.setText(getResources().getText(R.string.putong_comment)
                        + "(" + CommentList.size() + ")");
            }

            commentWait.setVisibility(View.GONE);
            commentLinearLayout.setVisibility(View.VISIBLE);
        }
    };
}
