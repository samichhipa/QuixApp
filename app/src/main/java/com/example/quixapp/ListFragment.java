package com.example.quixapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.quixapp.Adapter.AllQuizesAdapter;
import com.example.quixapp.Model.Quizes;
import com.example.quixapp.ViewModel.AllQuizesViewModel;
import com.iitr.kaishu.nsidedprogressbar.NSidedProgressBar;

import java.util.List;


public class ListFragment extends Fragment implements AllQuizesAdapter.AllQuizOnClicklistener {

    RecyclerView recyclerView;
    AllQuizesViewModel allQuizesViewModel;
    AllQuizesAdapter adapter;
    NSidedProgressBar progressBar;
    Animation fade_in, fade_out;
    NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_list, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.all_quiz_recyclerview);
        progressBar = view.findViewById(R.id.list_progressbar);
        adapter = new AllQuizesAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        fade_in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        allQuizesViewModel = new ViewModelProvider(getActivity()).get(AllQuizesViewModel.class);

        allQuizesViewModel.getQuizListModelData().observe(getViewLifecycleOwner(), new Observer<List<Quizes>>() {
            @Override
            public void onChanged(List<Quizes> quizesList) {

                recyclerView.startAnimation(fade_in);
                progressBar.startAnimation(fade_out);


                adapter.setQuizesList(quizesList);
                adapter.notifyDataSetChanged();

            }
        });


    }

    @Override
    public void onItemClick(int position) {

        ListFragmentDirections.ActionListFragmentToDetailsFragment action = ListFragmentDirections.actionListFragmentToDetailsFragment();
        action.setPosition(position);
        navController.navigate(action);


    }
}
