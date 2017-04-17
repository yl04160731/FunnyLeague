package league.funny.com.funnyleague.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.util.HttpUrlUtil;
import league.funny.com.funnyleague.util.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZOLYouQuContentFragment extends Fragment {

    @BindView(R.id.itemContent_zol_youqu)
    TextView itemContent;

    @BindView(R.id.itemTitle_zol_youqu)
    TextView itemTitle;

    @BindView(R.id.content_wait)
    RelativeLayout ContentWait;

    @BindView(R.id.content_layout)
    RelativeLayout contentLayout;

    private String url = null;

    private ItemBean itemBean;

    private View view = null;

    public void setItemBean(ItemBean itemBean) {
        this.itemBean = itemBean;
    }

    public ZOLYouQuContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_content_zol_youqu, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    public void initData() {

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
            ContentWait.setVisibility(View.GONE);
            contentLayout.setVisibility(View.VISIBLE);
        }
    };

    private void getData() {
        url = itemBean.getItemContentUrl();
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
                    String content = elementsText.html();
                    content = content.substring(content.indexOf("</div>") + 6);
                    itemBean.setItemContent(Util.replaceHtmlSign(content));
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
