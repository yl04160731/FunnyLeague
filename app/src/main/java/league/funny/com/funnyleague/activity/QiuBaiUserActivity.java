package league.funny.com.funnyleague.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.FunnyLeagueApplication;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.bean.QiuBaiItemBean;
import league.funny.com.funnyleague.bean.QiuBaiUserBean;
import league.funny.com.funnyleague.util.GlideCircleTransform;
import league.funny.com.funnyleague.util.HttpUrlUtil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_qiubai);

        ((FunnyLeagueApplication)FunnyLeagueApplication.getContext()).addActivity(this);
        ButterKnife.bind(this);

        if(Build.VERSION.SDK_INT>=21){
            getSupportActionBar().setElevation(0);
        }

        initData();
    }

    @Override
    public void initData() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setCustomView(R.layout.actionbar_content);//自定义ActionBar布局
//        ImageButton imageButton = (ImageButton)actionBar.getCustomView().findViewById(R.id.contentBack);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        Intent intent = this.getIntent();
        qiuBaiItemBean = (QiuBaiItemBean)intent.getSerializableExtra("qiuBaiItemBean");

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
            if(elementsUserStatis != null && elementsUserStatis.size() == 3){
                Elements userStatis = elementsUserStatis.get(0).select("ul").select("li");
                qiuBaiUserBean.setFansCount(userStatis.get(0).ownText());
                qiuBaiUserBean.setFollowCount(userStatis.get(1).ownText());
                qiuBaiUserBean.setArticlesCount(userStatis.get(2).ownText());
                qiuBaiUserBean.setCommentCount(userStatis.get(3).ownText());
                qiuBaiUserBean.setSmileCount(userStatis.get(4).ownText());
                qiuBaiUserBean.setBestCount(userStatis.get(5).ownText());

                userStatis = elementsUserStatis.get(1).select("ul").select("li");
                qiuBaiUserBean.setMarriage(userStatis.get(0).ownText());
                qiuBaiUserBean.setConstellation(userStatis.get(1).ownText());
                qiuBaiUserBean.setOccupation(userStatis.get(2).ownText());
                qiuBaiUserBean.setHometown(userStatis.get(3).ownText());
                qiuBaiUserBean.setQiuAge(userStatis.get(4).ownText());

                Elements userQius = doc.select(".user-block");
                ArrayList<String> stringList = new ArrayList<String>();

                for(int i = 0; i < userQius.size();i++){
                    String text = userQius.get(i).select(".user-article-text").text();
                    String pic = userQius.get(i).select(".user-article-pic").select("img").attr("src");
                    if(pic == null || "".equals(pic)){
                        stringList.add(text);
                    }
                    if(stringList.size() == 3){
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
            userName.setText(qiuBaiUserBean.getUserName());

            if (!qiuBaiUserBean.getUserImage().contains("qiushibaike")) {
                qiuBaiUserBean.setUserImage(HttpUrlUtil.QIU_BAI_DEFAULT_USER_IMAGE);
            }
            Glide.with(QiuBaiUserActivity.this).load(qiuBaiUserBean.getUserImage()).transform(new GlideCircleTransform(QiuBaiUserActivity.this, 50)).into(userImage);
            if (qiuBaiUserBean.getUserSex() != null && !"".equals(qiuBaiUserBean.getUserSex())
                    && qiuBaiUserBean.getUserAge() != null && !"".equals(qiuBaiUserBean.getUserAge())) {
                userSex.setBackgroundResource("man".equals(qiuBaiUserBean.getUserSex()) ? R.drawable.man : R.drawable.women);
                userAge.setText(qiuBaiUserBean.getUserAge());
                userSex.setVisibility(View.VISIBLE);
            }else{
                userSex.setVisibility(View.GONE);
            }
        }
    };
}
