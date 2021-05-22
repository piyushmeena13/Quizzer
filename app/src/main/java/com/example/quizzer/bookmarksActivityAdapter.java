package com.example.quizzer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class bookmarksActivityAdapter extends RecyclerView.Adapter<bookmarksActivityAdapter.viewHolder> {

    private List<questionModel> list=new ArrayList<>();

    public bookmarksActivityAdapter(List<questionModel> list) {
        this.list=list;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmarks_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.setdata(list.get(position).getQuestion(),list.get(position).getCorrectAns(),position );

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView bookmarksQuesTV,bookmarksAnsTV;
        private ImageButton btnDelete;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            btnDelete=itemView.findViewById(R.id.btnDelete);
            bookmarksQuesTV=itemView.findViewById(R.id.bookmarksQuesTV);
            bookmarksAnsTV=itemView.findViewById(R.id.bookmarksAnsTV);

        }

        private void setdata(String question, String answer, final int position){
            this.bookmarksQuesTV.setText(question);
            this.bookmarksAnsTV.setText(answer);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
