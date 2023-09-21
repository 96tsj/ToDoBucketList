package com.tsj2023.todobucketlist.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.activities.MainActivity;
import com.tsj2023.todobucketlist.adapters.TodoRecyclerAdapter;
import com.tsj2023.todobucketlist.data.TodoItem;
import com.tsj2023.todobucketlist.databinding.FragmentTodoBinding;

import java.util.ArrayList;

public class FragmentTodo extends Fragment{

    ArrayList<TodoItem> todoItems = new ArrayList<>();
    FragmentTodoBinding binding;
    TodoRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentTodoBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        todoItems.add(new TodoItem("실험용 자료추가",true));


        adapter = new TodoRecyclerAdapter(getContext(),todoItems);
        binding.todoRecyclerView.setAdapter(adapter);
        //binding.todoRecyclerView.setAdapter(adapter);

    }

}