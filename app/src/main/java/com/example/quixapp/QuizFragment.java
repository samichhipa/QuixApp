package com.example.quixapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quixapp.Model.Questions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;


public class QuizFragment extends Fragment implements View.OnClickListener {

    NavController navController;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    String quizId,currentUID,quizName;
    List<Questions> questionsList = new ArrayList<>();
    List<Questions> questionToanswer = new ArrayList<>();
    long totalQuestionToAnswer = 0L;
    boolean canAnswer = false;
    int currentQuestion = 0;
    CountDownTimer countDownTimer;
    int correctAns=0,wrongAns=0,notAns;

    TextView txt_question_no, txt_question, quiz_title, txt_answer, progress_txt;
    Button option_a, option_b, option_c, next_btn;
    ProgressBar progressBar;


    public QuizFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null){
            currentUID=auth.getCurrentUser().getUid();
        }else{


        }

        txt_question_no = view.findViewById(R.id.quiz_question_no);
        txt_question = view.findViewById(R.id.quiz_quiz_questions);
        txt_answer = view.findViewById(R.id.quiz_answer);
        option_a = view.findViewById(R.id.quiz_option_a);
        option_b = view.findViewById(R.id.quiz_option_b);
        option_c = view.findViewById(R.id.quiz_option_c);
        quiz_title = view.findViewById(R.id.quiz_quiz_title);
        progressBar = view.findViewById(R.id.progressBar);
        next_btn = view.findViewById(R.id.quiz_nextBtn);
        progress_txt = view.findViewById(R.id.txt_time);

        navController = Navigation.findNavController(view);

        quizId = QuizFragmentArgs.fromBundle(getArguments()).getQuizId();
        totalQuestionToAnswer = QuizFragmentArgs.fromBundle(getArguments()).getTotalQuestions();
        quizName=QuizFragmentArgs.fromBundle(getArguments()).getQuizName();



        //get All questions//
        firebaseFirestore.collection("Quizes").document(quizId).collection("Questions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    questionsList = task.getResult().toObjects(Questions.class);

                    pickQuestions();
                    loadUi();


                } else {

                    Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                }


            }
        });

        option_a.setOnClickListener(this);
        option_b.setOnClickListener(this);
        option_c.setOnClickListener(this);
        next_btn.setOnClickListener(this);
    }

    private void loadUi() {

        quiz_title.setText(quizName);
        txt_question.setText("Load First Question");

        enableOptions();

        loadQuestions(1);
    }

    private void loadQuestions(int questionNo) {

        txt_question_no.setText(String.valueOf(questionNo));

        txt_question.setText(questionToanswer.get(questionNo - 1).getQuestion());

        option_a.setText(questionToanswer.get(questionNo - 1).getOption_a());
        option_b.setText(questionToanswer.get(questionNo - 1).getOption_b());
        option_c.setText(questionToanswer.get(questionNo - 1).getOption_c());


        canAnswer = true;
        currentQuestion = questionNo;


        //start timer//
        startTimer(questionNo);

    }

    private void startTimer(int questionNo) {

        final Long timerToAnswer = questionToanswer.get(questionNo - 1).getTime();
        progress_txt.setText(timerToAnswer.toString());


        //show time progress//
        progressBar.setVisibility(View.VISIBLE);

        countDownTimer = new CountDownTimer(timerToAnswer * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {


                progress_txt.setText(millisUntilFinished / 1000 + "");


                Long percent = millisUntilFinished / (timerToAnswer * 10);
                progressBar.setProgress(percent.intValue());

            }

            @Override
            public void onFinish() {

                canAnswer=false;
                notAns++;
                txt_answer.setText("Time Up! No Answer selected..");
                txt_answer.setTextColor(getResources().getColor(R.color.colorPrimary,null));
                showMextBtn();

            }
        };

        countDownTimer.start();


    }


    private void enableOptions() {

        option_a.setVisibility(View.VISIBLE);
        option_b.setVisibility(View.VISIBLE);
        option_b.setVisibility(View.VISIBLE);


        option_a.setEnabled(true);
        option_b.setEnabled(true);
        option_c.setEnabled(true);

        txt_answer.setVisibility(View.INVISIBLE);
        next_btn.setVisibility(View.INVISIBLE);
        next_btn.setEnabled(false);


    }

    private void pickQuestions() {

        for (int i = 0; i < totalQuestionToAnswer; i++) {

            int random = getRandomInteger(questionsList.size(), 0);
            questionToanswer.add(questionsList.get(random));
            questionsList.remove(random);

            Log.d("TAG", "Question :" + i + " " + questionToanswer.get(i).getQuestion());
        }

    }


    public static int getRandomInteger(int max, int min) {
        return (int) ((Math.random() * (max - min))) + min;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.quiz_option_a:
                verifyAnswer(option_a);

                break;
            case R.id.quiz_option_b:
                verifyAnswer(option_b);

                break;
            case R.id.quiz_option_c:
                verifyAnswer(option_c);

                break;

            case R.id.quiz_nextBtn:
                if (currentQuestion==totalQuestionToAnswer){

                   submitResults();
                }else {
                    currentQuestion++;
                    loadQuestions(currentQuestion);
                    resetOptions();

                }

                break;

        }
    }

    private void submitResults() {


        HashMap<String,Object> data=new HashMap<>();
        data.put("correct",correctAns);
        data.put("wrong",wrongAns);
        data.put("unanswered",notAns);

        firebaseFirestore.collection("Quizes").document(quizId).collection("Results")
                .document(currentUID).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                   /* DetailsFragmentDirections.ActionDetailsFragmentToQuizFragment action = DetailsFragmentDirections.actionDetailsFragmentToQuizFragment();
                    action.setTotalQuestions(total_questions);
                    action.setQuizId(quiz_id);
                    action.setQuizName(quizName);
                    navController.navigate(action);


                    */

                   QuizFragmentDirections.ActionQuizFragmentToResultFragment action=QuizFragmentDirections.actionQuizFragmentToResultFragment();
                   action.setQuizId(quizId);
                   navController.navigate(action);

                }else{

                    Toast.makeText(getActivity(),task.getException().toString(),Toast.LENGTH_SHORT).show();

                }
            }
        });




    }

    private void resetOptions() {
        option_a.setBackground(getResources().getDrawable(R.drawable.btn_bg,null));
        option_b.setBackground(getResources().getDrawable(R.drawable.btn_bg,null));
        option_c.setBackground(getResources().getDrawable(R.drawable.btn_bg,null));


        txt_answer.setVisibility(View.INVISIBLE);
        next_btn.setVisibility(View.INVISIBLE);
        next_btn.setEnabled(false);
    }

    private void verifyAnswer(Button selectedButton) {



        if (canAnswer) {
            selectedButton.setBackground(getResources().getDrawable(R.drawable.btn_bg,null));
            if (questionToanswer.get(currentQuestion - 1).getAnswer().equals(selectedButton.getText())) {

                selectedButton.setBackground(getResources().getDrawable(R.drawable.correct_btn_bg,null));
                correctAns++;

                txt_answer.setTextColor(getResources().getColor(R.color.colorPrimary,null));
                txt_answer.setText("Correct Answer...");


            } else {
                wrongAns++;
                selectedButton.setBackground(getResources().getDrawable(R.drawable.wrong_btn_bg,null));

                txt_answer.setTextColor(getResources().getColor(R.color.redColor,null));
                txt_answer.setText("Wrong Answer "+questionToanswer.get(currentQuestion-1).getAnswer());

            }

            canAnswer = false;

            countDownTimer.cancel();


            showMextBtn();


        }
    }

    private void showMextBtn() {

        if (currentQuestion==totalQuestionToAnswer){

            next_btn.setText("Submit Results");
        }

        txt_answer.setVisibility(View.VISIBLE);
        next_btn.setVisibility(View.VISIBLE);
        next_btn.setEnabled(true);



    }


}
