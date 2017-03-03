package league.funny.com.funnyleague.activity;

import android.content.Intent;
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
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.bean.QiuBaiItemBean;
import league.funny.com.funnyleague.fragment.QiuBaiContentFragment;

public class QiuBaiContentActivity extends BaseActivity {

    private ArrayList<QiuBaiItemBean> qiuBaiItemBeanArrayList;

    @BindView(R.id.viewPager_qiubai)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_qiubai);
        ButterKnife.bind(this);
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
        qiuBaiItemBeanArrayList = (ArrayList<QiuBaiItemBean>) intent.getSerializableExtra("qiuBaiItemBeanArrayList");
        int qiubaiContentIndex = intent.getIntExtra("qiubaiContentIndex", 0);
        final QiuBaiPagerAdapter qiuBaiPagerAdapter = new QiuBaiPagerAdapter(
                getSupportFragmentManager());
        viewPager.setAdapter(qiuBaiPagerAdapter);
        viewPager.setCurrentItem(qiubaiContentIndex);
    }

    private class QiuBaiPagerAdapter extends FragmentStatePagerAdapter {
        public QiuBaiPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            QiuBaiContentFragment qiuBaiContentFragment = new QiuBaiContentFragment();
            qiuBaiContentFragment.setQiuBaiItemBean(qiuBaiItemBeanArrayList.get(position));
            return qiuBaiContentFragment;
        }

        @Override
        public int getCount() {
            return qiuBaiItemBeanArrayList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
        }
    }
}
