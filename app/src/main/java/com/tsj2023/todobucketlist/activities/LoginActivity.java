package com.tsj2023.todobucketlist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.util.Utility;
import com.kakao.sdk.user.UserApiClient;
import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.databinding.ActivityLoginBinding;
import com.tsj2023.todobucketlist.network.G;

import kotlin.Unit;
import kotlin.jvm.functions.Function12;
import kotlin.jvm.functions.Function2;

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

        String keyHash= Utility.INSTANCE.getKeyHash(this);
        Log.d("keyHash",keyHash);
    }

    void clickLoginKakao(){
        Function2<OAuthToken,Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if (throwable != null){
                    Toast.makeText(LoginActivity.this, "카카오로그인 실패 : " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("error",throwable.getMessage());
                }else {
                    Toast.makeText(LoginActivity.this, "카카오로그인 성공", Toast.LENGTH_SHORT).show();

                    //사용자 정보 요청
                    UserApiClient.getInstance().me((user, throwable1) -> {
                        String nickname=user.getKakaoAccount().getProfile().getNickname();
                        String profileImage=user.getKakaoAccount().getProfile().getProfileImageUrl();

                        G.email=user.getKakaoAccount().getEmail();
                        Toast.makeText(LoginActivity.this, ""+G.email, Toast.LENGTH_SHORT).show();

//                        //main화면으로 전환
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                        return null;

                    });

                }
                return null;
            }
        };

        //카톡으로 로그인을 권장 설치되지 않았다면 카카오계정 로그인 시도
        if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(this)){
            UserApiClient.getInstance().loginWithKakaoTalk(this,callback);
        }else {
            UserApiClient.getInstance().loginWithKakaoAccount(this,callback);
        }
    }
    void clickLoginGoogle(){

    }
    void clickLoginNaver(){

    }
}