package league.funny.com.funnyleague.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import league.funny.com.funnyleague.fragment.BaiSiJieTextFragment;
import league.funny.com.funnyleague.fragment.NeiHanTextFragment;
import league.funny.com.funnyleague.fragment.PengFuTextFragment;
import league.funny.com.funnyleague.fragment.QiuBaiTextImageFragment;
import league.funny.com.funnyleague.util.HttpUrlUtil;

public class ImageTabAdapter extends FragmentPagerAdapter {
    // 内容标题
    public static final String[] Video_TITLE = new String[]{"糗事百科",
            "捧腹网", "内涵段子", "百思姐"};

    public ImageTabAdapter(FragmentManager fm) {
        super(fm);
    }

    // 获取项
    @Override
    public Fragment getItem(int position) {
        Fragment textFragment = null;
        switch (position) {
            case 0:
                textFragment = new QiuBaiTextImageFragment();
                ((QiuBaiTextImageFragment)textFragment).setType(HttpUrlUtil.TYPE_IMAGE);
                return textFragment;
            case 1:
                textFragment = new PengFuTextFragment();
                return textFragment;
            case 2:
                textFragment = new NeiHanTextFragment();
                return textFragment;
            case 3:
                textFragment = new BaiSiJieTextFragment();
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