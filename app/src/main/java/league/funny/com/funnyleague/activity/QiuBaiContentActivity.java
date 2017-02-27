package league.funny.com.funnyleague.activity;

import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.fragment.QiuBaiContentFragment;

public class QiuBaiContentActivity extends BaseActivity {

    private String htmlUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_qiubai);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    public void initData(){
        Intent intent = this.getIntent();
        htmlUrl=(String)intent.getSerializableExtra("htmlUrl");

        Bundle bundle=new Bundle();
        bundle.putString("htmlUrl", htmlUrl);
        QiuBaiContentFragment fragment = new QiuBaiContentFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_qiubai, fragment).commit();
    }
}
