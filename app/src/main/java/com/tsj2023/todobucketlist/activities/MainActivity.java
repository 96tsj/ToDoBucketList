package com.tsj2023.todobucketlist.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.adapters.CompleteRecyclerAdapter;
import com.tsj2023.todobucketlist.adapters.ViewPaserAdapter;
import com.tsj2023.todobucketlist.data.BucketlistItem;
import com.tsj2023.todobucketlist.data.DatabaseHelper;
import com.tsj2023.todobucketlist.data.TodoItem;
import com.tsj2023.todobucketlist.databinding.ActivityMainBinding;
import com.tsj2023.todobucketlist.databinding.RecyclerItemCompleteBinding;
import com.tsj2023.todobucketlist.fragments.FragmentBucketList;
import com.tsj2023.todobucketlist.fragments.FragmentComplete;
import com.tsj2023.todobucketlist.fragments.FragmentCompleteNotLogin;
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
    SQLiteDatabase db;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        dbHelper = new DatabaseHelper(this);
        db= dbHelper.getWritableDatabase();

        tabLayout=findViewById(R.id.tab_layout);
        frameLayout=findViewById(R.id.frame_layout);

        pager = binding.pager.findViewById(R.id.pager);
        adapter=new ViewPaserAdapter(this,this);
        pager.setAdapter(adapter);

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
public long insertTodoItem(TodoItem todoItem) {
    ContentValues values = new ContentValues();
    values.put("msg", todoItem.msg);
    values.put("checked", todoItem.checked ? 1 : 0);
    values.put("category", todoItem.category);

    long newRowId = db.insert("todo_bucket_list", null, values);

    if (newRowId == -1) {
        // 데이터베이스에 추가 실패한 경우
        // 오류 처리 코드 작성
    } else {
        // 데이터베이스에 추가 성공한 경우
        // 성공 처리 코드 작성
    }
    return newRowId;
}
public long insertBucketItem(BucketlistItem bucketlistItem){
        ContentValues values = new ContentValues();
        values.put("msg",bucketlistItem.msg);
        values.put("checked", bucketlistItem.checked ? 1 : 0);
        values.put("category", bucketlistItem.category);

        long newRowId = db.insert("todo_bucket_list", null,values);
        if (newRowId == -1){

        }else {
            Toast.makeText(this, "데이터 추가에 실패", Toast.LENGTH_SHORT).show();
        }
        return newRowId;
}
public void updateTodoItem(TodoItem item) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    //db transaction시작
    db.beginTransaction();
    try {
        ContentValues values = new ContentValues();
        //valuew에 checked값 집어넣기
        values.put("checked", item.checked ? 1 : 0);

        int rowsUpdated = db.update("todo_bucket_list", values, "id = ?", new String[]{String.valueOf(item.getId())});

        if (rowsUpdated > 0) {
            // 업데이트가 성공적으로 수행되었습니다.
            db.setTransactionSuccessful();
        } else {
            // 업데이트에 실패한 경우
            Toast.makeText(this, "데이터 업데이트에 실패했습니다", Toast.LENGTH_SHORT).show();
        }
    }catch (Exception e) {
        Log.e("UpdateTodoItem", "트랜잭션 실패: " + e.getMessage());

    }finally {
        db.endTransaction();
    }
    }

    public void updateBucketItem(BucketlistItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //db transaction시작
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            //valuew에 checked값 집어넣기
            values.put("checked", item.checked ? 1 : 0);

            int rowsUpdated = db.update("todo_bucket_list", values, "id = ?", new String[]{String.valueOf(item.getId())});

            if (rowsUpdated > 0) {
                // 업데이트가 성공적으로 수행되었습니다.
                db.setTransactionSuccessful();
            } else {
                // 업데이트에 실패한 경우
                Toast.makeText(this, "데이터 업데이트에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e) {
            Log.e("UpdateBucketItem", "트랜잭션 실패: " + e.getMessage());

        }finally {
            db.endTransaction();
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }

    }
    public DatabaseHelper getDbHelper(){
        return dbHelper;
    }



}