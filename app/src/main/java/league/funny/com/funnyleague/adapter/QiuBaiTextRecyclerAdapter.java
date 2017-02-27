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
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.activity.QiuBaiContentActivity;
import league.funny.com.funnyleague.bean.QiuBaiItemBean;
import league.funny.com.funnyleague.util.GlideCircleTransform;
import league.funny.com.funnyleague.util.HttpUrlUtil;

public class QiuBaiTextRecyclerAdapter extends Adapter<ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context context;
    private ArrayList<QiuBaiItemBean> qiuBaiItemBeanArrayList;


    public QiuBaiTextRecyclerAdapter(Context context, ArrayList<QiuBaiItemBean> qiuBaiItemBeanArrayList) {
        this.context = context;
        this.qiuBaiItemBeanArrayList = qiuBaiItemBeanArrayList;
    }

//    public interface OnItemClickListener {
//        void onItemClick(View view, int position);
//
//        void onItemLongClick(View view, int position);
//    }
//
//    private OnItemClickListener onItemClickListener;
//
//    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }

    @Override
    public int getItemCount() {
        return qiuBaiItemBeanArrayList.size() == 0 ? 0 : qiuBaiItemBeanArrayList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_text_item_qiubai, parent,
                    false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_foot, parent,
                    false);
            return new FootViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final QiuBaiItemBean qiuBaiItemBean = qiuBaiItemBeanArrayList.get(position);
            ((ItemViewHolder) holder).userName.setText(qiuBaiItemBean.getUserName());

            if (!qiuBaiItemBean.getUserImage().contains("qiushibaike")) {
                qiuBaiItemBean.setUserImage(HttpUrlUtil.QIU_BAI_DEFAULT_USER_IMAGE);
            }
            Glide.with(context).load(qiuBaiItemBean.getUserImage()).transform(new GlideCircleTransform(context, 30)).into(((ItemViewHolder) holder).userImage);
            ((ItemViewHolder) holder).itemContent.setText(qiuBaiItemBean.getItemContent());
            if (qiuBaiItemBean.getUserSex() != null && !"".equals(qiuBaiItemBean.getUserSex())
                    && qiuBaiItemBean.getUserAge() != null && !"".equals(qiuBaiItemBean.getUserAge())) {
                ((ItemViewHolder) holder).userSex.setBackgroundResource("man".equals(qiuBaiItemBean.getUserSex()) ? R.drawable.man : R.drawable.women);
                ((ItemViewHolder) holder).userAge.setText(qiuBaiItemBean.getUserAge());
                ((ItemViewHolder) holder).userSex.setVisibility(View.VISIBLE);
            }else{
                ((ItemViewHolder) holder).userSex.setVisibility(View.GONE);
            }
            ((ItemViewHolder) holder).smileCount.setText(qiuBaiItemBean.getSmileCount());
            ((ItemViewHolder) holder).commentCount.setText(qiuBaiItemBean.getCommentCount());
            ((ItemViewHolder) holder).commentGood.setText(qiuBaiItemBean.getCommentGoodName() + qiuBaiItemBean.getCommentGoodContent());

            if (qiuBaiItemBean.getCommentGoodName() == null || "".equals(qiuBaiItemBean.getCommentGoodName())) {
                ((ItemViewHolder) holder).commentLayout.setVisibility(View.GONE);
            }

            ((ItemViewHolder) holder).userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, qiuBaiItemBean.getUserUrl(), Toast.LENGTH_SHORT).show();
                }
            });

            ((ItemViewHolder) holder).userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, qiuBaiItemBean.getUserUrl(), Toast.LENGTH_SHORT).show();
                }
            });

            ((ItemViewHolder) holder).itemContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toQiuBaiContentActivity(qiuBaiItemBean.getItemContentUrl());
                }
            });

            ((ItemViewHolder) holder).commentGood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toQiuBaiContentActivity(qiuBaiItemBean.getItemContentUrl());
                }
            });
        }
    }

    public void toQiuBaiContentActivity(String url){
        Intent intent = new Intent();
        intent.setClass(context, QiuBaiContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("htmlUrl", url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static class ItemViewHolder extends ViewHolder {

        @BindView(R.id.userName_qiubai)
        TextView userName;

        @BindView(R.id.itemContent_qiubai)
        TextView itemContent;

        @BindView(R.id.userImage_qiubai)
        ImageView userImage;

        @BindView(R.id.userSex_qiubai)
        LinearLayout userSex;

        @BindView(R.id.userAge_qiubai)
        TextView userAge;

        @BindView(R.id.smileCount_qiubai)
        TextView smileCount;

        @BindView(R.id.commentCount_qiubai)
        TextView commentCount;

        @BindView(R.id.commentGood_qiubai)
        TextView commentGood;

        @BindView(R.id.commentLayout_qiubai)
        LinearLayout commentLayout;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
//            tv = (TextView) view.findViewById(R.id.item_title);
        }
    }

    public static class FootViewHolder extends ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }
}