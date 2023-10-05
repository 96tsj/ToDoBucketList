package com.tsj2023.todobucketlist.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.activities.MainActivity;
import com.tsj2023.todobucketlist.data.DatabaseHelper;
import com.tsj2023.todobucketlist.data.TodoItem;
import com.tsj2023.todobucketlist.databinding.RecyclerItemTodoBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TodoRecyclerAdapter extends RecyclerView.Adapter<TodoRecyclerAdapter.VH> {
    Context context;
    ArrayList<TodoItem> todoItems = new ArrayList<>();

    public TodoRecyclerAdapter(Context context, ArrayList<TodoItem> todoItems) {
        this.context = context;
        this.todoItems = todoItems;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.from(context).inflate(R.layout.recycler_item_todo,parent,false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        //체크박스 값 변경불가를 위해 final로 변경
        final TodoItem todoItem = todoItems.get(position);

        holder.tv.setText(todoItem.msg);
        holder.cb.setChecked(todoItem.checked);

        //체크박스 리스너 초기화
        holder.cb.setOnCheckedChangeListener(null);

        holder.cb.setChecked(todoItem.getSelected());
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (holder.cb.isChecked()==true){
                    holder.iv.setVisibility(View.VISIBLE);
                    todoItem.setChecked(true);
                    Log.d("Check",""+holder.cb.isChecked());

                }else {
                    holder.iv.setVisibility(View.INVISIBLE);
                    todoItem.setChecked(false);
                    Log.d("Check2",""+holder.cb.isChecked());

                }
                todoItem.setChecked(b);
                //메인에서 메소드 업데이트 호출
                ((MainActivity) context).updateTodoItem(todoItems.set(position,todoItem));
            }
        });

    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    class VH extends RecyclerView.ViewHolder{

        RecyclerItemTodoBinding binding;
        TextView tv;
        CheckBox cb;

        ImageView iv;


        public VH(@NonNull View itemView) {
            super(itemView);
            binding=RecyclerItemTodoBinding.bind(itemView);
            tv=binding.tvRecyclerItemTodo;
            cb=binding.cbRecyclerItemTodo;
            iv=binding.todoStamp;


        }
    }

}
