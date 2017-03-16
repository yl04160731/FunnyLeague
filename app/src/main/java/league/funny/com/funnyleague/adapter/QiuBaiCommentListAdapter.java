package league.funny.com.funnyleague.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.FunnyLeagueApplication;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.bean.CommentBean;
import league.funny.com.funnyleague.util.GlideCircleTransform;
import league.funny.com.funnyleague.util.HttpUrlUtil;

/**
 * Created by inno-y on 2017/2/28.
 */

public class QiuBaiCommentListAdapter extends BaseAdapter {

    private Context context;

    private List<CommentBean> commentBeanList;

    private LayoutInflater inflater;

    public QiuBaiCommentListAdapter(Context context, List<CommentBean> commentBeanList) {
        super();
        this.context = context;
        this.commentBeanList = commentBeanList;
        inflater = LayoutInflater.from(context);

    }

    class ViewHolder {
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

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);//注解绑定View
        }
    }

    @Override
    public Object getItem(int position) {

        return commentBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.comment_list_item_qiubai, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CommentBean commentBean = commentBeanList.get(position);

        holder.userName.setText(commentBean.getUserName());

        if (!commentBean.getUserImage().contains("qiushibaike")) {
            commentBean.setUserImage(HttpUrlUtil.QIU_BAI_DEFAULT_USER_IMAGE);
        }

        Glide.with(FunnyLeagueApplication.getApplication()).load(commentBean.getUserImage()).transform(new GlideCircleTransform(FunnyLeagueApplication.getApplication(), 40)).into(holder.userImage);
        if (commentBean.getUserSex() != null && !"".equals(commentBean.getUserSex())
                && commentBean.getUserAge() != null && !"".equals(commentBean.getUserAge())) {
            holder.userSex.setBackgroundResource("man".equals(commentBean.getUserSex()) ? R.drawable.man : R.drawable.women);
            holder.userAge.setText(commentBean.getUserAge());
            holder.userSex.setVisibility(View.VISIBLE);
        }else{
            holder.userSex.setVisibility(View.GONE);
        }

        if(commentBean.getGoodCount() == null || "".equals(commentBean.getGoodCount())){
            holder.goodImage.setVisibility(View.GONE);
            holder.goodCount.setText(commentBean.getFloor());
        }else{
            holder.goodImage.setVisibility(View.VISIBLE);
            holder.goodCount.setText(commentBean.getGoodCount());
        }

        holder.commentContent.setText(commentBean.getCommentContent());



//        holder.arrowPicture.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//            }
//        });

        return convertView;
    }

    @Override
    public int getCount() {
        return commentBeanList.size();
    }
}
