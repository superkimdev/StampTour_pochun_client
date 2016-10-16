package com.thatzit.kjw.stamptour_kyj_client.more;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.TermsActivity;

public class GiftActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView gift_accept_textview;
    private EditText gift_name_input_text;
    private EditText gift_phone_input_text;
    private Button gift_sendjoin_btn;
    private ImageButton gift_toolbar_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);
        setLayout();

    }

    private void setLayout() {
        gift_accept_textview = (TextView)findViewById(R.id.gift_accept_textview);
        gift_name_input_text = (EditText)findViewById(R.id.gift_name_input_text);
        gift_phone_input_text = (EditText)findViewById(R.id.gift_phone_input_text);
        gift_sendjoin_btn = (Button)findViewById(R.id.gift_sendjoin_btn);
        gift_toolbar_back = (ImageButton)findViewById(R.id.gift_toolbar_back);

        gift_accept_textview.setOnClickListener(this);
        gift_sendjoin_btn.setOnClickListener(this);
        gift_toolbar_back.setOnClickListener(this);
    }

    void send(){

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.gift_accept_textview:
                Intent intent = new Intent(GiftActivity.this,TermsActivity.class);
                startActivity(intent);
                break;
            case R.id.gift_sendjoin_btn:
                send();
                Toast.makeText(this,"선물신청",Toast.LENGTH_SHORT).show();
                break;
            case R.id.gift_toolbar_back:
                finish();
                break;

        }
    }
}
