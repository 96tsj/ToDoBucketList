package com.tsj2023.todobucketlist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.data.CompleteItem;
import com.tsj2023.todobucketlist.databinding.RecyclerItemCompleteBinding;

import java.util.ArrayList;

public class CompleteRecyclerAdapter extends RecyclerView.Adapter<CompleteRecyclerAdapter.VH> {

    Context context;
    ArrayList<CompleteItem> completeItems = new ArrayList<>();

    public CompleteRecyclerAdapter(Context context, ArrayList<CompleteItem> completeItems) {
        this.context = context;
        this.completeItems = completeItems;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.recycler_item_complete,parent,false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        CompleteItem completeItem=completeItems.get(position);

        holder.tv.setText(completeItem.title);
        holder.iv.setImageResource(Integer.parseInt(completeItem.img));



    }

    @Override
    public int getItemCount() {
        return completeItems.size();
    }

    class VH extends RecyclerView.ViewHolder{
        RecyclerItemCompleteBinding binding;
        TextView tv;
        ImageView iv;

        public VH(@NonNull View itemView) {
            super(itemView);
            binding=RecyclerItemCompleteBinding.bind(itemView);
            tv=binding.toolbarTitle;
            iv=binding.ivRecyclerItemComplete;
        }
    }
}
