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
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.FunnyLeagueApplication;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.activity.QiuBaiContentActivity;
import league.funny.com.funnyleague.bean.QiuBaiItemBean;
import league.funny.com.funnyleague.util.GlideCircleTransform;
import league.funny.com.funnyleague.util.HttpUrlUtil;

import static com.ashokvarma.bottomnavigation.utils.Utils.getScreenWidth;

public class QiuBaiUserTextImageRecyclerAdapter extends Adapter<ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context context;
    private ArrayList<QiuBaiItemBean> qiuBaiItemBeanArrayList;


    public QiuBaiUserTextImageRecyclerAdapter(Context context, ArrayList<QiuBaiItemBean> qiuBaiItemBeanArrayList) {
        this.context = context;
        this.qiuBaiItemBeanArrayList = qiuBaiItemBeanArrayList;
    }

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            final QiuBaiItemBean qiuBaiItemBean = qiuBaiItemBeanArrayList.get(position);

            ((ItemViewHolder) holder).userName.setText(qiuBaiItemBean.getUserName());
            if (!qiuBaiItemBean.getUserImage().contains("qiushibaike")) {
                qiuBaiItemBean.setUserImage(HttpUrlUtil.QIU_BAI_DEFAULT_USER_IMAGE);
            }
            Glide.with(FunnyLeagueApplication.getApplication()).load(qiuBaiItemBean.getUserImage()).transform(new GlideCircleTransform(FunnyLeagueApplication.getApplication(), 40)).into(((ItemViewHolder) holder).userImage);
            ((ItemViewHolder) holder).itemContent.setText(qiuBaiItemBean.getItemContent() == null?"":qiuBaiItemBean.getItemContent());
            if (qiuBaiItemBean.getItemImage() != null && !"".equals(qiuBaiItemBean.getItemImage())) {
                ((ItemViewHolder) holder).itemImage_qiubai.setVisibility(View.VISIBLE);
//                ViewGroup.LayoutParams vglp = ((ItemViewHolder) holder).itemImage_qiubai.getLayoutParams();
//                vglp.width = 10*position;
//                vglp.height = 10*position;
//                vglp.
//                ((ItemViewHolder) holder).itemImage_qiubai.setLayoutParams(vglp);

//                Glide.with(FunnyLeagueApplication.getApplication())
//                        .load(qiuBaiItemBean.getItemImage()).diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .placeholder(R.drawable.imageload)
//                        .error(R.drawable.imageload)
//                        .dontAnimate()
//                        .centerCrop()
//                        .into(new SimpleTarget<GlideDrawable>() {
//                            @Override
//                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                                ViewGroup.LayoutParams params = ((ItemViewHolder) holder).itemImage_qiubai.getLayoutParams();
//                                int screenWidth = getScreenWidth(FunnyLeagueApplication.getApplication());
//                                params.width = screenWidth;
//                                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                                ((ItemViewHolder) holder).itemImage_qiubai.setLayoutParams(params);
//                                ((ItemViewHolder) holder).itemImage_qiubai.setMaxWidth(screenWidth);
//                                ((ItemViewHolder) holder).itemImage_qiubai.setImageDrawable(resource);
//                            }
//                        });

                ViewGroup.LayoutParams params = ((ItemViewHolder) holder).itemImage_qiubai.getLayoutParams();
                int screenWidth = getScreenWidth(FunnyLeagueApplication.getApplication());
                params.width = screenWidth;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                ((ItemViewHolder) holder).itemImage_qiubai.setLayoutParams(params);
                ((ItemViewHolder) holder).itemImage_qiubai.setMaxWidth(screenWidth);

                Glide.with(FunnyLeagueApplication.getApplication()).load(qiuBaiItemBean.getItemImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate().error(R.drawable.imageload).placeholder(R.drawable.imageload)
                        .into(((ItemViewHolder) holder).itemImage_qiubai);

//                Glide.with(FunnyLeagueApplication.getApplication()).load(qiuBaiItemBean.getItemImage())
//                        .placeholder(R.drawable.imageload).into(((ItemViewHolder) holder).itemImage_qiubai);
            } else {
                ((ItemViewHolder) holder).itemImage_qiubai.setVisibility(View.GONE);
            }

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

            ((ItemViewHolder) holder).itemContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toQiuBaiContentActivity(position);
                }
            });


        }
    }

    public void toQiuBaiContentActivity(int position){
        Intent intent = new Intent();
        intent.setClass(context, QiuBaiContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("qiuBaiItemBeanArrayList", qiuBaiItemBeanArrayList);
        bundle.putInt("qiubaiContentIndex",position);
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

        @BindView(R.id.itemImage_qiubai)
        ImageView itemImage_qiubai;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public static class FootViewHolder extends ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }
}