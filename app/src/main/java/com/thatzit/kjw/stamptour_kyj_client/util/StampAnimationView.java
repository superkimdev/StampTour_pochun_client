package com.thatzit.kjw.stamptour_kyj_client.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.MainRecyclerAdapter;
import com.thatzit.kjw.stamptour_kyj_client.main.msgListener.StampSealListnenr;


/**
 * Created by csc-pc on 2016. 10. 24..
 */

public class StampAnimationView extends Dialog implements View.OnClickListener{
    private Context context;
    private ImageView waitAnimation;
    private AnimationDrawable frameAnimation;
    private int position;
    private StampSealListnenr listnenr;
    private final String TAG ="MainRecyclerAdapter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        lpWindow.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lpWindow.width = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_stamp_seal);
        setAnimationWait();
        Log.e("daaaaaaaaa","onCreate()");
    }

    public void setAnimationWait() {
        Log.e("daaaaaaaaa","setAnimationWait()");
        waitAnimation = (ImageView)findViewById(R.id.dialog_stamp_seal_imageview);
        waitAnimation.setVisibility(View.VISIBLE);
        waitAnimation.setOnClickListener(this);
        waitAnimation.setBackgroundResource(R.drawable.animationlist);
        frameAnimation = (AnimationDrawable)waitAnimation.getBackground();
        waitAnimation.animate().alpha(1);
        startAnimation();
    }

    public void startAnimation(){
        if(frameAnimation.isRunning()){
            frameAnimation.stop();
        }

        frameAnimation.start();
    }


    public void stopAnimation(){
        if(frameAnimation.isRunning()){
            frameAnimation.stop();
        }

    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        waitAnimation.setVisibility(View.GONE);
        frameAnimation.stop();
    }
    public StampAnimationView(Context context, int position) {
        super(context,android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_stamp_seal_imageview:
                listnenr.OnStampASeal(position);
                dismiss();
                break;

        }
    }


    public void SetOnStampASealListener(StampSealListnenr listnenr) {
        this.listnenr = listnenr;
    }
}
