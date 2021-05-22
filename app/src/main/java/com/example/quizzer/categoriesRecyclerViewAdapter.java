package com.example.quizzer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class categoriesRecyclerViewAdapter extends RecyclerView.Adapter<categoriesRecyclerViewAdapter.viewHolder> {

    private List<categoriesModel> categoriesModelList;

    public categoriesRecyclerViewAdapter(List<categoriesModel> categoriesModelList) {
        this.categoriesModelList = categoriesModelList;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_item,parent,false);
        viewHolder holder =new viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.setData(categoriesModelList.get(position).getUrl(),categoriesModelList.get(position).getName(),categoriesModelList.get(position).getSets());
    }

    @Override
    public int getItemCount() {
        return categoriesModelList.size();
    }

    public  class viewHolder extends RecyclerView.ViewHolder {

        private CircleImageView circlularImageView;
        private TextView tittleTextview;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            circlularImageView =itemView.findViewById(R.id.circularImageview);
            tittleTextview=itemView.findViewById(R.id.tittleTextview);
        }

        private void setData(String url, final String tittleTextview,final int sets){
            Glide.with(itemView.getContext()).load(url).into(circlularImageView);
            this.tittleTextview.setText(tittleTextview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent setIntent= new Intent(itemView.getContext(),SetsActivity.class);
                    setIntent.putExtra("tittle",tittleTextview);
                    setIntent.putExtra("sets",sets);
                    itemView.getContext().startActivity(setIntent);
                }
            });
        }
    }
}