package com.tsj2023.todobucketlist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.adapters.ViewPaserAdapter;
import com.tsj2023.todobucketlist.data.TodoItem;
import com.tsj2023.todobucketlist.databinding.ActivityMainBinding;
import com.tsj2023.todobucketlist.fragments.FragmentBucketList;
import com.tsj2023.todobucketlist.fragments.FragmentComplete;
import com.tsj2023.todobucketlist.fragments.FragmentSchedule;
import com.tsj2023.todobucketlist.fragments.FragmentTodo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<TodoItem> todoItems=new ArrayList<>();
    ActivityMainBinding binding;
    TabLayout tabLayout;
    ViewPager2 pager;
    FrameLayout frameLayout;
    String[] tabTitle = new String[] {"오늘의할일","일정관리","버킷리스트","달성목표"};
    Fragment[] fragments = new Fragment[4];
    ViewPaserAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tabLayout=findViewById(R.id.tab_layout);
        frameLayout=findViewById(R.id.frame_layout);

        pager = binding.pager.findViewById(R.id.pager);
        adapter=new ViewPaserAdapter(this);
        pager.setAdapter(adapter);

        //시작할때 TodoFragment 보여주기
        //getSupportFragmentManager().beginTransaction().add(R.id.frame_layout,new FragmentTodo()).commit();

        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout,pager,(tab, position) -> {
            tab.setText(tabTitle[position]);
        });

        mediator.attach();

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });



    }

}