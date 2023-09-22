package com.tsj2023.todobucketlist.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.adapters.TodoRecyclerAdapter;
import com.tsj2023.todobucketlist.data.TodoItem;
import com.tsj2023.todobucketlist.databinding.DialogAddTodoBinding;
import com.tsj2023.todobucketlist.databinding.FragmentTodoBinding;

import java.util.ArrayList;

public class FragmentTodo extends Fragment{

    ArrayList<TodoItem> todoItems = new ArrayList<>();
    FragmentTodoBinding binding;
    DialogAddTodoBinding binding2;
    TodoRecyclerAdapter adapter;
    TextInputLayout textInputLayout;

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
                if (!TextUtils.isEmpty(a)) {
                    // This block is responsible for checking if edittext is empty and carrying out the action
                    todoItems.add(new TodoItem(a, false));
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

}