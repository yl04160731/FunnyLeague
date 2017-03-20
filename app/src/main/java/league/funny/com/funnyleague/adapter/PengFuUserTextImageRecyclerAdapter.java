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
import league.funny.com.funnyleague.activity.PengFuContentActivity;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.util.GlideCircleTransform;

import static com.ashokvarma.bottomnavigation.utils.Utils.getScreenWidth;

public class PengFuUserTextImageRecyclerAdapter extends Adapter<ViewHolder> {

    private Context context;
    private ArrayList<ItemBean> itemBeanArrayList;


    public PengFuUserTextImageRecyclerAdapter(Context context, ArrayList<ItemBean> itemBeanArrayList) {
        this.context = context;
        this.itemBeanArrayList = itemBeanArrayList;
    }

    @Override
    public int getItemCount() {
        return itemBeanArrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_text_image_item_pengfu, parent,
                false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemBean itemBean = itemBeanArrayList.get(position);
        ((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).userName.setText(itemBean.getUserName());

        Glide.with(FunnyLeagueApplication.getApplication()).load(itemBean.getUserImage()).transform(new GlideCircleTransform(FunnyLeagueApplication.getApplication(), 40)).into(((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).userImage);


        if(itemBean.getItemContent() != null && !"".equals(itemBean.getItemContent())){
            ((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).itemContent.setVisibility(View.VISIBLE);
            ((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).itemContent.setText(itemBean.getItemContent());
        }else{
            ((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).itemContent.setVisibility(View.GONE);
        }

        if (itemBean.getItemImage() != null && !"".equals(itemBean.getItemImage())) {
            ((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = ((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage.getLayoutParams();
            int screenWidth = getScreenWidth(FunnyLeagueApplication.getApplication());
            params.width = screenWidth * 11 / 12;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            ((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage.setLayoutParams(params);
            ((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage.setMaxWidth(screenWidth);

            Glide.with(FunnyLeagueApplication.getApplication()).load(itemBean.getItemImage())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate().error(R.drawable.imageload).placeholder(R.drawable.imageload)
                    .into(((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage);
        } else {
            ((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage.setVisibility(View.GONE);
        }
        ((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).itemTitle.setText(itemBean.getItemContentTitle());
        TextPaint tp = ((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).itemTitle.getPaint();
        tp.setFakeBoldText(true);

        ((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).ding.setText(itemBean.getDing());
        ((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).cai.setText(itemBean.getCai());
        ((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).comment.setText(itemBean.getCommentCount());

        ((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPengFuContentActivity(position);
            }
        });

        ((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).itemTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPengFuContentActivity(position);
            }
        });

        ((PengFuUserTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage.setOnClickListener(new View.OnClickListener() {
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