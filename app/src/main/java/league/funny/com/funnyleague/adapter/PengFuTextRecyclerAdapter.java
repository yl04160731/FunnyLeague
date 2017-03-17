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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.FunnyLeagueApplication;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.activity.PengFuContentActivity;
import league.funny.com.funnyleague.activity.PengFuUserActivity;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.util.GlideCircleTransform;

public class PengFuTextRecyclerAdapter extends Adapter<ViewHolder> {

    private Context context;
    private ArrayList<ItemBean> itemBeanArrayList;

    public PengFuTextRecyclerAdapter(Context context, ArrayList<ItemBean> itemBeanArrayList) {
        this.context = context;
        this.itemBeanArrayList = itemBeanArrayList;
    }

    @Override
    public int getItemCount() {
        return itemBeanArrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_text_item_pengfu, parent,
                false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemBean itemBean = itemBeanArrayList.get(position);
        ((ItemViewHolder) holder).userName.setText(itemBean.getUserName());

        Glide.with(FunnyLeagueApplication.getApplication()).load(itemBean.getUserImage()).transform(new GlideCircleTransform(FunnyLeagueApplication.getApplication(), 40)).into(((ItemViewHolder) holder).userImage);
        ((ItemViewHolder) holder).itemContent.setText(itemBean.getItemContent());
        ((ItemViewHolder) holder).itemTitle.setText(itemBean.getItemContentTitle());
        TextPaint tp = ((ItemViewHolder) holder).itemTitle.getPaint();
        tp.setFakeBoldText(true);

        ((ItemViewHolder) holder).ding.setText(itemBean.getDing());
        ((ItemViewHolder) holder).cai.setText(itemBean.getCai());
        ((ItemViewHolder) holder).comment.setText(itemBean.getCommentCount());

        ((ItemViewHolder) holder).userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPengFuUserActivity(itemBean);
            }
        });

        ((ItemViewHolder) holder).userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPengFuUserActivity(itemBean);
            }
        });

        ((ItemViewHolder) holder).itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPengFuContentActivity(position);
            }
        });

        ((ItemViewHolder) holder).itemTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPengFuContentActivity(position);
            }
        });

    }

    public void toPengFuContentActivity(int position) {
        Intent intent = new Intent();
        intent.setClass(context, PengFuContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("itemBeanArrayList", itemBeanArrayList);
        bundle.putInt("pengFuContentIndex", position);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void toPengFuUserActivity(ItemBean itemBean) {
        if (itemBean.getUserUrl() == null || "".equals(itemBean.getUserUrl())) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, PengFuUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("itemBean", itemBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static class ItemViewHolder extends ViewHolder {

        @BindView(R.id.userName_pengfu)
        TextView userName;

        @BindView(R.id.itemContent_pengfu)
        TextView itemContent;

        @BindView(R.id.userImage_pengfu)
        ImageView userImage;

        @BindView(R.id.itemImage_pengfu)
        ImageView itemImage;

        @BindView(R.id.itemTitle_pengfu)
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