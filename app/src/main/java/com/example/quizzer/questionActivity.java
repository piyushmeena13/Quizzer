package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class questionActivity extends AppCompatActivity {

    public static final String FILE_NAME ="QUIZZER";
    public static final String KEY_NAME ="QUESTION";

    FirebaseDatabase database =FirebaseDatabase.getInstance();
    DatabaseReference myRef =database.getReference();

    private static final String TAG = "questionActivity";
    private TextView questionTextview,questionNumberTv;
    private FloatingActionButton floatingBookmarksBtn;
    private LinearLayout optionContainer;
    private Button btnShare,btnNext;
    private int count=0;
    private List<questionModel> questionModelList;
    private int positon = 0;
    private int score = 0;
    private  String category;
    private  int setNo;
    private Dialog loadingDialog;

    private List<questionModel> bookmarksList;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private  int matchedQuestionPosition;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Toolbar toolbar =findViewById(R.id.questionToolbar);
        setSupportActionBar(toolbar);

        questionTextview =findViewById(R.id.questionTextview);
        questionNumberTv =findViewById(R.id.questionNumberTv);
        floatingBookmarksBtn =findViewById(R.id.floatingBookmarksBtn);
        optionContainer =findViewById(R.id.optionContainer);
        btnShare =findViewById(R.id.btnShare);
        btnNext =findViewById(R.id.btnNext);

        preferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor=preferences.edit();
        gson=new Gson();

        getBookmarks();

        floatingBookmarksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modelMatch()){
                    bookmarksList.remove(matchedQuestionPosition);
                    floatingBookmarksBtn.setImageDrawable(getDrawable(R.drawable.ic_bookmark_border));
                }else {
                    bookmarksList.add(questionModelList.get(positon));
                    floatingBookmarksBtn.setImageDrawable(getDrawable(R.drawable.ic_bookmarked));
                }
            }
        });

        category=getIntent().getStringExtra("category");
        setNo=getIntent().getIntExtra("setNo",1);

        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.corner_rounded_button));
        loadingDialog.setCancelable(false);


        questionModelList = new ArrayList<>();

        loadingDialog.show();
        myRef.child("SETS").child(category).child("questions").orderByChild("setNo").equalTo(setNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    questionModelList.add(dataSnapshot1.getValue(questionModel.class));
                }
                if(questionModelList.size() >  0 ){

                    for(int i=0;i<4;i++){
                        optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkAnswer((Button) v);
                            }
                        });
                    }
                    questionAnimation(questionTextview,0,questionModelList.get(positon).getQuestion());
                    btnNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btnNext.setEnabled(false);
                            btnNext.setAlpha(0.5f);
                            positon++;
                            enableoption(true);
                            if(positon == questionModelList.size()){
                                ///scoreActivity

                                Intent scoreIntent =new Intent(questionActivity.this,scoreActivity.class);
                                scoreIntent.putExtra("score",score);
                                scoreIntent.putExtra("total",questionModelList.size());
                                startActivity(scoreIntent);
                                finish();
                                return;
                            }
                            count=0;
                            questionAnimation(questionTextview,0,questionModelList.get(positon).getQuestion());
                        }
                    });

                    btnShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String body =questionModelList.get(positon).getQuestion() + "\n" +
                                    questionModelList.get(positon).getOptionA() + "\n" +
                                    questionModelList.get(positon).getOptionB() + "\n" +
                                    questionModelList.get(positon).getOptionC() + "\n" +
                                    questionModelList.get(positon).getOptionD();
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Quizzer challenge");
                            shareIntent.putExtra(Intent.EXTRA_TEXT,body);
                            startActivity(Intent.createChooser(shareIntent,"Share via"));
                        }
                    });

                }else{
                    finish();
                    Toast.makeText(questionActivity.this, "no question", Toast.LENGTH_SHORT).show();
                }

                loadingDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(questionActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                finish();
            }
        });



    }

    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }

    private void questionAnimation (final View view, final int value, final String data){
        Log.d(TAG, "questionAnimation: called");
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.d(TAG, "onAnimationStart: start");
                if (value == 0 && count<4){
                    String option = "";
                    if(count == 0){
                        option=questionModelList.get(positon).getOptionA();
                    }else if(count ==1){
                        option=questionModelList.get(positon).getOptionB();
                    }else if(count ==2){
                        option=questionModelList.get(positon).getOptionC();
                    }else if(count ==3){
                        option=questionModelList.get(positon).getOptionD();
                    }
                    questionAnimation(optionContainer.getChildAt(count),0,option);
                    count++;
                }
            }



            @SuppressLint("NewApi")
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "onAnimationEnd: start");
                if (value ==0){
                    try{
                        ((TextView)view).setText((data));
                        questionNumberTv.setText(positon+1+"/"+questionModelList.size());
                        if(modelMatch()){
                            floatingBookmarksBtn.setImageDrawable(getDrawable(R.drawable.ic_bookmarked));
                        }else {
                            floatingBookmarksBtn.setImageDrawable(getDrawable(R.drawable.ic_bookmark_border));
                        }
                    }catch (ClassCastException ex){
                        ((Button)view).setText((data));
                    }
                    view.setTag(data);
                    questionAnimation(view,1,data);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    @SuppressLint("NewApi")
    private void checkAnswer(Button selectedOption){
        Log.d(TAG, "checkAnswer: called");
        enableoption(false);
        btnNext.setEnabled(true);
        btnNext.setAlpha(1);
        if(selectedOption.getText().toString().equals(questionModelList.get(positon).getCorrectAns())){
            /// correct => color change
            score++;
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }else {
            /// incorrectoption
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
            Button correctOption=  (Button) optionContainer.findViewWithTag(questionModelList.get(positon).getCorrectAns());
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }
    }
    @SuppressLint("NewApi")
    private void enableoption(boolean enable){
        for(int i=0;i<4;i++){
            optionContainer.getChildAt(i).setEnabled(enable);
            if(enable){
                optionContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }
        }
    }

    private void  getBookmarks(){
        String json =preferences.getString(KEY_NAME,"");
        Type type =new TypeToken<List<questionModel>>(){}.getType();
        bookmarksList = gson.fromJson(json,type);

        if(bookmarksList == null){
            bookmarksList =new ArrayList<>();
        }
    }

    private boolean modelMatch(){
        boolean matced = false;
        int i=0;
        for (questionModel model : bookmarksList){
            if (model.getQuestion().equals(questionModelList.get(positon).getQuestion())
            && model.getCorrectAns().equals(questionModelList.get(positon).getCorrectAns())
            && model.getSetNo() == questionModelList.get(positon).getSetNo()){

                matced=true;
                matchedQuestionPosition=i;
            }
            i++;
        }
        return matced;
    }

    private void storeBookmarks(){

        String json = gson.toJson(bookmarksList);
        editor.putString(KEY_NAME,json);
        editor.commit();
    }
}