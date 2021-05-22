package com.example.quizzer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class setsGridviewAdapter extends BaseAdapter {

    private int sets=0;
    private String category;

    public setsGridviewAdapter(int sets, String category)
    {
        this.sets = sets;
        this.category=category;
    }

    public setsGridviewAdapter(List<categoriesModel> list) {

    }

    @Override
    public int getCount() {
        return sets;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View view;

        if(convertView==null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sets_item,parent,false);
        }else {
            view = convertView;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent questionIntent =new Intent(parent.getContext(),questionActivity.class);
                questionIntent.putExtra("category",category);
                questionIntent.putExtra("setNo",sets);
                parent.getContext().startActivity(questionIntent);
            }
        });

        ((TextView)view.findViewById(R.id.setsNumberTV)).setText(String.valueOf(position+1));

        return view;
    }
}
