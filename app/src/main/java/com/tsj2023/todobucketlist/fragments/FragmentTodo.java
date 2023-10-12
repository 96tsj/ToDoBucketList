package com.tsj2023.todobucketlist.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.activities.MainActivity;
import com.tsj2023.todobucketlist.adapters.TodoRecyclerAdapter;
import com.tsj2023.todobucketlist.data.DatabaseHelper;
import com.tsj2023.todobucketlist.data.TodoItem;
import com.tsj2023.todobucketlist.databinding.DialogAddTodoBinding;
import com.tsj2023.todobucketlist.databinding.FragmentTodoBinding;
import com.tsj2023.todobucketlist.databinding.RecyclerItemTodoBinding;

import java.util.ArrayList;

public class FragmentTodo extends Fragment{

    ArrayList<TodoItem> todoItems = new ArrayList<>();
    FragmentTodoBinding binding;
    TodoRecyclerAdapter adapter;
    String category = "오늘의할일";
    SQLiteDatabase db;
    DatabaseHelper dbHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentTodoBinding.inflate(inflater,container,false);
        binding.fabTodo.setOnClickListener(view -> clickfab());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        adapter = new TodoRecyclerAdapter(getContext(),todoItems);
        binding.todoRecyclerView.setAdapter(adapter);
        loadTodoItem();

        adapter.setOnItemLongClickListener(new TodoRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                deletedItem(position);
            }
        });
    }

    void deletedItem(int position){
        if (position>=0&&position<todoItems.size()){
            TodoItem itemToDelete = todoItems.get(position);

            //데이터 베이스에서 삭제
            deletedItemFromDatabase(itemToDelete.getId());

            //리스트에서 삭제
            todoItems.remove(position);
            adapter.notifyDataSetChanged();
        }
    }

    void clickfab(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialog_add_todo, null);

        // DialogAddTodoBinding으로부터 EditText를 참조합니다.
        TextInputLayout editTextTodo = dialogView.findViewById(R.id.text_input_layout_todo);

        builder.setView(dialogView);

        builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String a = editTextTodo.getEditText().getText().toString();
                long id = ((MainActivity) requireActivity()).insertTodoItem(new TodoItem(a, false, category));

                TodoItem newTodoItem = new TodoItem(id,a, false,category);

                if (id !=-1 && !TextUtils.isEmpty(a)) {
                    todoItems.add(newTodoItem);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void loadTodoItem(){
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        db = dbHelper.getReadableDatabase();

        String[] columns = null; // 모든 열을 선택
        String selection = "category = ?"; // category 열이 "오늘의할일"인 것만 선택
        String[] selectionArgs = new String[]{"오늘의할일"};

        Cursor cursor = db.query("todo_bucket_list", columns, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
                 TodoItem todoItem = new TodoItem();
                 todoItem.id = cursor.getLong(0);
                 todoItem.msg = cursor.getString(1);
                 todoItem.checked = cursor.getInt(2) == 1;
                 todoItem.category = cursor.getString(3);

                 todoItems.add(todoItem);
             }
             cursor.close();
             db.close();

             //데이터 추가후 어댑터 생성
            adapter.notifyDataSetChanged();
    }
    public void deletedItemFromDatabase(long itemId){
        dbHelper = ((MainActivity) requireActivity()).getDbHelper();
        db=dbHelper.getWritableDatabase();

        //데이터 베이스 트랜젝션 시작
        db.beginTransaction();

        try {
            //삭제할 아이템의 ID를 사용하여 DELETE쿼리 실행
            db.delete("todo_bucket_list","id = ?", new String[]{String.valueOf(itemId)});

            //트랜젝션 성공
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.e("DeleteItem","삭제실패 : " + e.getMessage());

        }finally {
            db.endTransaction();
        }
    }

}