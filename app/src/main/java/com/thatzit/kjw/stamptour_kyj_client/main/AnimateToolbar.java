package com.thatzit.kjw.stamptour_kyj_client.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.fileReader.LoadAsyncTask;


public class AnimateToolbar extends Fragment implements SimpleRecyclerAdapter.OnItemClickListener, View.OnClickListener {
    CollapsingToolbarLayout collapsingToolbar;
    RecyclerView recyclerView;
    int mutedColor = R.attr.colorPrimary;
    SimpleRecyclerAdapter simpleRecyclerAdapter;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_animate_toolbar, container, false);
        setLayout();
        return view;
    }
    private void setLayout(){
        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.anim_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Suleiman Ali Shakir");


        ImageView header = (ImageView) view.findViewById(R.id.header);
        header.setOnClickListener(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.header);

//        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//            @SuppressWarnings("ResourceType")
//            @Override
//            public void onGenerated(Palette palette) {
//
//                mutedColor = palette.getMutedColor(R.color.primary_500);
//                collapsingToolbar.setContentScrimColor(mutedColor);
//                collapsingToolbar.setStatusBarScrimColor(R.color.black_trans80);
//            }
//        });

        recyclerView = (RecyclerView) view.findViewById(R.id.scrollableview);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        simpleRecyclerAdapter = new SimpleRecyclerAdapter(view.getContext());
        recyclerView.setAdapter(simpleRecyclerAdapter);
        simpleRecyclerAdapter.SetOnItemClickListener(this);
        new LoadAsyncTask(simpleRecyclerAdapter,getActivity()).execute();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                ((AppCompatActivity)getActivity()).finish();
//                return true;
//            case R.id.action_settings:
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.e("Recycleitem","position = "+position);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.header){
            Toast.makeText(getContext(),"이미지클릭",Toast.LENGTH_LONG).show();
        }
    }
}
