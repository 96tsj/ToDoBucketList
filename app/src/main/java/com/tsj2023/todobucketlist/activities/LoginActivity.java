package com.tsj2023.todobucketlist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvBtnNologin.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        binding.ivBtnLoginKakao.setOnClickListener(view -> clickLoginKakao());
        binding.ivBtnLoginGoogle.setOnClickListener(view -> clickLoginGoogle());
        binding.ivBtnLoginNaver.setOnClickListener(view -> clickLoginNaver());
    }

    void clickLoginKakao(){

    }
    void clickLoginGoogle(){

    }
    void clickLoginNaver(){

    }
}