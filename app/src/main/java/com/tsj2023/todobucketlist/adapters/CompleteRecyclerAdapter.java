package com.tsj2023.todobucketlist.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.data.CompleteItem;
import com.tsj2023.todobucketlist.databinding.RecyclerItemCompleteBinding;

import java.util.ArrayList;

public class CompleteRecyclerAdapter extends RecyclerView.Adapter<CompleteRecyclerAdapter.VH> {

    Context context;
    ArrayList<CompleteItem> completeItems = new ArrayList<>();
    String imgPath; //이미지의 실제경로
    RecyclerItemCompleteBinding binding;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    public CompleteRecyclerAdapter(Context context, ArrayList<CompleteItem> completeItems, ActivityResultLauncher<Intent> imagePickerLauncher) {
        this.context = context;
        this.completeItems = completeItems;
        this.imagePickerLauncher = imagePickerLauncher;
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
        String url= "http://tsj123.dothome.co.kr/TodoBucketlist/" + completeItem.img;
        Glide.with(context).load(url).into(holder.iv);

        holder.ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSelect();
            }
        });

    }

    @Override
    public int getItemCount() {
        return completeItems.size();
    }

    class VH extends RecyclerView.ViewHolder{
        RecyclerItemCompleteBinding binding;
        TextView tv;
        ImageView iv;
        ImageButton ib;

        public VH(@NonNull View itemView) {
            super(itemView);
            binding=RecyclerItemCompleteBinding.bind(itemView);
            tv=binding.completeTitle;
            iv=binding.ivRecyclerItemComplete;
            ib=binding.selectImageBtn;
        }
    }
    void clickSelect(){
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        imagePickerLauncher.launch(intent);
    }


}
