package league.funny.com.funnyleague.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import league.funny.com.funnyleague.bean.CommentBean;
import league.funny.com.funnyleague.util.GlideCircleTransform;
import league.funny.com.funnyleague.view.RecycleViewDivider;

public class FanJianCommentRecyclerAdapter extends Adapter<ViewHolder> {

    private Context context;
    private ArrayList<CommentBean> commentBeanList;


    public FanJianCommentRecyclerAdapter(Context context, ArrayList<CommentBean> commentBeanList) {
        this.context = context;
        this.commentBeanList = commentBeanList;
    }

    @Override
    public int getItemCount() {
        return commentBeanList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_list_item_fanjian, parent,
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
            FanJianCommentSubRecyclerAdapter fanjianCommentSubRecyclerAdapter = new FanJianCommentSubRecyclerAdapter(context, commentBean.getCommentbeanList());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            ((ItemViewHolder) holder).putong_comment_sub_recyclerView.addItemDecoration(new RecycleViewDivider(context, RecycleViewDivider.VERTICAL_LIST, R.drawable.small_divider));
            ((ItemViewHolder) holder).putong_comment_sub_recyclerView.setLayoutManager(linearLayoutManager);
            ((ItemViewHolder) holder).putong_comment_sub_recyclerView.setAdapter(fanjianCommentSubRecyclerAdapter);
        }
    }

    public static class ItemViewHolder extends ViewHolder {

        @BindView(R.id.userName_fanjian)
        public TextView userName;

        @BindView(R.id.userImage_fanjian)
        public ImageView userImage;

        @BindView(R.id.goodCount)
        public TextView goodCount;

        @BindView(R.id.commentContent)
        public TextView commentContent;

        @BindView(R.id.putong_comment_sub_recyclerView)
        public RecyclerView putong_comment_sub_recyclerView;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}