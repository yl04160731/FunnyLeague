package league.funny.com.funnyleague;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationBar.OnTabSelectedListener;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.waps.AppConnect;
import league.funny.com.funnyleague.activity.BaseActivity;
import league.funny.com.funnyleague.api.ApiManage;
import league.funny.com.funnyleague.bean.GlobleBean;
import league.funny.com.funnyleague.bean.image.ImageResponse;
import league.funny.com.funnyleague.fragment.ChengFragment;
import league.funny.com.funnyleague.fragment.ImageFragment;
import league.funny.com.funnyleague.fragment.ManHuaFragment;
import league.funny.com.funnyleague.fragment.MoreFragment;
import league.funny.com.funnyleague.fragment.NoChengRenFragment;
import league.funny.com.funnyleague.fragment.NoManHuaFragment;
import league.funny.com.funnyleague.fragment.TextFragment;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements OnTabSelectedListener {

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;

    private Fragment currentFragment;

    private final int EXIT_ALL_TIME = 2000;
    private long exitTime = 0;

    private TextFragment textFragment = null;
    private ImageFragment imageFragment = null;
    private ManHuaFragment manHuaFragment = null;
    private ChengFragment chengrenFragment = null;
    private MoreFragment moreFragment = null;

    private NoChengRenFragment noChengRenFragment = null;
    private NoManHuaFragment noManHuaFragment = null;

    private Subscription subscription;

    public ChengFragment getChengrenFragment() {
        return chengrenFragment;
    }

    public void setChengrenFragment(ChengFragment chengrenFragment) {
        this.chengrenFragment = chengrenFragment;
    }

    public ManHuaFragment getManHuaFragment() {
        return manHuaFragment;
    }

    public void setManHuaFragment(ManHuaFragment manHuaFragment) {
        this.manHuaFragment = manHuaFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((FunnyLeagueApplication) FunnyLeagueApplication.getContext()).addActivity(this);
        ButterKnife.bind(this);
        AppConnect.getInstance(this); // 广告
        AppConnect.getInstance(this).initPopAd(this); // 广告
        AppConnect.getInstance(this).showPopAd(this); // 广告
        getBackground();
        if (Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(0);
        }

        initUI();
        initListener();
    }

    private void initUI() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_main);//自定义ActionBar布局
        bottomNavigationBar
                .setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);

        bottomNavigationBar.setBarBackgroundColor(R.color.colorPrimary);
        bottomNavigationBar.setInActiveColor(R.color.gray);//未选中时的颜色
        bottomNavigationBar.setActiveColor(R.color.white);//选中时的颜色

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.bottom_text, R.string.title_text))
                .addItem(new BottomNavigationItem(R.drawable.bottom_image, R.string.title_image))
                .addItem(new BottomNavigationItem(R.drawable.bottom_chengren, R.string.title_chengren))
                .addItem(new BottomNavigationItem(R.drawable.bottom_video, R.string.title_qingqutu))
                .addItem(new BottomNavigationItem(R.drawable.bottom_more, R.string.title_more))
                .initialise();

        if (currentFragment == null) {
            Fragment fragment = getFragmentById(0);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();
            currentFragment = fragment;
        }
    }

    private void initListener() {
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
        if (!fragment.isAdded()) {    // 先判断是否被add过
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
        SharedPreferences preferences = getSharedPreferences("DuanZiLianMeng", Context.MODE_PRIVATE);
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String NowTime = df.format(new Date());
        switch (position) {
            case 0:
                if (textFragment == null) {
                    textFragment = new TextFragment();
                    fragment = textFragment;
                } else {
                    fragment = textFragment;
                }
                break;
            case 1:
                if (imageFragment == null) {
                    imageFragment = new ImageFragment();
                    fragment = imageFragment;
                } else {
                    fragment = imageFragment;
                }
                break;
            case 2:
                String date_chengren = preferences.getString("date_chengren","");
                if(date_chengren.compareTo(NowTime) < 0) {
                    if (noChengRenFragment == null) {
                        noChengRenFragment = new NoChengRenFragment();
                        fragment = noChengRenFragment;
                    } else {
                        fragment = noChengRenFragment;
                    }
                } else {
                    if (chengrenFragment == null) {
                        chengrenFragment = new ChengFragment();
                        fragment = chengrenFragment;
                    } else {
                        fragment = chengrenFragment;
                    }
                }
                break;
            case 3:
                String date_manhua = preferences.getString("date_manhua","");
                if(date_manhua.compareTo(NowTime) < 0) {
                    if (noManHuaFragment == null) {
                        noManHuaFragment = new NoManHuaFragment();
                        fragment = noManHuaFragment;
                    } else {
                        fragment = noManHuaFragment;
                    }
                }else {
                    if (manHuaFragment == null) {
                        manHuaFragment = new ManHuaFragment();
                        fragment = manHuaFragment;
                    } else {
                        fragment = manHuaFragment;
                    }
                }
                break;
            case 4:
                if (moreFragment == null) {
                    moreFragment = new MoreFragment();
                    fragment = moreFragment;
                } else {
                    fragment = moreFragment;
                }
                break;

        }
        return fragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > EXIT_ALL_TIME) {
                Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.out_app), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                AppConnect.getInstance(this).close(); // 广告
                FunnyLeagueApplication.quiteApplication();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void getBackground() {

        if (GlobleBean.imageResponse == null) {

            subscription = ApiManage.getInstence().getZuiMeiApiService().getImage()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ImageResponse>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(ImageResponse imageReponse) {
                            if (imageReponse != null) {
                                GlobleBean.imageResponse = imageReponse;
                            } else {
                                return;
                            }

                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
