package league.funny.com.funnyleague.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.FunnyLeagueApplication;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.bean.GlobleBean;
import league.funny.com.funnyleague.bean.QiuBaiItemBean;
import league.funny.com.funnyleague.bean.QiuBaiUserBean;
import league.funny.com.funnyleague.bean.image.ImageResponse;
import league.funny.com.funnyleague.util.GlideCircleTransform;
import league.funny.com.funnyleague.util.HttpUrlUtil;
import league.funny.com.funnyleague.util.Util;

import static league.funny.com.funnyleague.R.id.userName_qiubai;

public class QiuBaiUserActivity extends BaseActivity {

    private QiuBaiItemBean qiuBaiItemBean;
    private QiuBaiUserBean qiuBaiUserBean;

    @BindView(userName_qiubai)
    public TextView userName;

    @BindView(R.id.userImage_qiubai)
    public ImageView userImage;

    @BindView(R.id.userSex_qiubai)
    public LinearLayout userSex;

    @BindView(R.id.userAge_qiubai)
    public TextView userAge;

    @BindView(R.id.constellation_qiubai)
    public TextView constellation;

    @BindView(R.id.marriage_qiubai)
    public TextView marriage;

    @BindView(R.id.back_icon_layout)
    public RelativeLayout backIconLayout;

    @BindView(R.id.text1)
    public TextView text1;

    @BindView(R.id.text2)
    public TextView text2;

    @BindView(R.id.text3)
    public TextView text3;

    @BindView(R.id.qiushiCount)
    public TextView qiushiCount;

    @BindView(R.id.smileCount)
    public TextView smileCount;

    @BindView(R.id.commentCount)
    public TextView commentCount;

    @BindView(R.id.text_forward)
    public ImageView text_forward;

    @BindView(R.id.zhiye)
    public TextView zhiye;

    @BindView(R.id.guxiang)
    public TextView guxiang;

    @BindView(R.id.qiuling)
    public TextView qiuling;

    @BindView(R.id.qiushijingxuan)
    public TextView qiushijingxuan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_qiubai);
        ((FunnyLeagueApplication) FunnyLeagueApplication.getContext()).addActivity(this);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    public void initData() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = this.getIntent();
        qiuBaiItemBean = (QiuBaiItemBean) intent.getSerializableExtra("qiuBaiItemBean");

        glideBackground(GlobleBean.imageResponse);

        qiuBaiUserBean = new QiuBaiUserBean();
        qiuBaiUserBean.setUserId(qiuBaiItemBean.getUserId());
        qiuBaiUserBean.setUserName(qiuBaiItemBean.getUserName());
        qiuBaiUserBean.setUserImage(qiuBaiItemBean.getUserImage());
        qiuBaiUserBean.setUserAge(qiuBaiItemBean.getUserAge());
        qiuBaiUserBean.setUserSex(qiuBaiItemBean.getUserSex());
        qiuBaiUserBean.setUserUrl(qiuBaiItemBean.getUserUrl());

        final String userUrl = qiuBaiItemBean.getUserUrl() + HttpUrlUtil.QIU_BAI_USER_ARTICLES;

        Runnable networkTask = new Runnable() {
            @Override
            public void run() {
                getData(userUrl);
            }
        };

        new Thread(networkTask).start();
    }

    private void getData(String userUrl) {
        try {
            Document doc = Jsoup.connect(userUrl)
                    .userAgent(HttpUrlUtil.USER_AGENT)
                    .timeout(15000).get();

            Elements elementsUserStatis = doc.select(".user-statis");
            if (elementsUserStatis != null && elementsUserStatis.size() == 3) {
                Elements userStatis = elementsUserStatis.get(0).select("ul").select("li");
                qiuBaiUserBean.setFansCount(Util.replaceHtmlSign(userStatis.get(0).ownText()));
                qiuBaiUserBean.setFollowCount(Util.replaceHtmlSign(userStatis.get(1).ownText()));
                qiuBaiUserBean.setArticlesCount(Util.replaceHtmlSign(userStatis.get(2).ownText()));
                qiuBaiUserBean.setCommentCount(Util.replaceHtmlSign(userStatis.get(3).ownText()));
                qiuBaiUserBean.setSmileCount(Util.replaceHtmlSign(userStatis.get(4).ownText()));
                qiuBaiUserBean.setBestCount(Util.replaceHtmlSign(userStatis.get(5).ownText()));

                userStatis = elementsUserStatis.get(1).select("ul").select("li");
                qiuBaiUserBean.setMarriage(Util.replaceHtmlSign(userStatis.get(0).ownText()));
                qiuBaiUserBean.setConstellation(Util.replaceHtmlSign(userStatis.get(1).ownText()));
                qiuBaiUserBean.setOccupation(Util.replaceHtmlSign(userStatis.get(2).ownText()));
                qiuBaiUserBean.setHometown(Util.replaceHtmlSign(userStatis.get(3).ownText()));
                qiuBaiUserBean.setQiuAge(Util.replaceHtmlSign(userStatis.get(4).ownText()));

                Elements userQius = doc.select(".user-block");
                ArrayList<String> stringList = new ArrayList<String>();

                for (int i = 0; i < userQius.size(); i++) {
                    String text = userQius.get(i).select(".user-article-text").text();
                    String pic = userQius.get(i).select(".user-article-pic").select("img").attr("src");
                    if (pic == null || "".equals(pic)) {
                        stringList.add(Util.replaceHtmlSign(text));
                    }
                    if (stringList.size() == 3) {
                        break;
                    }
                }
                qiuBaiUserBean.setStringList(stringList);
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

            if (qiuBaiUserBean.getConstellation() != null && !"".equals(qiuBaiUserBean.getConstellation())) {
                constellation.setText(qiuBaiUserBean.getConstellation());
                constellation.setVisibility(View.VISIBLE);
            } else {
                constellation.setVisibility(View.GONE);
            }

            if (qiuBaiUserBean.getMarriage() != null && !"".equals(qiuBaiUserBean.getMarriage())) {

                if (qiuBaiUserBean.getConstellation() != null && !"".equals(qiuBaiUserBean.getConstellation())) {
                    marriage.setText(" | " + qiuBaiUserBean.getMarriage());
                } else {
                    marriage.setText(qiuBaiUserBean.getMarriage());
                }

                marriage.setVisibility(View.VISIBLE);
            } else {
                marriage.setVisibility(View.GONE);
            }

            if(qiuBaiUserBean.getStringList() != null && qiuBaiUserBean.getStringList().size() > 0){
                text1.setText(qiuBaiUserBean.getStringList().get(0));
                if(qiuBaiUserBean.getStringList().size() > 1){
                    text2.setText(qiuBaiUserBean.getStringList().get(1));
                }
                if(qiuBaiUserBean.getStringList().size() > 2){
                    text3.setText(qiuBaiUserBean.getStringList().get(2));
                }
            }else{
                text1.setVisibility(View.INVISIBLE);
                text2.setVisibility(View.INVISIBLE);
                text3.setVisibility(View.INVISIBLE);
                text_forward.setVisibility(View.INVISIBLE);
            }

            if(qiuBaiUserBean.getArticlesCount() != null && !"".equals(qiuBaiUserBean.getArticlesCount())){
                qiushiCount.setText(qiuBaiUserBean.getArticlesCount());
            }else{
                qiushiCount.setText("0");
            }

            smileCount.setText(qiuBaiUserBean.getSmileCount() == null ? "0" : qiuBaiUserBean.getSmileCount());
            commentCount.setText(qiuBaiUserBean.getCommentCount() == null ? "0" : qiuBaiUserBean.getCommentCount());
            zhiye.setText(qiuBaiUserBean.getOccupation());
            guxiang.setText(qiuBaiUserBean.getHometown());
            qiuling.setText(qiuBaiUserBean.getQiuAge());
            qiushijingxuan.setText(qiuBaiUserBean.getBestCount());
        }
    };

    public void glideBackground(ImageResponse imageReponse) {

        userName.setText(qiuBaiItemBean.getUserName());

        if (!qiuBaiItemBean.getUserImage().contains("qiushibaike")) {
            qiuBaiItemBean.setUserImage(HttpUrlUtil.QIU_BAI_DEFAULT_USER_IMAGE);
        }
        Glide.with(QiuBaiUserActivity.this).load(qiuBaiItemBean.getUserImage()).transform(new GlideCircleTransform(QiuBaiUserActivity.this, 50)).into(userImage);
        if (qiuBaiItemBean.getUserSex() != null && !"".equals(qiuBaiItemBean.getUserSex())
                && qiuBaiItemBean.getUserAge() != null && !"".equals(qiuBaiItemBean.getUserAge())) {
            userSex.setBackgroundResource("man".equals(qiuBaiItemBean.getUserSex()) ? R.drawable.man : R.drawable.women);
            userAge.setText(qiuBaiItemBean.getUserAge());
            userSex.setVisibility(View.VISIBLE);
        } else {
            userSex.setVisibility(View.GONE);
        }

        if(imageReponse == null){
            return;
        }
        int imageCount = imageReponse.getData().getImages().size();
        int randomNumber = new Random().nextInt(imageCount);

        String imagePath = "http://wpstatic.zuimeia.com/"
                + imageReponse.getData().getImages().get(randomNumber).getImageUrl()
                + "?imageMogr/v2/auto-orient/thumbnail/480x320/quality/100";
        Glide.with(QiuBaiUserActivity.this).load(imagePath).asBitmap().into(new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap arg0, GlideAnimation<? super Bitmap> arg1) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    //Android系统大于等于API16，使用setBackground
                    backIconLayout.setBackground(new BitmapDrawable(getApplicationContext().getResources(), arg0));
                } else {
                    //Android系统小于API16，使用setBackground
                    backIconLayout.setBackgroundDrawable(new BitmapDrawable(getApplicationContext().getResources(), arg0));
                }
            }
        });




    }
}
