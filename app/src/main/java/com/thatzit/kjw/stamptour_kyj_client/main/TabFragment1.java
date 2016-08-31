package com.thatzit.kjw.stamptour_kyj_client.main;

/**
 * Created by kjw on 16. 8. 22..
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.ListViewAdapter;
import com.thatzit.kjw.stamptour_kyj_client.main.fileReader.LoadAsyncTask;
import com.thatzit.kjw.stamptour_kyj_client.main.fileReader.ReadJson;

public class TabFragment1 extends Fragment implements AdapterView.OnItemClickListener{

    private View view;
    private ListView listview;
    private ListViewAdapter madapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        setLayout();
        return view;
    }

    private void setLayout() {
        listview = (ListView)view.findViewById(R.id.TownListView) ;
        View header = getActivity().getLayoutInflater().inflate(R.layout.townlistheader, null, false) ;

        // listView에 header추가.
        listview.addHeaderView(header) ;

        // 데이터를 지정하지 않은 adapter 생성하여 listview에 지정.
        madapter = new ListViewAdapter(view.getContext()) ;
        listview.setAdapter(madapter) ;
        listview.setOnItemClickListener(this);
        new LoadAsyncTask(madapter,getContext()).execute();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("memCashList", String.valueOf(ReadJson.memCashList.get(position-1).getName()));

    }
}