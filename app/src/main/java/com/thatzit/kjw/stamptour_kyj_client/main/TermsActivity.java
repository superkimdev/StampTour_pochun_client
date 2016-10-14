package com.thatzit.kjw.stamptour_kyj_client.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.thatzit.kjw.stamptour_kyj_client.R;


public class TermsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        setLayout();
    }

    private void setLayout() {
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back)finish();
    }
}
