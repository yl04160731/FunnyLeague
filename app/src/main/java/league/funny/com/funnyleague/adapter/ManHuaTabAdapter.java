package league.funny.com.funnyleague.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import league.funny.com.funnyleague.fragment.ManHuaImageFragment;
import league.funny.com.funnyleague.util.HttpUrlUtil;

public class ManHuaTabAdapter extends FragmentPagerAdapter {
    // 内容标题
    public static final String[] Video_TITLE = new String[]{"邪恶漫画",
            "内涵漫画", "色系漫画", "卡列漫画", "幻啃漫画"};

    public ManHuaTabAdapter(FragmentManager fm) {
        super(fm);
    }

    // 获取项
    @Override
    public Fragment getItem(int position) {
        Fragment textFragment = null;
        switch (position) {
            case 0:
                textFragment = new ManHuaImageFragment();
                ((ManHuaImageFragment)textFragment).setType(HttpUrlUtil.TYPE_YOU_QU_XIE_E);
                return textFragment;
            case 1:
                textFragment = new ManHuaImageFragment();
                ((ManHuaImageFragment)textFragment).setType(HttpUrlUtil.TYPE_YOU_QU_NEI_HAN);
                return textFragment;
            case 2:
                textFragment = new ManHuaImageFragment();
                ((ManHuaImageFragment)textFragment).setType(HttpUrlUtil.TYPE_YOU_QU_SE_XI);
                return textFragment;
            case 3:
                textFragment = new ManHuaImageFragment();
                ((ManHuaImageFragment)textFragment).setType(HttpUrlUtil.TYPE_YOU_QU_KA_LIE);
                return textFragment;
            case 4:
                textFragment = new ManHuaImageFragment();
                ((ManHuaImageFragment)textFragment).setType(HttpUrlUtil.TYPE_YOU_QU_HUAN_KEN);
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