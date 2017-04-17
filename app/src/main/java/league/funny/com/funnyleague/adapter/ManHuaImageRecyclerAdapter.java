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
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.util.GlideCircleTransform;

import league.funny.com.funnyleague.activity.ManHuaContentActivity;

public class ManHuaImageRecyclerAdapter extends Adapter<ViewHolder> {

    private Context context;
    private ArrayList<ItemBean> itemBeanArrayList;

    public ManHuaImageRecyclerAdapter(Context context, ArrayList<ItemBean> itemBeanArrayList) {
        this.context = context;
        this.itemBeanArrayList = itemBeanArrayList;
    }

    @Override
    public int getItemCount() {
        return itemBeanArrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_image_item_manhua, parent,
                false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemBean itemBean = itemBeanArrayList.get(position);

        Glide.with(FunnyLeagueApplication.getApplication())
                .load(itemBean.getItemImage())
                .transform(new GlideCircleTransform(FunnyLeagueApplication.getApplication(), 5))
                .into(((ItemViewHolder) holder).itemContent);

        ((ItemViewHolder) holder).itemTitle.setText(itemBean.getItemContentTitle());
//        TextPaint tp = ((ItemViewHolder) holder).itemTitle.getPaint();
//        tp.setFakeBoldText(true);

        ((ItemViewHolder) holder).itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toManHuaContentActivity(position);
            }
        });

        ((ItemViewHolder) holder).itemTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toManHuaContentActivity(position);
            }
        });

        ((ItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toManHuaContentActivity(position);
            }
        });

    }

    public void toManHuaContentActivity(int position) {
        Intent intent = new Intent();
        intent.setClass(context, ManHuaContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("itemBeanArrayList", itemBeanArrayList);
        bundle.putInt("ManHuaContentIndex", position);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static class ItemViewHolder extends ViewHolder {

        @BindView(R.id.itemContent_manhua)
        ImageView itemContent;

        @BindView(R.id.itemTitle_manhua)
        TextView itemTitle;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}