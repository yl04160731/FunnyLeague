package league.funny.com.funnyleague.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.adapter.MoreRecyclerAdapter;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.view.RecycleViewDivider;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends BaseFragment {

    private View view = null;
    @BindView(R.id.grid_recyclerView)
    RecyclerView grid_recyclerView;

    private ArrayList<ItemBean> itemBeanArrayList = new ArrayList<ItemBean>();
    private MoreRecyclerAdapter adapter;

    public MoreFragment() {
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
        ItemBean itemBean = new ItemBean();
        itemBean.setItemImage(R.drawable.gold_coin + "");
        itemBean.setItemContentTitle("积分获得");
        itemBean.setItemContentUrl("http://neihanshequ.com/");
        itemBeanArrayList.add(itemBean);
        itemBeanArrayList.add(itemBean);
        itemBeanArrayList.add(itemBean);
        itemBeanArrayList.add(itemBean);
        itemBeanArrayList.add(itemBean);
        itemBeanArrayList.add(itemBean);
        itemBeanArrayList.add(itemBean);
        itemBeanArrayList.add(itemBean);
        itemBeanArrayList.add(itemBean);
        adapter = new MoreRecyclerAdapter(getActivity(), itemBeanArrayList);
        grid_recyclerView.setAdapter(adapter);
    }

}
