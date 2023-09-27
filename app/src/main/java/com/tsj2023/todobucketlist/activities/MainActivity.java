package com.tsj2023.todobucketlist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.adapters.ViewPaserAdapter;
import com.tsj2023.todobucketlist.data.DatabaseHelper;
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
    SQLiteDatabase db;
    TodoItem todoItem;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        String TableName = "myapp";
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        db= dbHelper.getWritableDatabase();
        TodoItem todoItem=new TodoItem("새로운 할 일",false,"일반");
        insertTodoItem(todoItem);

        db=openOrCreateDatabase(TableName,MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS todo_bucket_list(id INTEGER PRIMARY KEY AUTOINCREMENT,msg TEXT,checked INTEGER,category TEXT)");

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
public void insertTodoItem(TodoItem todoItem) {
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
}
    public void loadTodoItemsFromDatabase() {
        Cursor cursor = db.query("todo_bucket_list", null, null, null, null, null, "id ASC"); // ID 오름차순으로 정렬

        if (cursor != null) {
            int idColumnIndex = cursor.getColumnIndex("id");
            int msgColumnIndex = cursor.getColumnIndex("msg");
            int checkedColumnIndex = cursor.getColumnIndex("checked");
            int categoryColumnIndex = cursor.getColumnIndex("category");

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumnIndex);
                String msg = cursor.getString(msgColumnIndex);
                int checked = cursor.getInt(checkedColumnIndex);
                String category = cursor.getString(categoryColumnIndex);
                boolean isChecked = checked == 1;

                TodoItem todoItem = new TodoItem(id, msg, isChecked, category);
                todoItems.add(todoItem);
            }
            cursor.close();
        }
    }

}