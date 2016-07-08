package com.kuoruan.slideswitch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.kuoruan.slideswitch.ui.SlideSwitch;
import com.kuoruan.slideswitch.ui.SlideSwitch.OnSwitchStateChangeListener;

public class MainActivity extends AppCompatActivity {

    private SlideSwitch ss_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ss_main = (SlideSwitch) findViewById(R.id.ss_main);
        ss_main.setSwitchState(true);
        ss_main.setOnSwitchStateChangeListener(new OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(View view, boolean state) {
                Toast.makeText(MainActivity.this, "当前状态：" + state, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
