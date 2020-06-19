package com.example.quixapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ResultFragment extends Fragment {

    NavController navController;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;

    TextView txt_correct,txt_wrong,txt_missed;
    TextView txt_result_percent;
    ProgressBar resultProgressBar;
    Button btnHome;

    String quizId;
    String currentUID;


    public ResultFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        navController= Navigation.findNavController(view);

        btnHome=view.findViewById(R.id.homeBtn0);
        txt_correct=view.findViewById(R.id.correctAns);
        txt_wrong=view.findViewById(R.id.wrongAns);
        txt_missed=view.findViewById(R.id.missedAns);
        txt_result_percent=view.findViewById(R.id.txt_result);
        resultProgressBar=view.findViewById(R.id.resultprogressBar);

        if (auth.getCurrentUser()!=null){
             currentUID = auth.getCurrentUser().getUid();
        }else{


        }
        quizId = QuizFragmentArgs.fromBundle(getArguments()).getQuizId();


        firebaseFirestore.collection("Quizes").document(quizId).collection("Results").document(currentUID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
           if (task.isSuccessful()){

               DocumentSnapshot res=task.getResult();
               Long correct,wrong,unanswered;

               correct=res.getLong("correct");
               wrong=res.getLong("wrong");
               unanswered=res.getLong("unanswered");

               txt_correct.setText(correct.toString());
               txt_wrong.setText(wrong.toString());
               txt_missed.setText(unanswered.toString());

               Long total=correct+wrong+unanswered;
               Long percent=(correct*100)/total;

               txt_result_percent.setText(percent+ "%");
               resultProgressBar.setProgress(percent.intValue());


           }
            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_resultFragment_to_listFragment);

            }
        });


    }
}
