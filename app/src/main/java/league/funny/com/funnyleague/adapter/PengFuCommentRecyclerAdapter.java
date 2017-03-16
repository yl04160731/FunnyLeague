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
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.FunnyLeagueApplication;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.activity.QiuBaiUserActivity;
import league.funny.com.funnyleague.bean.CommentBean;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.util.GlideCircleTransform;
import league.funny.com.funnyleague.util.HttpUrlUtil;

public class PengFuCommentRecyclerAdapter extends Adapter<ViewHolder> {

    private Context context;
    private ArrayList<CommentBean> commentBeanList;


    public PengFuCommentRecyclerAdapter(Context context, ArrayList<CommentBean> commentBeanList) {
        this.context = context;
        this.commentBeanList = commentBeanList;
    }

    @Override
    public int getItemCount() {
        return commentBeanList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_list_item_pengfu, parent,
                false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            CommentBean commentBean = commentBeanList.get(position);

            ((ItemViewHolder) holder).userName.setText(commentBean.getUserName());

            Glide.with(FunnyLeagueApplication.getApplication()).load(commentBean.getUserImage()).transform(new GlideCircleTransform(FunnyLeagueApplication.getApplication(), 40)).into(((ItemViewHolder) holder).userImage);
            ((ItemViewHolder) holder).goodCount.setText(commentBean.getFloor());

            ((ItemViewHolder) holder).commentContent.setText(commentBean.getCommentContent());

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
        if(commentBeanList.get(position).getUserUrl() == null || "".equals(commentBeanList.get(position).getUserUrl().replace(HttpUrlUtil.QIU_BAI_HOME,""))){
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, QiuBaiUserActivity.class);
        Bundle bundle = new Bundle();
        ItemBean itemBean = new ItemBean();
        CommentBean iuBaiCommentBean = commentBeanList.get(position);
        itemBean.setUserAge(iuBaiCommentBean.getUserAge());
        itemBean.setUserImage(iuBaiCommentBean.getUserImage());
        itemBean.setUserId(iuBaiCommentBean.getUserId());
        itemBean.setUserSex(iuBaiCommentBean.getUserSex());
        itemBean.setUserName(iuBaiCommentBean.getUserName());
        itemBean.setUserUrl(iuBaiCommentBean.getUserUrl());
        bundle.putSerializable("itemBean", itemBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static class ItemViewHolder extends ViewHolder {

        @BindView(R.id.userName_pengfu)
        public TextView userName;

        @BindView(R.id.userImage_pengfu)
        public ImageView userImage;

        @BindView(R.id.goodCount)
        public TextView goodCount;

        @BindView(R.id.commentContent)
        public TextView commentContent;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}