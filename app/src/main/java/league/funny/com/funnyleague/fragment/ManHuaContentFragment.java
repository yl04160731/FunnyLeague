package league.funny.com.funnyleague.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.FunnyLeagueApplication;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.util.HttpUrlUtil;
import league.funny.com.funnyleague.util.Util;

import static com.ashokvarma.bottomnavigation.utils.Utils.getScreenWidth;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManHuaContentFragment extends Fragment {

//    @BindView(R.id.itemContent_manhua)
//    ImageView itemContent;
    @BindView(R.id.viewGroup)
    LinearLayout viewGroup;

    @BindView(R.id.itemTitle_manhua)
    TextView itemTitle;

    @BindView(R.id.content_wait)
    RelativeLayout ContentWait;

    @BindView(R.id.content_layout)
    RelativeLayout contentLayout;

    private String url = null;

    private ItemBean itemBean;

    private View view = null;

    private String[] imageViewString;

    public void setItemBean(ItemBean itemBean) {
        this.itemBean = itemBean;
    }

    public ManHuaContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_content_manhua, container, false);
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

            itemTitle.setText(itemBean.getItemContentTitle());

            for (int i = 0; i < imageViewString.length; i++) {
                ImageView imageView = new ImageView(getActivity());
                int screenWidth = getScreenWidth(FunnyLeagueApplication.getApplication());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(screenWidth * 11 / 12,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                imageView.setMaxWidth(screenWidth);
                imageView.setPadding(Util.dp2px(getActivity(),4),
                        Util.dp2px(getActivity(),4),Util.dp2px(getActivity(),4),
                        Util.dp2px(getActivity(),4));
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                Glide.with(FunnyLeagueApplication.getApplication()).load(imageViewString[i])
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .dontAnimate().error(R.drawable.imageload).placeholder(R.drawable.imageload)
                        .into(imageView);
                viewGroup.addView(imageView);
            }

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

            Elements elementsArticle = doc.select(".info");
            Elements elementsText = doc.select("td");
            if (elementsArticle != null && elementsArticle.size() > 0) {
                itemBean.setItemContentTitle(Util.replaceHtmlSign(elementsArticle.select("h1").text()));
                Elements elementsImage = elementsText.select("img");
                imageViewString = new String[elementsImage.size()];
                for(int i = 0; i < elementsImage.size();i++){
                    imageViewString[i] = elementsImage.get(i).attr("src");
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
