package league.funny.com.funnyleague.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import league.funny.com.funnyleague.fragment.ZOLYouQuTextFragment;
import league.funny.com.funnyleague.util.HttpUrlUtil;

public class ChengRenTabAdapter extends FragmentPagerAdapter {
    // 内容标题
    public static final String[] Video_TITLE = new String[]{"ZOL",
            "爆笑男女", "有趣吧", "夫妻笑话"};

    public ChengRenTabAdapter(FragmentManager fm) {
        super(fm);
    }

    // 获取项
    @Override
    public Fragment getItem(int position) {
        Fragment textFragment = null;
        switch (position) {
            case 0:
                textFragment = new ZOLYouQuTextFragment();
                ((ZOLYouQuTextFragment)textFragment).setType(HttpUrlUtil.TYPE_ZOL_CHENG_REN);
                return textFragment;
            case 1:
                textFragment = new ZOLYouQuTextFragment();
                ((ZOLYouQuTextFragment)textFragment).setType(HttpUrlUtil.TYPE_ZOL_NAN_NV);
                return textFragment;
            case 2:
                textFragment = new ZOLYouQuTextFragment();
                ((ZOLYouQuTextFragment)textFragment).setType(HttpUrlUtil.TYPE_YOU_QU_CHENG_REN);
                return textFragment;
            case 3:
                textFragment = new ZOLYouQuTextFragment();
                ((ZOLYouQuTextFragment)textFragment).setType(HttpUrlUtil.TYPE_YOU_QU_NAN_NV);
                return textFragment;
        }
        return textFragment;
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