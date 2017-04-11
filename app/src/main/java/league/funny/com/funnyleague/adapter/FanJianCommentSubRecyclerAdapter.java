package league.funny.com.funnyleague.adapter;

import android.content.Context;
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

public class FanJianCommentSubRecyclerAdapter extends Adapter<ViewHolder> {

    private Context context;
    private ArrayList<CommentBean> commentBeanList;


    public FanJianCommentSubRecyclerAdapter(Context context, ArrayList<CommentBean> commentBeanList) {
        this.context = context;
        this.commentBeanList = commentBeanList;
    }

    @Override
    public int getItemCount() {
        return commentBeanList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_list_item_sub_fanjian, parent,
                false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            CommentBean commentBean = commentBeanList.get(position);

            ((ItemViewHolder) holder).userName.setText(commentBean.getUserName());

            Glide.with(FunnyLeagueApplication.getApplication()).load(commentBean.getUserImage()).transform(new GlideCircleTransform(FunnyLeagueApplication.getApplication(), 30)).into(((ItemViewHolder) holder).userImage);
            ((ItemViewHolder) holder).commentContent.setText(commentBean.getCommentContent());

            if(commentBean.getReplyUser() == null || "".equals(commentBean.getReplyUser())){
                ((ItemViewHolder) holder).replyUser.setVisibility(View.GONE);
            }else{
                ((ItemViewHolder) holder).replyUser.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).replyUser.setText("  回复  " + commentBean.getReplyUser());
            }
        }
    }

    public static class ItemViewHolder extends ViewHolder {

        @BindView(R.id.userName_fanjian)
        public TextView userName;

        @BindView(R.id.userImage_fanjian)
        public ImageView userImage;

        @BindView(R.id.commentContent)
        public TextView commentContent;

        @BindView(R.id.replyUser)
        public TextView replyUser;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}