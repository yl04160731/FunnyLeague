package league.funny.com.funnyleague.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.FunnyLeagueApplication;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.activity.NeiHanContentActivity;
import league.funny.com.funnyleague.activity.NeiHanUserActivity;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.util.GlideCircleTransform;

import static com.ashokvarma.bottomnavigation.utils.Utils.getScreenWidth;

public class NeiHanTextImageRecyclerAdapter extends Adapter<ViewHolder> {

    private Context context;
    private ArrayList<ItemBean> itemBeanArrayList;

    public NeiHanTextImageRecyclerAdapter(Context context, ArrayList<ItemBean> itemBeanArrayList) {
        this.context = context;
        this.itemBeanArrayList = itemBeanArrayList;
    }

    @Override
    public int getItemCount() {
        return itemBeanArrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_text_image_item_neihan, parent,
                false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemBean itemBean = itemBeanArrayList.get(position);
        ((ItemViewHolder) holder).userName.setText(itemBean.getUserName());

        Glide.with(FunnyLeagueApplication.getApplication()).load(itemBean.getUserImage()).transform(new GlideCircleTransform(FunnyLeagueApplication.getApplication(), 40)).into(((ItemViewHolder) holder).userImage);
        if(itemBean.getItemContent() != null && !"".equals(itemBean.getItemContent())){
            ((NeiHanTextImageRecyclerAdapter.ItemViewHolder) holder).itemContent.setVisibility(View.VISIBLE);
            ((NeiHanTextImageRecyclerAdapter.ItemViewHolder) holder).itemContent.setText(itemBean.getItemContent());
        }else{
            ((NeiHanTextImageRecyclerAdapter.ItemViewHolder) holder).itemContent.setVisibility(View.GONE);
        }

        if (itemBean.getItemImage() != null && !"".equals(itemBean.getItemImage())) {
            ((NeiHanTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = ((NeiHanTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage.getLayoutParams();
            int screenWidth = getScreenWidth(FunnyLeagueApplication.getApplication());
            params.width = screenWidth * 11 / 12;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            ((NeiHanTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage.setLayoutParams(params);
            ((NeiHanTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage.setMaxWidth(screenWidth);

            Glide.with(FunnyLeagueApplication.getApplication()).load(itemBean.getItemImage())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate().error(R.drawable.imageload).placeholder(R.drawable.imageload)
                    .into(((NeiHanTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage);

        } else {
            ((NeiHanTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage.setVisibility(View.GONE);
        }
        ((ItemViewHolder) holder).itemTitle.setText(itemBean.getItemContentTitle());
        TextPaint tp = ((ItemViewHolder) holder).itemTitle.getPaint();
        tp.setFakeBoldText(true);

        ((ItemViewHolder) holder).ding.setText(itemBean.getDing());
        ((ItemViewHolder) holder).cai.setText(itemBean.getCai());
        ((ItemViewHolder) holder).comment.setText(itemBean.getCommentCount());

        ((ItemViewHolder) holder).userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNeiHanUserActivity(itemBean);
            }
        });

        ((ItemViewHolder) holder).userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNeiHanUserActivity(itemBean);
            }
        });

        ((ItemViewHolder) holder).itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNeiHanContentActivity(position);
            }
        });

        ((ItemViewHolder) holder).itemTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNeiHanContentActivity(position);
            }
        });

        ((ItemViewHolder) holder).itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNeiHanContentActivity(position);
            }
        });

    }

    public void toNeiHanContentActivity(int position) {
        Intent intent = new Intent();
        intent.setClass(context, NeiHanContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("itemBeanArrayList", itemBeanArrayList);
        bundle.putInt("NeiHanContentIndex", position);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void toNeiHanUserActivity(ItemBean itemBean) {
        if (itemBean.getUserUrl() == null || "".equals(itemBean.getUserUrl())) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, NeiHanUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("itemBean", itemBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static class ItemViewHolder extends ViewHolder {

        @BindView(R.id.userName_neihan)
        TextView userName;

        @BindView(R.id.itemContent_neihan)
        TextView itemContent;

        @BindView(R.id.userImage_neihan)
        ImageView userImage;

        @BindView(R.id.itemImage_neihan)
        ImageView itemImage;

        @BindView(R.id.itemTitle_neihan)
        TextView itemTitle;

        @BindView(R.id.ding)
        TextView ding;

        @BindView(R.id.cai)
        TextView cai;

        @BindView(R.id.comment)
        TextView comment;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}