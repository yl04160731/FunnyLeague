package league.funny.com.funnyleague.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.FunnyLeagueApplication;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.bean.ItemBean;
import league.funny.com.funnyleague.fragment.ManHuaContentFragment;

public class ManHuaContentActivity extends BaseActivity {

    private ArrayList<ItemBean> itemBeanArrayList;

    @BindView(R.id.viewPager_manhua)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_manhua);
        ((FunnyLeagueApplication)FunnyLeagueApplication.getContext()).addActivity(this);
        ButterKnife.bind(this);

        if(Build.VERSION.SDK_INT>=21){
            getSupportActionBar().setElevation(0);
        }

        initData();
    }

    @Override
    public void initData() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_content);//自定义ActionBar布局
        ImageButton imageButton = (ImageButton)actionBar.getCustomView().findViewById(R.id.contentBack);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = this.getIntent();
        itemBeanArrayList = (ArrayList<ItemBean>) intent.getSerializableExtra("itemBeanArrayList");
        int ManHuaContentIndex = intent.getIntExtra("ManHuaContentIndex", 0);
        final ManHuaContentActivity.ManHuaPagerAdapter ManHuaPagerAdapter = new ManHuaContentActivity.ManHuaPagerAdapter(
                getSupportFragmentManager());
        viewPager.setAdapter(ManHuaPagerAdapter);
        viewPager.setCurrentItem(ManHuaContentIndex);
    }

    private class ManHuaPagerAdapter extends FragmentStatePagerAdapter {
        public ManHuaPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ManHuaContentFragment ManHuaContentFragment = new ManHuaContentFragment();
            ManHuaContentFragment.setItemBean(itemBeanArrayList.get(position));
            return ManHuaContentFragment;
        }

        @Override
        public int getCount() {
            return itemBeanArrayList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
        }
    }
}
