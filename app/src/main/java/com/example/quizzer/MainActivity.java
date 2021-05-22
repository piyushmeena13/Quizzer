package com.example.quizzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button startQuizBtn ,bookmarksBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startQuizBtn =findViewById(R.id.startQuizBtn);
        bookmarksBtn =findViewById(R.id.bookmarksBtn);


        startQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,CategoriesActivity.class);
                startActivity(intent);
            }
        });

        bookmarksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,bookmarksActivity.class);
                startActivity(intent);
            }
        });
    }
}
