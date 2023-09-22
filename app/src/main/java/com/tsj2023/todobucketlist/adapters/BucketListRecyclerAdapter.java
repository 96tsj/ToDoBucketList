package com.tsj2023.todobucketlist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.data.BucketlistItem;
import com.tsj2023.todobucketlist.databinding.RecyclerItemBucketlistBinding;

import java.util.ArrayList;

public class BucketListRecyclerAdapter extends RecyclerView.Adapter<BucketListRecyclerAdapter.VH> {

    Context context;
    ArrayList<BucketlistItem> bucketlistItems=new ArrayList<>();

    public BucketListRecyclerAdapter(Context context, ArrayList<BucketlistItem> bucketlistItems) {
        this.context = context;
        this.bucketlistItems = bucketlistItems;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View itemView = layoutInflater.from(context).inflate(R.layout.recycler_item_bucketlist,parent,false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        BucketlistItem bucketlistItem = bucketlistItems.get(position);
        holder.tv.setText(bucketlistItem.msg);
        holder.cb.setChecked(bucketlistItem.cheked);

    }

    @Override
    public int getItemCount() {
        return bucketlistItems.size();
    }

    class VH extends RecyclerView.ViewHolder{

        RecyclerItemBucketlistBinding binding;
        TextView tv;
        CheckBox cb;

        public VH(@NonNull View itemView) {
            super(itemView);
            binding=RecyclerItemBucketlistBinding.bind(itemView);
            tv=binding.tvRecyclerItemBucketlist;
            cb=binding.cbRecyclerItemBucketlist;
        }
    }
}
