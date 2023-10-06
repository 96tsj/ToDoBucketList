package com.tsj2023.todobucketlist.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.adapters.CompleteRecyclerAdapter;
import com.tsj2023.todobucketlist.data.CompleteItem;
import com.tsj2023.todobucketlist.databinding.FragmentCompleteBinding;
import com.tsj2023.todobucketlist.databinding.FragmentTodoBinding;

import java.util.ArrayList;

public class FragmentComplete extends Fragment{

    ArrayList<CompleteItem> completeItems = new ArrayList<>();
    FragmentCompleteBinding binding;
    CompleteRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentCompleteBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        completeItems.add(new CompleteItem("",R.drawable.app_logo_my+""));

        adapter=new CompleteRecyclerAdapter(getContext(),completeItems);
        binding.completeRecyclerView.setAdapter(adapter);

    }
}