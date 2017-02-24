package league.funny.com.funnyleague;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationBar.OnTabSelectedListener;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.activity.BaseActivity;
import league.funny.com.funnyleague.fragment.ChengrenFragment;
import league.funny.com.funnyleague.fragment.ImageFragment;
import league.funny.com.funnyleague.fragment.MoreFragment;
import league.funny.com.funnyleague.fragment.TextFragment;
import league.funny.com.funnyleague.fragment.VideoFragment;

public class MainActivity extends BaseActivity implements OnTabSelectedListener {

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;

    private Fragment currentFragment;

    private final int EXIT_ALL_TIME = 2000;
    private long exitTime = 0;

    private TextFragment textFragment = null;
    private ImageFragment imageFragment = null;
    private VideoFragment videoFragment = null;
    private ChengrenFragment chengrenFragment = null;
    private MoreFragment moreFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((FunnyLeagueApplication)FunnyLeagueApplication.getContext()).addActivity(this);
        ButterKnife.bind(this);

        if(Build.VERSION.SDK_INT>=21){
            getSupportActionBar().setElevation(0);
        }

        initUI();
        initListener();
    }

    private void initUI(){
        bottomNavigationBar
                .setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);

        bottomNavigationBar.setBarBackgroundColor(R.color.colorPrimary);
        bottomNavigationBar.setInActiveColor(R.color.gray);//未选中时的颜色
        bottomNavigationBar.setActiveColor(R.color.white);//选中时的颜色

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.bottom_text, R.string.title_text))
                .addItem(new BottomNavigationItem(R.drawable.bottom_image, R.string.title_image))
                .addItem(new BottomNavigationItem(R.drawable.bottom_video, R.string.title_video))
                .addItem(new BottomNavigationItem(R.drawable.bottom_chengren, R.string.title_chengren))
                .addItem(new BottomNavigationItem(R.drawable.bottom_more, R.string.title_more))
                .initialise();

        if(currentFragment == null){
            Fragment fragment = getFragmentById(0);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();
            currentFragment = fragment;
        }
    }

    private void initListener(){
        bottomNavigationBar.setTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(int position) {

        Fragment fragment = getFragmentById(position);

//        if (currentFragment == null || !currentFragment
//                .getClass().getName().equals(fragment.getClass().getName())) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, fragment).commit();
//        }
        if (!fragment.isAdded()) {	// 先判断是否被add过
            getSupportFragmentManager().beginTransaction().hide(currentFragment).add(R.id.fragment_container, fragment).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            getSupportFragmentManager().beginTransaction().hide(currentFragment).show(fragment).commit(); // 隐藏当前的fragment，显示下一个
        }

        currentFragment = fragment;

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private Fragment getFragmentById(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                if(textFragment == null){
                    textFragment = new TextFragment();
                    fragment = textFragment;
                }else{
                    fragment = textFragment;
                }
                break;
            case 1:
                if(imageFragment == null){
                    imageFragment = new ImageFragment();
                    fragment = imageFragment;
                }else{
                    fragment = imageFragment;
                }
                break;
            case 2:
                if(videoFragment == null){
                    videoFragment = new VideoFragment();
                    fragment = videoFragment;
                }else{
                    fragment = videoFragment;
                }
                break;
            case 3:
                if(chengrenFragment == null){
                    chengrenFragment = new ChengrenFragment();
                    fragment = chengrenFragment;
                }else{
                    fragment = chengrenFragment;
                }
                break;
            case 4:
                if(moreFragment == null){
                    moreFragment = new MoreFragment();
                    fragment = moreFragment;
                }else{
                    fragment = moreFragment;
                }
                break;

        }
        return fragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > EXIT_ALL_TIME)
            {
                Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.out_app), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                FunnyLeagueApplication.quiteApplication();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
