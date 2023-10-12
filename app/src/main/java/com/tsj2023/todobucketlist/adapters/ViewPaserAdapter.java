package com.tsj2023.todobucketlist.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tsj2023.todobucketlist.activities.MainActivity;
import com.tsj2023.todobucketlist.fragments.FragmentBucketList;
import com.tsj2023.todobucketlist.fragments.FragmentComplete;
import com.tsj2023.todobucketlist.fragments.FragmentCompleteNotLogin;
import com.tsj2023.todobucketlist.fragments.FragmentSchedule;
import com.tsj2023.todobucketlist.fragments.FragmentTodo;

public class ViewPaserAdapter extends FragmentStateAdapter {

    Fragment[] fragments = new Fragment[4];
    MainActivity mainActivity;



    public ViewPaserAdapter(@NonNull FragmentActivity fragmentActivity, MainActivity mainActivity){
        super(fragmentActivity);
        this.mainActivity = mainActivity;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new FragmentTodo();
            case 1:
                return new FragmentSchedule();
            case 2:
                return new FragmentBucketList();
            case 3:
                if (isTokenAvailable()) {
                    return new FragmentComplete();
                } else {
                    return new FragmentCompleteNotLogin();
                }
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }
    private boolean isTokenAvailable() {
        SharedPreferences preferences = mainActivity.getSharedPreferences("LoginToken", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        Log.d("TokenCheck",token);
        return !token.equals("null");
    }
}

