package com.example.quizzer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class scoreActivity extends AppCompatActivity {


    private TextView scoreTv,totalTv;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scoreTv=findViewById(R.id.scoreTv);
        totalTv =findViewById(R.id.totalTv);
        btnDone=findViewById(R.id.btnDone);

        scoreTv.setText(String.valueOf(getIntent().getIntExtra("score",0)));
        totalTv.setText(String.valueOf(getIntent().getIntExtra("total",0)));


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
