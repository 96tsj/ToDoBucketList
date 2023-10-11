package com.tsj2023.todobucketlist.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.data.CompleteItem;
import com.tsj2023.todobucketlist.databinding.RecyclerItemCompleteBinding;
import com.tsj2023.todobucketlist.fragments.FragmentComplete;
import com.tsj2023.todobucketlist.network.RetrofitHelper;
import com.tsj2023.todobucketlist.network.RetrofitService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CompleteRecyclerAdapter extends RecyclerView.Adapter<CompleteRecyclerAdapter.VH> {

    Context context;
    ArrayList<CompleteItem> completeItems = new ArrayList<>();
    FragmentComplete fragmentComplete;
    int number;
    String imgPath;

    public CompleteRecyclerAdapter(Context context, ArrayList<CompleteItem> completeItems, FragmentComplete fragmentComplete) {
        this.context = context;
        this.completeItems = completeItems;
        this.fragmentComplete = fragmentComplete;
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

        holder.tv.setText(completeItem.date+" "+completeItem.title);

        String url= "http://tsj123.dothome.co.kr/TodoBucketlist/" + completeItem.getFile();
        //Log.d("CompleteURL",url);
        number=completeItem.getNo();

        Glide.with(context).load(url).error(R.drawable.defaultcomplete).into(holder.binding.ivRecyclerItemComplete);

        holder.ib.setOnClickListener(view -> {
            fragmentComplete.setSelectedCompleteItem(completeItem); // 아이템을 선택하도록 FragmentComplete에 알림
            fragmentComplete.onImagePickerRequested();
        });

        String itemImgPath = completeItem.getImgPath();

        if (itemImgPath!=null){
            updateComplete(itemImgPath);
            Log.d("itemImgPath",itemImgPath+"");
        }else {
        }
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
    void updateComplete(String imgPath){
        Retrofit retrofit= RetrofitHelper.getRetrofitInstance();
        RetrofitService retrofitService= retrofit.create(RetrofitService.class);

        int no = number;

        if (no!=0){
            //String데이터들
            Map<String,String> dataPart=new HashMap<>();
            dataPart.put("no",no+"");

            MultipartBody.Part filePart= null;
            if(imgPath !=null){
                File file= new File(imgPath);
                RequestBody body= RequestBody.create(MediaType.parse("image/*"), file);
                filePart= MultipartBody.Part.createFormData("img", file.getName(), body);
                Log.d("upload img",imgPath);
            }

            Call<String> call= retrofitService.postDataToServer(dataPart,filePart);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String s= response.body();
                    Toast.makeText(context, "업로드 성공 : ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Retrofit Error", t.getMessage());
                    Toast.makeText(context, "업로드 실패: ", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "no=null", Toast.LENGTH_SHORT).show();
        }
    }
}
