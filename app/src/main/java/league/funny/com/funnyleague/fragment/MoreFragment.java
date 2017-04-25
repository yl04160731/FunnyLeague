package league.funny.com.funnyleague.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.adapter.MoreRecyclerAdapter;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.util.Util;
import league.funny.com.funnyleague.view.RecycleViewDivider;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends BaseFragment {

    private View view = null;
    @BindView(R.id.grid_recyclerView)
    RecyclerView grid_recyclerView;

    @BindView(R.id.cacheCount)
    TextView cacheCount;



    private ArrayList<ItemBean> itemBeanArrayList = new ArrayList<ItemBean>();
    private MoreRecyclerAdapter adapter;

    protected boolean isCreate = false;

    public MoreFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreate = true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        setHasOptionsMenu(true);
        return view;
    }

    public void initView() {
        GridLayoutManager mgr=new GridLayoutManager(getActivity(),3);
        grid_recyclerView.setLayoutManager(mgr);
        grid_recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), RecycleViewDivider.VERTICAL_LIST, R.drawable.divider));
    }

    public void initData() {
        cacheCount.setText(Util.getCacheSize(getActivity()));

        ItemBean itemBean = new ItemBean();
        itemBean.setItemImage(R.drawable.gold_coin + "");
        itemBean.setItemContentTitle("积分获得");
        itemBean.setItemContentUrl("jifen");
        itemBeanArrayList.add(itemBean);

        itemBean = new ItemBean();
        itemBean.setItemImage(R.drawable.neihan + "");
        itemBean.setItemContentTitle("内涵段子");
        itemBean.setItemContentUrl("http://neihanshequ.com/");
        itemBeanArrayList.add(itemBean);

        itemBean = new ItemBean();
        itemBean.setItemImage(R.drawable.huifu + "");
        itemBean.setItemContentTitle("神回复");
        itemBean.setItemContentUrl("http://www.shenhuif.com/");
        itemBeanArrayList.add(itemBean);

        itemBean = new ItemBean();
        itemBean.setItemImage(R.drawable.baozou + "");
        itemBean.setItemContentTitle("暴走漫画");
        itemBean.setItemContentUrl("http://baozoumanhua.com/");
        itemBeanArrayList.add(itemBean);

        itemBean = new ItemBean();
        itemBean.setItemImage(R.drawable.laifu + "");
        itemBean.setItemContentTitle("来福岛");
        itemBean.setItemContentUrl("http://www.laifudao.com/");
        itemBeanArrayList.add(itemBean);

        itemBean = new ItemBean();
        itemBean.setItemImage(R.drawable.neihanba + "");
        itemBean.setItemContentTitle("内涵吧");
        itemBean.setItemContentUrl("http://www.neihan8.com/");
        itemBeanArrayList.add(itemBean);

        itemBean = new ItemBean();
        itemBean.setItemImage(R.drawable.xixihaha + "");
        itemBean.setItemContentTitle("嘻嘻哈哈");
        itemBean.setItemContentUrl("http://www.xxhh.com/");
        itemBeanArrayList.add(itemBean);

        adapter = new MoreRecyclerAdapter(getActivity(), itemBeanArrayList);
        grid_recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        cacheCount.setText(Util.getCacheSize(getActivity()));
    }

    @OnClick(R.id.clearCache)
    public void clearCache(){
        Util.clearImageAllCache(getActivity());
        Toast.makeText(getActivity(),"清除缓存成功",Toast.LENGTH_SHORT).show();
        cacheCount.setText(Util.getCacheSize(getActivity()));
    }
}
