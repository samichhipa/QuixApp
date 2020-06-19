package com.example.quixapp.Repository;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.quixapp.Model.Quizes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FirebaseRepository {

    private OnFirebaseTaskComplete onFirebaseTaskComplete;

    public FirebaseRepository(OnFirebaseTaskComplete onFirebaseTaskComplete) {
        this.onFirebaseTaskComplete = onFirebaseTaskComplete;
    }

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference reference = firebaseFirestore.collection("Quizes");

    public void getAllQuizes() {

        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                        onFirebaseTaskComplete.quizListDataAdded(task.getResult().toObjects(Quizes.class));



                } else {

                    onFirebaseTaskComplete.onError(task.getException());

                }
            }
        });


    }



    public interface OnFirebaseTaskComplete{

        void quizListDataAdded(List<Quizes> quizesList);

        void onError(Exception e);


    }
}
