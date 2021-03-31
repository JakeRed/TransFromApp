package com.transfromapp.mass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        long result = calFactorial(100);
        TextView tv_result = findViewById(R.id.tv_result);
        tv_result.setText(String.valueOf(result));
    }

    /**
     * 计算阶乘
     * @param number
     * @return
     */
    private long calFactorial(int number) {
        long result = 0;
        for (int i = 1; i < number;i++){
            result = result + i;
        }
        return result;
    }
}