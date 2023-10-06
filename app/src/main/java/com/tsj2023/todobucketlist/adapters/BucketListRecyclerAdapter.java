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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.activities.MainActivity;
import com.tsj2023.todobucketlist.data.BucketlistItem;
import com.tsj2023.todobucketlist.databinding.RecyclerItemBucketlistBinding;
import com.tsj2023.todobucketlist.network.RetrofitHelper;
import com.tsj2023.todobucketlist.network.RetrofitService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BucketListRecyclerAdapter extends RecyclerView.Adapter<BucketListRecyclerAdapter.VH> {

    AppCompatActivity activity; // Activity 참조를 저장하기 위한 필드
    ArrayList<BucketlistItem> bucketlistItems=new ArrayList<>();

    private BucketlistItem lastCheckedItem;


    public BucketListRecyclerAdapter(AppCompatActivity activity, ArrayList<BucketlistItem> bucketlistItems) {
        this.activity = activity;
        this.bucketlistItems = bucketlistItems;

    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(activity);
        View itemView = layoutInflater.from(activity).inflate(R.layout.recycler_item_bucketlist,parent,false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        BucketlistItem bucketlistItem = bucketlistItems.get(position);
        holder.tv.setText(bucketlistItem.getMsg());
        holder.cb.setChecked(bucketlistItem.isChecked());

        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (holder.cb.isChecked()){
                    holder.iv.setVisibility(View.VISIBLE);
                    bucketlistItem.setChecked(b);
                    //마지막체크된 아이템 저장
                    lastCheckedItem = bucketlistItem;
                    updateComplete();

                }else {
                    holder.iv.setVisibility(View.INVISIBLE);
                    bucketlistItem.setChecked(b);
                }

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
    void updateComplete(){
        Retrofit retrofit= RetrofitHelper.getRetrofitInstance();
        RetrofitService retrofitService= retrofit.create(RetrofitService.class);


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 1을 더함
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

//        String title=null;
//
//
//        for (BucketlistItem item : bucketlistItems) {
//            if (item.isChecked()) {
//                String msg = item.getMsg();
//                // msg를 사용하여 원하는 작업 수행
//                title=msg;
//            }
//        }
        String title = lastCheckedItem.getMsg();
        // 연월일을 원하는 형식으로 포맷
        String date = String.format("%04d-%02d-%02d", year, month, dayOfMonth);

        if (title!=null){
            //String데이터들
            Map<String,String> dataPart=new HashMap<>();
            dataPart.put("title",title);
            dataPart.put("date",date);

            Call<String> call= retrofitService.postDataToServer(dataPart,null);

            Log.d("Debug", "Title: " + title);
            Log.d("Debug", "Date: " + date);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String s= response.body();
                    Toast.makeText(activity, "응답 : "+s, Toast.LENGTH_SHORT).show();
                    //Log.d("retrofit response","응답 : "+s);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Retrofit Error", t.getMessage());
                    Toast.makeText(activity, "업로드 실패: ", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "title = null", Toast.LENGTH_SHORT).show();
        }

    }

}
