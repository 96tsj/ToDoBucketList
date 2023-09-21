package com.tsj2023.todobucketlist.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tsj2023.todobucketlist.fragments.FragmentBucketList;
import com.tsj2023.todobucketlist.fragments.FragmentComplete;
import com.tsj2023.todobucketlist.fragments.FragmentSchedule;
import com.tsj2023.todobucketlist.fragments.FragmentTodo;

public class ViewPaserAdapter extends FragmentStateAdapter {

    Fragment[] fragments = new Fragment[4];

    public ViewPaserAdapter(@NonNull FragmentActivity fragmentActivity){
        super(fragmentActivity);

        fragments[0] = new FragmentTodo();
        fragments[1] = new FragmentSchedule();
        fragments[2] = new FragmentBucketList();
        fragments[3] = new FragmentComplete();

    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }
}
