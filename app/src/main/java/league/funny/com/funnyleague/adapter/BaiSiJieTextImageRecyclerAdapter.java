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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.FunnyLeagueApplication;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.util.GlideCircleTransform;

import static com.ashokvarma.bottomnavigation.utils.Utils.getScreenWidth;

public class BaiSiJieTextImageRecyclerAdapter extends Adapter<ViewHolder> {

    private Context context;
    private ArrayList<ItemBean> itemBeanArrayList;

    public BaiSiJieTextImageRecyclerAdapter(Context context, ArrayList<ItemBean> itemBeanArrayList) {
        this.context = context;
        this.itemBeanArrayList = itemBeanArrayList;
    }

    @Override
    public int getItemCount() {
        return itemBeanArrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_text_image_item_baisijie, parent,
                false);
        return new BaiSiJieTextImageRecyclerAdapter.ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemBean itemBean = itemBeanArrayList.get(position);
        ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).userName.setText(itemBean.getUserName());

        Glide.with(FunnyLeagueApplication.getApplication()).load(itemBean.getUserImage()).transform(new GlideCircleTransform(FunnyLeagueApplication.getApplication(), 40)).into(((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).userImage);
        if(itemBean.getItemContent() != null && !"".equals(itemBean.getItemContent())){
            ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).itemContent.setVisibility(View.VISIBLE);
            ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).itemContent.setText(itemBean.getItemContent());
        }else{
            ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).itemContent.setVisibility(View.GONE);
        }

        if (itemBean.getItemImage() != null && !"".equals(itemBean.getItemImage())) {
            ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage.getLayoutParams();
            int screenWidth = getScreenWidth(FunnyLeagueApplication.getApplication());
            params.width = screenWidth * 11 / 12;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage.setLayoutParams(params);
            ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage.setMaxWidth(screenWidth);

            Glide.with(FunnyLeagueApplication.getApplication()).load(itemBean.getItemImage())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate().error(R.drawable.imageload).placeholder(R.drawable.imageload)
                    .into(((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage);

        } else {
            ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage.setVisibility(View.GONE);
        }

        if(itemBean.getItemContentTitle() == null || "".equals(itemBean.getItemContentTitle())){
            ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).itemTitle.setVisibility(View.GONE);
        }else{
            ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).itemTitle.setVisibility(View.VISIBLE);
            ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).itemTitle.setText(itemBean.getItemContentTitle());
            TextPaint tp = ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).itemTitle.getPaint();
            tp.setFakeBoldText(true);
        }

        if(itemBean.getSortTag() == null || "".equals(itemBean.getSortTag())){
            ((ItemViewHolder) holder).linearLayout_tag.setVisibility(View.GONE);
        }else{
            ((ItemViewHolder) holder).linearLayout_tag.setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).tag.setText(itemBean.getSortTag());
        }

        ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).ding.setText(itemBean.getDing());
        ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).cai.setText(itemBean.getCai());
        ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).comment.setText(itemBean.getCommentCount());

        ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFanJianContentActivity(position);
            }
        });

        ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFanJianContentActivity(position);
            }
        });
        ((BaiSiJieTextImageRecyclerAdapter.ItemViewHolder) holder).itemTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFanJianContentActivity(position);
            }
        });

    }

    public void toFanJianContentActivity(int position) {
        Intent intent = new Intent();
//        intent.setClass(context, BaiSiJieContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("itemBeanArrayList", itemBeanArrayList);
        bundle.putInt("BaiSiJieContentIndex", position);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

//    public void toFanJianUserActivity(ItemBean itemBean) {
//        if (itemBean.getUserUrl() == null || "".equals(itemBean.getUserUrl())) {
//            return;
//        }
//        Intent intent = new Intent();
//        intent.setClass(context, FanJianUserActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("itemBean", itemBean);
//        intent.putExtras(bundle);
//        context.startActivity(intent);
//    }

    public static class ItemViewHolder extends ViewHolder {

        @BindView(R.id.userName_baisijie)
        TextView userName;

        @BindView(R.id.itemContent_baisijie)
        TextView itemContent;

        @BindView(R.id.userImage_baisijie)
        ImageView userImage;

        @BindView(R.id.itemImage_baisijie)
        ImageView itemImage;

        @BindView(R.id.itemTitle_baisijie)
        TextView itemTitle;

        @BindView(R.id.ding)
        TextView ding;

        @BindView(R.id.cai)
        TextView cai;

        @BindView(R.id.comment)
        TextView comment;

        @BindView(R.id.tag)
        TextView tag;

        @BindView(R.id.linearLayout_tag)
        LinearLayout linearLayout_tag;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}