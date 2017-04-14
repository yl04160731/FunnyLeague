package league.funny.com.funnyleague.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.FunnyLeagueApplication;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.util.HttpUrlUtil;
import league.funny.com.funnyleague.util.Util;

public class ZOLYouQuContentActivity extends BaseActivity {

    @BindView(R.id.itemContent_zol_youqu)
    TextView itemContent;

    @BindView(R.id.itemTitle_zol_youqu)
    TextView itemTitle;

    private ItemBean itemBean = new ItemBean();
    private String url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_zol_youqu);
        ButterKnife.bind(this);
        ((FunnyLeagueApplication) FunnyLeagueApplication.getContext()).addActivity(this);
        initData();
        if (Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(0);
        }
    }

    public void initData() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_content);//自定义ActionBar布局
        ImageButton imageButton = (ImageButton) actionBar.getCustomView().findViewById(R.id.contentBack);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = this.getIntent();
        url = intent.getStringExtra("ZOLYouQuUrl");
        Runnable networkTask = new Runnable() {
            @Override
            public void run() {
                getData();
            }
        };

        new Thread(networkTask).start();
    }

    Handler HtmlHandler = new Handler() {
        public void handleMessage(Message msg) {
            itemContent.setText(itemBean.getItemContent());
            itemTitle.setText(itemBean.getItemContentTitle());
            TextPaint tp = itemTitle.getPaint();
            tp.setFakeBoldText(true);
        }
    };

    private void getData() {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(HttpUrlUtil.USER_AGENT)
                    .timeout(HttpUrlUtil.TIMEOUT).get();

            if (url.contains(HttpUrlUtil.ZOL_HOME)) {
                Elements elementsArticle = doc.select(".article-title");
                Elements elementsText = doc.select(".article-text");
                if (elementsArticle != null && elementsArticle.size() > 0) {
                    itemBean.setItemContentTitle(Util.replaceHtmlSign(elementsArticle.text()));
                    itemBean.setItemContent(Util.replaceHtmlSign(elementsText.html()));
                }
            } else {
                Elements elementsArticle = doc.select(".joketitle");
                Elements elementsText = doc.select("td");
                if (elementsArticle != null && elementsArticle.size() > 0) {
                    itemBean.setItemContentTitle(Util.replaceHtmlSign(elementsArticle.text()));
                    itemBean.setItemContent(Util.replaceHtmlSign(elementsText.text()));
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
