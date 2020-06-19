package com.example.quixapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quixapp.Model.Quizes;
import com.example.quixapp.R;

import java.util.List;

public class AllQuizesAdapter extends RecyclerView.Adapter<AllQuizesAdapter.ViewHolder> {

    List<Quizes> quizesList;
    AllQuizOnClicklistener allQuizOnClicklistener;

    public AllQuizesAdapter(AllQuizOnClicklistener allQuizOnClicklistener) {
        this.allQuizOnClicklistener = allQuizOnClicklistener;
    }

    public void setQuizesList(List<Quizes> quizesList) {
        this.quizesList = quizesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_quiz_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Quizes quizes=quizesList.get(position);



        Glide.with(holder.itemView.getContext()).load(quizes.getQuiz_image()).into(holder.quiz_image);

        String description=quizes.getQuiz_description();
        if (description.length()>150){

            description=description.substring(0,150);
        }

        holder.txt_quiz_title.setText("Quiz Name: "+quizes.getQuiz_title());
        holder.txt_quiz_desc.setText("About: "+description);
        holder.txt_quiz_level.setText("Level: "+quizes.getQuiz_level());


    }



    @Override
    public int getItemCount() {
        if (quizesList == null) {

            return 0;
        } else {


            return quizesList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView quiz_image;
        TextView txt_quiz_title,txt_quiz_desc,txt_quiz_level;
        Button btn_view_quiz;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            quiz_image=itemView.findViewById(R.id.quiz_image);
            txt_quiz_title=itemView.findViewById(R.id.quiz_title);
            txt_quiz_desc=itemView.findViewById(R.id.quiz_description);
            txt_quiz_level=itemView.findViewById(R.id.quiz_level);

            btn_view_quiz=itemView.findViewById(R.id.view_quiz_btn);
            btn_view_quiz.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            allQuizOnClicklistener.onItemClick(getAdapterPosition());

        }
    }
    public interface AllQuizOnClicklistener{

        void onItemClick(int position);


    }
}
