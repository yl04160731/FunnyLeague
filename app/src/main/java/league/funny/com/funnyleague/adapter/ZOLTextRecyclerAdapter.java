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
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.activity.PengFuContentActivity;
import league.funny.com.funnyleague.bean.ItemBean;

public class ZOLTextRecyclerAdapter extends Adapter<ViewHolder> {

    private Context context;
    private ArrayList<ItemBean> itemBeanArrayList;

    public ZOLTextRecyclerAdapter(Context context, ArrayList<ItemBean> itemBeanArrayList) {
        this.context = context;
        this.itemBeanArrayList = itemBeanArrayList;
    }

    @Override
    public int getItemCount() {
        return itemBeanArrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_text_item_zol, parent,
                false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemBean itemBean = itemBeanArrayList.get(position);

        if(itemBean.getItemContent() != null && !"".equals(itemBean.getItemContent())){
            ((ItemViewHolder) holder).itemContent.setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).itemContent.setText(itemBean.getItemContent());
        }else{
            ((ItemViewHolder) holder).itemContent.setVisibility(View.GONE);
        }

        ((ItemViewHolder) holder).itemTitle.setText(itemBean.getItemContentTitle());
        TextPaint tp = ((ItemViewHolder) holder).itemTitle.getPaint();
        tp.setFakeBoldText(true);

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

    public static class ItemViewHolder extends ViewHolder {

        @BindView(R.id.itemContent_zol)
        TextView itemContent;

        @BindView(R.id.itemTitle_zol)
        TextView itemTitle;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}