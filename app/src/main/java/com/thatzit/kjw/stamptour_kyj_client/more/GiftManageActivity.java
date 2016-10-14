package com.thatzit.kjw.stamptour_kyj_client.more;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageButton;

import com.thatzit.kjw.stamptour_kyj_client.R;

import java.util.ArrayList;

public class GiftManageActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView gift_manage_recyclerview;
    private ImageButton gift_manage_toolbar_back;
    private ImageButton gift_manage_toolbar_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_manage);
        setLayout();
        setInitData();
    }

    private void setInitData() {

    }

    private void setLayout() {
        gift_manage_toolbar_back = (ImageButton) findViewById(R.id.gift_manage_toolbar_back);
        gift_manage_toolbar_done = (ImageButton) findViewById(R.id.gift_manage_toolbar_done);
        gift_manage_recyclerview = (RecyclerView) findViewById(R.id.gift_manage_recyclerview);


        gift_manage_toolbar_back.setOnClickListener(this);
        gift_manage_toolbar_done.setOnClickListener(this);
        setAdapter(gift_manage_recyclerview);

    }

    private void setAdapter(RecyclerView recyclerView){
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter Adapter;

        RecyclerView.LayoutManager layoutManager;
        // Item 리스트에 아이템 객체 넣기
        ArrayList<GiftDTO> items = new ArrayList<>();

        items.add(new GiftDTO("성골 선물","선물받기까지 10개 스탬프 남았습니", "0"));
        items.add(new GiftDTO("진골 선물","눌러서 선물을 신청하세", "1"));
        items.add(new GiftDTO("백골 선물","선물 신청 완료", "2"));


        // StaggeredGrid 레이아웃을 사용한다
        layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //layoutManager = new LinearLayoutManager(this);
        //layoutManager = new GridLayoutManager(this,3);

        // 지정된 레이아웃매니저를 RecyclerView에 Set 해주어야한다.
        recyclerView.setLayoutManager(layoutManager);

        Adapter = new GiftRecyclerViewAdapter(items,this);
        recyclerView.setAdapter(Adapter);
    }

    @Override
    public void onClick(View v) {

    }
}
