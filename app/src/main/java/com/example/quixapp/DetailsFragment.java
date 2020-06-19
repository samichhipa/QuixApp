package com.example.quixapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quixapp.Model.Quizes;
import com.example.quixapp.ViewModel.AllQuizesViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class DetailsFragment extends Fragment implements View.OnClickListener {

    NavController navController;
    AllQuizesViewModel allQuizesViewModel;
    TextView txt_quiz_title, quiz_desc, quiz_level, quiz_questions,txt_last_score;
    ImageView quiz_image;
    int position;
    FloatingActionButton btnStarQuiz;
    String quiz_id,quizName;
    long total_questions;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_details, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        navController = Navigation.findNavController(view);
        auth=FirebaseAuth.getInstance();

        position = DetailsFragmentArgs.fromBundle(getArguments()).getPosition();

        txt_quiz_title = view.findViewById(R.id.detail_quiz_title);
        quiz_image = view.findViewById(R.id.detail_image);
        quiz_desc = view.findViewById(R.id.detail_quiz_desc);
        quiz_level = view.findViewById(R.id.detail_quiz_level);
        quiz_questions = view.findViewById(R.id.detail_quiz_questions);
        btnStarQuiz = view.findViewById(R.id.star_quiz_btn);
        txt_last_score=view.findViewById(R.id.lastScore);

        btnStarQuiz.setOnClickListener(this);


        Toast.makeText(getContext(), "Position:" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        allQuizesViewModel = new ViewModelProvider(getActivity()).get(AllQuizesViewModel.class);
        allQuizesViewModel.getQuizListModelData().observe(getViewLifecycleOwner(), new Observer<List<Quizes>>() {
            @Override
            public void onChanged(List<Quizes> quizesList) {

                Quizes quizes = quizesList.get(position);

                quiz_id=quizes.getQuiz_id();
                quizName=quizes.getQuiz_title();
                total_questions=quizes.getQuiz_questions();
                txt_quiz_title.setText(quizes.getQuiz_title());
                Glide.with(getActivity()).load(quizes.getQuiz_image()).into(quiz_image);
                quiz_desc.setText(quizes.getQuiz_description());
                quiz_level.setText(quizes.getQuiz_level());
                quiz_questions.setText(String.valueOf(quizes.getQuiz_questions()));

                loadResultData();



            }
        });


    }

    private void loadResultData() {

        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Quizes").document(quiz_id).collection("Results").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    DocumentSnapshot res=task.getResult();

                    if (res!=null && res.exists()){



                    Long correct,wrong,unanswered;

                    correct=res.getLong("correct");
                    wrong=res.getLong("wrong");
                    unanswered=res.getLong("unanswered");



                    Long total=correct+wrong+unanswered;
                    Long percent=(correct*100)/total;

                    txt_last_score.setText(String.valueOf(percent));

                    }else{


                    }


                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.star_quiz_btn) {
            DetailsFragmentDirections.ActionDetailsFragmentToQuizFragment action = DetailsFragmentDirections.actionDetailsFragmentToQuizFragment();
            action.setTotalQuestions(total_questions);
            action.setQuizId(quiz_id);
            action.setQuizName(quizName);
            navController.navigate(action);

        }
    }
}
