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
import android.widget.RelativeLayout;
import android.widget.TableLayout;
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
import butterknife.OnClick;
import league.funny.com.funnyleague.FunnyLeagueApplication;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.bean.GlobleBean;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.bean.UserBean;
import league.funny.com.funnyleague.bean.image.ImageResponse;
import league.funny.com.funnyleague.util.GlideCircleTransform;
import league.funny.com.funnyleague.util.HttpUrlUtil;
import league.funny.com.funnyleague.util.Util;

import static league.funny.com.funnyleague.R.id.userName_pengfu;

public class PengFuUserActivity extends BaseActivity {

    private ItemBean itemBean;
    private UserBean userBean;

    @BindView(userName_pengfu)
    public TextView userName;

    @BindView(R.id.userImage_pengfu)
    public ImageView userImage;

    @BindView(R.id.back_icon_layout)
    public RelativeLayout backIconLayout;

    @BindView(R.id.text1)
    public TextView text1;

    @BindView(R.id.text2)
    public TextView text2;

    @BindView(R.id.text3)
    public TextView text3;

    @BindView(R.id.text_forward)
    public ImageView text_forward;

    @BindView(R.id.fangwenliang)
    public TextView fangwenliang;

    @BindView(R.id.dengji)
    public TextView dengji;

    @BindView(R.id.jingyan)
    public TextView jingyan;

    @BindView(R.id.qianming)
    public TextView qianming;

    @BindView(R.id.user_info_wait)
    public RelativeLayout user_info_wait;

    @BindView(R.id.user_info_table)
    public TableLayout user_info_table;

    @BindView(R.id.smileIcon)
    public ImageView smileIcon;

    @BindView(R.id.message)
    public TextView message;

    @BindView(R.id.levelName_pengfu)
    public TextView levelName_pengfu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pengfu);
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
        itemBean = (ItemBean) intent.getSerializableExtra("itemBean");

        glideBackground(GlobleBean.imageResponse);

        userBean = new UserBean();
        userBean.setUserId(itemBean.getUserId());
        userBean.setUserName(itemBean.getUserName());
        userBean.setUserImage(itemBean.getUserImage());
        userBean.setUserUrl(itemBean.getUserUrl());

        final String userUrl = itemBean.getUserUrl();

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

            Elements elementsUserStatis = doc.select(".fl").select(".level-txt").select(".mr10");

            if(elementsUserStatis != null && elementsUserStatis.size() > 1){
                userBean.setDengji(elementsUserStatis.get(0).text());
                userBean.setJingyan(elementsUserStatis.get(1).text());
            }

            userBean.setLevelName(doc.select(".level_icon").attr("title"));
            userBean.setFangwenliang(doc.getElementById("visit_num").text());
            userBean.setQianming(Util.replaceHtmlSign(doc.select(".h20").select(".gray")
                    .select(".f14").text()).replace("签名：","").replace("设置签名",""));

            Elements elementsUserTieZi = doc.select(".clearfix").select(".dl-con");
            ArrayList<String> stringList = new ArrayList<String>();

            if(elementsUserTieZi != null){
                for (int i = 0; i < elementsUserTieZi.size(); i++) {
                    String text = elementsUserTieZi.get(i).select(".content-img").text();
                    String pic = elementsUserTieZi.get(i).select(".content-img").select("img").attr("src");
                    String title = elementsUserTieZi.get(i).select(".dp-b").text();
                    if (pic == null || "".equals(pic)) {
                        stringList.add(Util.replaceHtmlSign(text));
                    }else{
                        stringList.add(Util.replaceHtmlSign(title));
                    }
                    if (stringList.size() == 3) {
                        break;
                    }
                }
                userBean.setStringList(stringList);
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
            if (msg.what == 1) {

                if (userBean.getStringList() != null && userBean.getStringList().size() > 0) {
                    text1.setText(userBean.getStringList().get(0));
                    if (userBean.getStringList().size() > 1) {
                        text2.setText(userBean.getStringList().get(1));
                    } else {
                        text2.setVisibility(View.INVISIBLE);
                        text3.setVisibility(View.INVISIBLE);
                    }
                    if (userBean.getStringList().size() > 2) {
                        text3.setText(userBean.getStringList().get(2));
                    } else {
                        text3.setVisibility(View.INVISIBLE);
                    }
                } else {
                    text1.setVisibility(View.INVISIBLE);
                    text2.setVisibility(View.INVISIBLE);
                    text3.setVisibility(View.INVISIBLE);
                    text_forward.setVisibility(View.INVISIBLE);
                }

                fangwenliang.setText(userBean.getFangwenliang());
                dengji.setText(userBean.getDengji());
                jingyan.setText(userBean.getJingyan());
                qianming.setText(userBean.getQianming());
                levelName_pengfu.setText(userBean.getLevelName());

                user_info_wait.setVisibility(View.GONE);
                user_info_table.setVisibility(View.VISIBLE);

            }
        }
    };

    public void glideBackground(ImageResponse imageReponse) {

        userName.setText(itemBean.getUserName());

        Glide.with(FunnyLeagueApplication.getApplication()).load(itemBean.getUserImage()).transform(new GlideCircleTransform(FunnyLeagueApplication.getApplication(), 55)).into(userImage);

        if (imageReponse == null) {
            return;
        }
        int imageCount = imageReponse.getData().getImages().size();
        int randomNumber = new Random().nextInt(imageCount);

        String imagePath = "http://wpstatic.zuimeia.com/"
                + imageReponse.getData().getImages().get(randomNumber).getImageUrl()
                + "?imageMogr/v2/auto-orient/thumbnail/480x320/quality/100";
        Glide.with(FunnyLeagueApplication.getApplication()).load(imagePath).asBitmap().into(new SimpleTarget<Bitmap>() {

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

    @OnClick(R.id.text_forward)
    public void text_forward() {
        toUserContent();
    }

    @OnClick(R.id.text1)
    public void text1() {
        toUserContent();
    }

    @OnClick(R.id.text2)
    public void text2() {
        toUserContent();
    }

    @OnClick(R.id.text3)
    public void text3() {
        toUserContent();
    }

    public void toUserContent() {
        Intent intent = new Intent();
        intent.setClass(this, QiuBaiUserTextImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("itemBean", itemBean);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }
}
