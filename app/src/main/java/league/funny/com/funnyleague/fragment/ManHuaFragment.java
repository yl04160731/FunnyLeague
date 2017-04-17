package league.funny.com.funnyleague.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.PageIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.adapter.ManHuaTabAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManHuaFragment extends BaseFragment {

    private View view = null;
    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.indicator)
    PageIndicator indicator;

    public ManHuaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_manhua, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initDate();
        initView();
        super.onViewCreated(view, savedInstanceState);
    }

    public void initDate(){
        FragmentPagerAdapter adapter = new ManHuaTabAdapter(
                getChildFragmentManager());
        // 视图切换器
        pager.setOffscreenPageLimit(ManHuaTabAdapter.Video_TITLE.length);
        pager.setAdapter(adapter);

        // 页面指示器
        indicator.setViewPager(pager);
    }

    public void initView(){

    }

}
