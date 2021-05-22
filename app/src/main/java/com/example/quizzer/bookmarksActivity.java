package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.quizzer.questionActivity.FILE_NAME;
import static com.example.quizzer.questionActivity.KEY_NAME;

public class bookmarksActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private List<questionModel> list;
    private  RecyclerView recyclerView;

    private List<questionModel> bookmarksList;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        toolbar = findViewById(R.id.bookmarksToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bookmarks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView =findViewById(R.id.bookmarksRecyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);

        preferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor=preferences.edit();
        gson=new Gson();

        getBookmarks();

        bookmarksActivityAdapter adapter=new bookmarksActivityAdapter(bookmarksList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void  getBookmarks(){
        String json =preferences.getString(KEY_NAME,"");
        Type type =new TypeToken<List<questionModel>>(){}.getType();
        bookmarksList = gson.fromJson(json,type);

        if(bookmarksList == null){
            bookmarksList =new ArrayList<>();
        }
    }


    private void storeBookmarks(){

        String json = gson.toJson(bookmarksList);
        editor.putString(KEY_NAME,json);
        editor.commit();
    }
}
