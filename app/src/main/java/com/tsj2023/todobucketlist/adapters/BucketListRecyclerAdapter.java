package com.tsj2023.todobucketlist.adapters;

import android.content.Context;
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
        holder.cb.setChecked(bucketlistItem.checked);

        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (holder.cb.isChecked()){
                    holder.iv.setVisibility(View.VISIBLE);
                }else {
                    holder.iv.setVisibility(View.INVISIBLE);
                }

                bucketlistItem.setChecked(b);

                if (listener!=null){
                    listener.onItemCheckedChanged(bucketlistItem,b);
                }
            }
        });

    }

    // 버킷리스트 항목의 체크 상태 변경 리스너 인터페이스 정의
    public interface OnItemCheckedChangedListener {
        void onItemCheckedChanged(BucketlistItem item, boolean isChecked);
    }

    private OnItemCheckedChangedListener listener;

    // 리스너 설정 메서드
    public void setOnItemCheckedChangedListener(OnItemCheckedChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return bucketlistItems.size();
    }

    class VH extends RecyclerView.ViewHolder{

        RecyclerItemBucketlistBinding binding;
        TextView tv;
        CheckBox cb;
        ImageView iv;

        public VH(@NonNull View itemView) {
            super(itemView);
            binding=RecyclerItemBucketlistBinding.bind(itemView);
            tv=binding.tvRecyclerItemBucketlist;
            cb=binding.cbRecyclerItemBucketlist;
            iv=binding.bucketlistStamp;
        }
    }

}
