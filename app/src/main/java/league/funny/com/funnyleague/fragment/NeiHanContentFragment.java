package league.funny.com.funnyleague.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import league.funny.com.funnyleague.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NeiHanContentFragment extends Fragment {


    public NeiHanContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content_neihan, container, false);
    }

}
