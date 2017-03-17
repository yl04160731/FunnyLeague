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
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.util.GlideCircleTransform;
import league.funny.com.funnyleague.util.HttpUrlUtil;

import static com.ashokvarma.bottomnavigation.utils.Utils.getScreenWidth;

public class QiuBaiUserTextImageRecyclerAdapter extends Adapter<ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context context;
    private ArrayList<ItemBean> itemBeanArrayList;


    public QiuBaiUserTextImageRecyclerAdapter(Context context, ArrayList<ItemBean> itemBeanArrayList) {
        this.context = context;
        this.itemBeanArrayList = itemBeanArrayList;
    }

    @Override
    public int getItemCount() {
        return itemBeanArrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_text_item_qiubai, parent,
                false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemBean itemBean = itemBeanArrayList.get(position);

        ((ItemViewHolder) holder).userName.setText(itemBean.getUserName());
        if (!itemBean.getUserImage().contains("qiushibaike")) {
            itemBean.setUserImage(HttpUrlUtil.QIU_BAI_DEFAULT_USER_IMAGE);
        }
        Glide.with(FunnyLeagueApplication.getApplication()).load(itemBean.getUserImage()).transform(new GlideCircleTransform(FunnyLeagueApplication.getApplication(), 40)).into(((ItemViewHolder) holder).userImage);
        ((ItemViewHolder) holder).itemContent.setText(itemBean.getItemContent() == null ? "" : itemBean.getItemContent());
        if (itemBean.getItemImage() != null && !"".equals(itemBean.getItemImage())) {
            ((ItemViewHolder) holder).itemImage_qiubai.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = ((ItemViewHolder) holder).itemImage_qiubai.getLayoutParams();
            int screenWidth = getScreenWidth(FunnyLeagueApplication.getApplication());
            params.width = screenWidth;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            ((ItemViewHolder) holder).itemImage_qiubai.setLayoutParams(params);
            ((ItemViewHolder) holder).itemImage_qiubai.setMaxWidth(screenWidth);

            Glide.with(FunnyLeagueApplication.getApplication()).load(itemBean.getItemImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate().error(R.drawable.imageload).placeholder(R.drawable.imageload)
                    .into(((ItemViewHolder) holder).itemImage_qiubai);

        } else {
            ((ItemViewHolder) holder).itemImage_qiubai.setVisibility(View.GONE);
        }

        if (itemBean.getUserSex() != null && !"".equals(itemBean.getUserSex())
                && itemBean.getUserAge() != null && !"".equals(itemBean.getUserAge())) {
            ((ItemViewHolder) holder).userSex.setBackgroundResource("man".equals(itemBean.getUserSex()) ? R.drawable.man : R.drawable.women);
            ((ItemViewHolder) holder).userAge.setText(itemBean.getUserAge());
            ((ItemViewHolder) holder).userSex.setVisibility(View.VISIBLE);
        } else {
            ((ItemViewHolder) holder).userSex.setVisibility(View.GONE);
        }
        ((ItemViewHolder) holder).smileCount.setText(itemBean.getSmileCount());
        ((ItemViewHolder) holder).commentCount.setText(itemBean.getCommentCount());
        ((ItemViewHolder) holder).commentGood.setText(itemBean.getCommentGoodName() + itemBean.getCommentGoodContent());

        if (itemBean.getCommentGoodName() == null || "".equals(itemBean.getCommentGoodName())) {
            ((ItemViewHolder) holder).commentLayout.setVisibility(View.GONE);
        }

        ((ItemViewHolder) holder).itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toQiuBaiContentActivity(position);
            }
        });

        ((ItemViewHolder) holder).itemImage_qiubai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toQiuBaiContentActivity(position);
            }
        });
    }

    public void toQiuBaiContentActivity(int position) {
        Intent intent = new Intent();
        intent.setClass(context, QiuBaiContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("itemBeanArrayList", itemBeanArrayList);
        bundle.putInt("qiubaiContentIndex", position);
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
            ButterKnife.bind(this, view);
        }
    }
}