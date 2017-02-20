package league.funny.com.funnyleague.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import league.funny.com.funnyleague.R;
import league.funny.com.funnyleague.adapter.TextRecyclerAdapter;
import league.funny.com.funnyleague.view.RecycleViewDivider;

/**
 * A simple {@link Fragment} subclass.
 */
public class TextSubFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private int pid;
    private View view = null;

    private TextRecyclerAdapter textRecyclerAdapter;

    @BindView(R.id.swipeRefreshLayout_text)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView_text)
    RecyclerView recyclerView;

    public TextSubFragment() {
        // Required empty public constructor
    }

    private List<String> mList = new ArrayList<>();

    public TextSubFragment(int pid) {
        this.pid = pid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_text_sub, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);


        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.gray);

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        textRecyclerAdapter = new TextRecyclerAdapter(getActivity(), initData());
        recyclerView.setAdapter(textRecyclerAdapter);

        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(),RecycleViewDivider.VERTICAL_LIST));
        return view;
    }

    private List<String> initData() {
        List<String> datas = new ArrayList<String>();
        for (int i = 0; i <= 30; i++) {
            datas.add("item:" + Math.random());
        }
        return datas;
    }

    @Override
    public void onRefresh() {

        swipeRefreshLayout.setRefreshing(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textRecyclerAdapter = new TextRecyclerAdapter(getActivity(), initData());
                recyclerView.setAdapter(textRecyclerAdapter);
                textRecyclerAdapter.notifyDataSetChanged();

                Toast.makeText(getActivity(), "刷新了一条数据", Toast.LENGTH_SHORT).show();

                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3200);
    }

}
