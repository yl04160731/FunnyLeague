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
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.bean.QiuBaiCommentBean;
import league.funny.com.funnyleague.util.GlideCircleTransform;
import league.funny.com.funnyleague.util.HttpUrlUtil;

/**
 * Created by inno-y on 2017/2/28.
 */

public class QiuBaiCommentListAdapter extends BaseAdapter {

    private Context context;

    private List<QiuBaiCommentBean> qiuBaiCommentBeanList;

    private LayoutInflater inflater;

    public QiuBaiCommentListAdapter(Context context, List<QiuBaiCommentBean> qiuBaiCommentBeanList) {
        super();
        this.context = context;
        this.qiuBaiCommentBeanList = qiuBaiCommentBeanList;
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

        return qiuBaiCommentBeanList.get(position);
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

        QiuBaiCommentBean qiuBaiCommentBean = qiuBaiCommentBeanList.get(position);

        holder.userName.setText(qiuBaiCommentBean.getUserName());

        if (!qiuBaiCommentBean.getUserImage().contains("qiushibaike")) {
            qiuBaiCommentBean.setUserImage(HttpUrlUtil.QIU_BAI_DEFAULT_USER_IMAGE);
        }

        Glide.with(context).load(qiuBaiCommentBean.getUserImage()).transform(new GlideCircleTransform(context, 40)).into(holder.userImage);
        if (qiuBaiCommentBean.getUserSex() != null && !"".equals(qiuBaiCommentBean.getUserSex())
                && qiuBaiCommentBean.getUserAge() != null && !"".equals(qiuBaiCommentBean.getUserAge())) {
            holder.userSex.setBackgroundResource("man".equals(qiuBaiCommentBean.getUserSex()) ? R.drawable.man : R.drawable.women);
            holder.userAge.setText(qiuBaiCommentBean.getUserAge());
            holder.userSex.setVisibility(View.VISIBLE);
        }else{
            holder.userSex.setVisibility(View.GONE);
        }

        if(qiuBaiCommentBean.getGoodCount() == null || "".equals(qiuBaiCommentBean.getGoodCount())){
            holder.goodImage.setVisibility(View.GONE);
            holder.goodCount.setText(qiuBaiCommentBean.getFloor());
        }else{
            holder.goodImage.setVisibility(View.VISIBLE);
            holder.goodCount.setText(qiuBaiCommentBean.getGoodCount());
        }

        holder.commentContent.setText(qiuBaiCommentBean.getCommentContent());

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
        return qiuBaiCommentBeanList.size();
    }
}
