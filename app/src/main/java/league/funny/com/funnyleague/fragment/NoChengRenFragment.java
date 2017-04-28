package league.funny.com.funnyleague.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.waps.AppConnect;
import cn.waps.UpdatePointsListener;
import league.funny.com.funnyleague.MainActivity;
import league.funny.com.funnyleague.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoChengRenFragment extends BaseFragment implements UpdatePointsListener {

    private View view;
    @BindView(R.id.jifenCount)
    TextView jifenCount;

    private final static int OPEN_7 = 50;
    private final static int OPEN_30 = 100;

    private String displayPointsText;

    final Handler mHandler = new Handler();

    public NoChengRenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_chengren_no, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    public void initData() {

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        AppConnect.getInstance(getActivity()).getPoints(this);
    }

    // 创建一个线程
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
            if (jifenCount != null) {
                jifenCount.setText(displayPointsText);
            }
        }
    };

    /**
     * AppConnect.getPoints()方法的实现，必须实现
     *
     * @param currencyName
     *            虚拟货币名称.
     * @param pointTotal
     *            虚拟货币余额.
     */
    public void getUpdatePoints(String currencyName, int pointTotal) {
        displayPointsText = "" + pointTotal;
        mHandler.post(mUpdateResults);
    }

    /**
     * AppConnect.getPoints() 方法的实现，必须实现
     *
     * @param error
     *            请求失败的错误信息
     */
    public void getUpdatePointsFailed(String error) {
        displayPointsText = error;
        mHandler.post(mUpdateResults);
    }

    @OnClick(R.id.zhuan)
    public void toOffer(){
        AppConnect.getInstance(getActivity()).showOffers(getActivity());
    }

    @OnClick(R.id.chengren_7)
    public void open7(){
        if(displayPointsText != null && !"".equals(displayPointsText)
                && Integer.parseInt(displayPointsText) > OPEN_7){
            AppConnect.getInstance(getActivity()).spendPoints(OPEN_7,this);
            SharedPreferences preferences = getActivity().getSharedPreferences("DuanZiLianMeng", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 7);
            String toDate = df.format(cal.getTime());
            editor.putString("date_chengren", toDate);
            editor.commit();
            ((MainActivity)getActivity()).onTabSelected(0);
            ((MainActivity)getActivity()).onTabSelected(2);
        }else{
            Toast.makeText(getActivity(),"积分不足，请先赚取积分",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.chengren30)
    public void open30(){
        if(displayPointsText != null && !"".equals(displayPointsText)
                && Integer.parseInt(displayPointsText) > OPEN_30){
            AppConnect.getInstance(getActivity()).spendPoints(OPEN_30,this);
            SharedPreferences preferences = getActivity().getSharedPreferences("DuanZiLianMeng", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 30);
            String toDate = df.format(cal.getTime());
            editor.putString("date_chengren", toDate);
            editor.commit();
            ((MainActivity)getActivity()).onTabSelected(0);
            ((MainActivity)getActivity()).onTabSelected(2);
        }else{
            Toast.makeText(getActivity(),"积分不足，请先赚取积分",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            AppConnect.getInstance(getActivity()).getPoints(this);
        }
    }
}
