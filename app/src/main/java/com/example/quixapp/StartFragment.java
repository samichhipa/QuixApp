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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iitr.kaishu.nsidedprogressbar.NSidedProgressBar;


public class StartFragment extends Fragment {


    FirebaseAuth auth;
    TextView txt_start;
    NSidedProgressBar nSidedProgressBar;

    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view=inflater.inflate(R.layout.fragment_start, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth=FirebaseAuth.getInstance();

        navController= Navigation.findNavController(view);

        nSidedProgressBar=view.findViewById(R.id.NSidedProgressBar);
        txt_start=view.findViewById(R.id.start_txt);

        txt_start.setText("Checking User Credentials...");
    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseUser user=auth.getCurrentUser();

        if (user==null){
            txt_start.setText("Creating New Account...");
            auth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()){
                   txt_start.setText("Account Created...");
                   navController.navigate(R.id.action_startFragment_to_listFragment);





               }else{

                   Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
               }

                }
            });


        }else{
            txt_start.setText("Login...");
            navController.navigate(R.id.action_startFragment_to_listFragment);


        }


    }
}
