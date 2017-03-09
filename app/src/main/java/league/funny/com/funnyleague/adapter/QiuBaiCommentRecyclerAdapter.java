package league.funny.com.funnyleague.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.FunnyLeagueApplication;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.activity.QiuBaiUserActivity;
import league.funny.com.funnyleague.bean.QiuBaiCommentBean;
import league.funny.com.funnyleague.bean.QiuBaiItemBean;
import league.funny.com.funnyleague.util.GlideCircleTransform;
import league.funny.com.funnyleague.util.HttpUrlUtil;

public class QiuBaiCommentRecyclerAdapter extends Adapter<ViewHolder> {

    private Context context;
    private ArrayList<QiuBaiCommentBean> qiuBaiCommentBeanList;


    public QiuBaiCommentRecyclerAdapter(Context context, ArrayList<QiuBaiCommentBean> qiuBaiCommentBeanList) {
        this.context = context;
        this.qiuBaiCommentBeanList = qiuBaiCommentBeanList;
    }

    @Override
    public int getItemCount() {
        return qiuBaiCommentBeanList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_list_item_qiubai, parent,
                false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            QiuBaiCommentBean qiuBaiCommentBean = qiuBaiCommentBeanList.get(position);

            ((ItemViewHolder) holder).userName.setText(qiuBaiCommentBean.getUserName());

            if (!qiuBaiCommentBean.getUserImage().contains("qiushibaike")) {
                qiuBaiCommentBean.setUserImage(HttpUrlUtil.QIU_BAI_DEFAULT_USER_IMAGE);
            }

            Glide.with(FunnyLeagueApplication.getApplication()).load(qiuBaiCommentBean.getUserImage()).transform(new GlideCircleTransform(FunnyLeagueApplication.getApplication(), 40)).into(((ItemViewHolder) holder).userImage);
            if (qiuBaiCommentBean.getUserSex() != null && !"".equals(qiuBaiCommentBean.getUserSex())
                    && qiuBaiCommentBean.getUserAge() != null && !"".equals(qiuBaiCommentBean.getUserAge())) {
                ((ItemViewHolder) holder).userSex.setBackgroundResource("man".equals(qiuBaiCommentBean.getUserSex()) ? R.drawable.man : R.drawable.women);
                ((ItemViewHolder) holder).userAge.setText(qiuBaiCommentBean.getUserAge());
                ((ItemViewHolder) holder).userSex.setVisibility(View.VISIBLE);
            }else{
                ((ItemViewHolder) holder).userSex.setVisibility(View.GONE);
            }

            if(qiuBaiCommentBean.getGoodCount() == null || "".equals(qiuBaiCommentBean.getGoodCount())){
                ((ItemViewHolder) holder).goodImage.setVisibility(View.GONE);
                ((ItemViewHolder) holder).goodCount.setText(qiuBaiCommentBean.getFloor());
            }else{
                ((ItemViewHolder) holder).goodImage.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).goodCount.setText(qiuBaiCommentBean.getGoodCount());
            }

            ((ItemViewHolder) holder).commentContent.setText(qiuBaiCommentBean.getCommentContent());

            ((ItemViewHolder) holder).userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toQiuBaiUserActivity(position);
                }
            });
            ((ItemViewHolder) holder).userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toQiuBaiUserActivity(position);
                }
            });
        }
    }

    public void toQiuBaiUserActivity(int position){
        if(qiuBaiCommentBeanList.get(position).getUserUrl() == null || "".equals(qiuBaiCommentBeanList.get(position).getUserUrl().replace(HttpUrlUtil.QIU_BAI_HOME,""))){
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, QiuBaiUserActivity.class);
        Bundle bundle = new Bundle();
        QiuBaiItemBean qiuBaiItemBean = new QiuBaiItemBean();
        QiuBaiCommentBean iuBaiCommentBean = qiuBaiCommentBeanList.get(position);
        qiuBaiItemBean.setUserAge(iuBaiCommentBean.getUserAge());
        qiuBaiItemBean.setUserImage(iuBaiCommentBean.getUserImage());
        qiuBaiItemBean.setUserId(iuBaiCommentBean.getUserId());
        qiuBaiItemBean.setUserSex(iuBaiCommentBean.getUserSex());
        qiuBaiItemBean.setUserName(iuBaiCommentBean.getUserName());
        qiuBaiItemBean.setUserUrl(iuBaiCommentBean.getUserUrl());
        bundle.putSerializable("qiuBaiItemBean",qiuBaiItemBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static class ItemViewHolder extends ViewHolder {

        @BindView(R.id.userName_qiubai)
        public TextView userName;

        @BindView(R.id.userImage_qiubai)
        public ImageView userImage;

        @BindView(R.id.userSex_qiubai)
        public LinearLayout userSex;

        @BindView(R.id.userAge_qiubai)
        public TextView userAge;

        @BindView(R.id.goodCount)
        public TextView goodCount;

        @BindView(R.id.commentContent)
        public TextView commentContent;

        @BindView(R.id.goodImage)
        public ImageView goodImage;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}