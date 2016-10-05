package com.thatzit.kjw.stamptour_kyj_client.main;

/**
 * Created by kjw on 16. 8. 22..
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.thatzit.kjw.stamptour_kyj_client.R;

public class RankingFragment extends Fragment {

    private View view;
    private final String TAG = "RankingFragment";
    private ProgressBar list_progressbar;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.rank_fragment, container, false);
        setLayout();
        return view;
    }

    private void setLayout() {
        toolbar = (Toolbar) view.findViewById(R.id.rank_toolbar);
        list_progressbar = (ProgressBar) view.findViewById(R.id.list_progressbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }
}