package com.example.quixapp.ViewModel;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.quixapp.Model.Quizes;
import com.example.quixapp.Repository.FirebaseRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AllQuizesViewModel extends ViewModel implements FirebaseRepository.OnFirebaseTaskComplete{


    MutableLiveData<List<Quizes>> quizListModelData=new MutableLiveData<>();

    public LiveData<List<Quizes>> getQuizListModelData() {
        return quizListModelData;
    }


    FirebaseRepository firebaseRepository=new FirebaseRepository(this);

    public AllQuizesViewModel() {
        firebaseRepository.getAllQuizes();
    }

    @Override
    public void quizListDataAdded(List<Quizes> quizesList) {

        quizListModelData.setValue(quizesList);



    }

    @Override
    public void onError(Exception e) {

    }
}
