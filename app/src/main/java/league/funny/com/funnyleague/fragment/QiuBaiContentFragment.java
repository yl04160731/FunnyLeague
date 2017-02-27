package league.funny.com.funnyleague.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.bean.QiuBaiCommentBean;
import league.funny.com.funnyleague.util.HttpUrlUtil;
import league.funny.com.funnyleague.util.NetworkUtil;
import league.funny.com.funnyleague.util.Util;

import static league.funny.com.funnyleague.R.id.loading_layout_qiubai;

/**
 * A simple {@link Fragment} subclass.
 */
public class QiuBaiContentFragment extends BaseFragment {

    private View view = null;
    private String htmlUrl;
    private String content;
    private String nextLink;
    @BindView(R.id.content_qiubai)
    TextView contentTextView;

    @BindView(loading_layout_qiubai)
    RelativeLayout relativeLayout_loading;

    @BindView(R.id.loading_qiubai)
    ImageView loadingImageView;

    @BindView(R.id.nowifi_layout_qiubai)
    RelativeLayout relativeLayout_nowifi;

    public QiuBaiContentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_content_qiubai, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    public void initData(){

        if(!NetworkUtil.isNetworkAvailable(getActivity())){
            relativeLayout_nowifi.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(),getResources().getText(R.string.no_wifi_warn),Toast.LENGTH_SHORT).show();
            return;
        }

        relativeLayout_nowifi.setVisibility(View.GONE);
        relativeLayout_loading.setVisibility(View.VISIBLE);

        Glide.with(getActivity()).load(R.drawable.loading).into(loadingImageView);

        Bundle bundle = getArguments();
        htmlUrl = bundle.getString("htmlUrl");

        Runnable networkTask = new Runnable() {
            @Override
            public void run() {
                getData();
            }
        };

        new Thread(networkTask).start();
    }

    @OnClick(R.id.content_qiubai)
    public void onClickNext(){
        Bundle bundle=new Bundle();
        bundle.putString("htmlUrl", nextLink);
        QiuBaiContentFragment fragment = new QiuBaiContentFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_qiubai, fragment).commit();
    }

    @OnClick(R.id.nowifi_layout_qiubai)
    public void onClickReConnect(){
        initData();
    }

    private void getData() {
        try {
            Document doc = Jsoup.connect(htmlUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 5.1; zh-CN) AppleWebKit/535.12 (KHTML, like Gecko) Chrome/22.0.1229.79 Safari/535.12")
                    .timeout(6000).get();

            if(doc.getElementById("articleNextLink").attr("value") != null &&
                    !"".equals(doc.getElementById("articleNextLink").attr("value")) &&
                    !"/".equals(doc.getElementById("articleNextLink").attr("value"))){
                nextLink = HttpUrlUtil.QIU_BAI_HOME + doc.getElementById("articleNextLink").attr("value");
            }else{
                nextLink = HttpUrlUtil.QIU_BAI_HOME + doc.select(".recommend").select("a").attr("href");
            }

            content = Util.replaceHtmlSign(doc.select(".content").html());
            String smileCount = doc.select(".stats-vote").select(".number").html();
            String commentCount = doc.select(".stats-comments").select(".number").html();

            ArrayList<QiuBaiCommentBean> shenCommentList = new ArrayList<>();
            ArrayList<QiuBaiCommentBean> CommentList = new ArrayList<>();

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

            if(content != null && !"".equals(content)){
                contentTextView.setText(content + "****" + nextLink);
                relativeLayout_loading.setVisibility(View.GONE);
            }else{
                relativeLayout_nowifi.setVisibility(View.VISIBLE);
            }
        }
    };
}
