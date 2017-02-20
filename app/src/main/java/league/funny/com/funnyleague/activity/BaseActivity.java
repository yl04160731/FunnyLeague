package league.funny.com.funnyleague.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import league.funny.com.funnyleague.FunnyLeagueApplication;

/**
 * Created by yangm on 17/2/6.
 */
public class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();
    public static BaseActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        activity = this;
        ((FunnyLeagueApplication)FunnyLeagueApplication.getApplication()).addActivity(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activity = null;
    }

    private void init() {
        initData();
        initEvents();
    }

    /***
     * 初始化事件（监听事件等事件绑定）
     */
    protected void initEvents() {
    }

    /**
     * 绑定数据
     */
    protected void initData() {
    }

    /**
     * activity退出时将activity移出栈
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((FunnyLeagueApplication)FunnyLeagueApplication.getApplication()).removeActivity(this);
    }
}
