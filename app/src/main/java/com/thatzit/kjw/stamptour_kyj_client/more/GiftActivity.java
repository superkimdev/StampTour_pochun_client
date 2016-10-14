package com.thatzit.kjw.stamptour_kyj_client.more;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.TermsActivity;

public class GiftActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView gift_accept_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);
        setLayout();

    }

    private void setLayout() {
        gift_accept_textview = (TextView)findViewById(R.id.gift_accept_textview);
        gift_accept_textview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.join_accept_textview:
                Intent intent = new Intent(this,TermsActivity.class);
                startActivity(intent);
                break;

        }
    }
}
