package com.tsj2023.todobucketlist.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tsj2023.todobucketlist.activities.LoginActivity;
import com.tsj2023.todobucketlist.databinding.FragmentCompleteNotLoginBinding;

public class FragmentCompleteNotLogin extends Fragment {

    FragmentCompleteNotLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentCompleteNotLoginBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.goLogin.setOnClickListener(view1 -> clickBtn());
    }

    void clickBtn(){
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }
}
