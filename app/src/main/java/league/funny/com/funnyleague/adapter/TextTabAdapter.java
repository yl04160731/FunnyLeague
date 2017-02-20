package league.funny.com.funnyleague.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import league.funny.com.funnyleague.fragment.TextSubFragment;

public class TextTabAdapter extends FragmentPagerAdapter {
    // 内容标题
    public static final String[] Video_TITLE = new String[]{"糗事百科",
            "捧腹网", "内涵段子", "百思姐"};

    public TextTabAdapter(FragmentManager fm) {
        super(fm);
    }

    // 获取项
    @Override
    public Fragment getItem(int position) {
        TextSubFragment textSubFragment = null;
        switch (position) {
            case 0:
                textSubFragment = new TextSubFragment(0);
                return textSubFragment;
            case 1:
                textSubFragment = new TextSubFragment(1);
                return textSubFragment;
            case 2:
                textSubFragment = new TextSubFragment(2);
                return textSubFragment;
            case 3:
                textSubFragment = new TextSubFragment(3);
                return textSubFragment;
        }
        return textSubFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // 返回页面标题
        return Video_TITLE[position % Video_TITLE.length].toUpperCase();
    }

    @Override
    public int getCount() {
        // 页面个数
        return Video_TITLE.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}