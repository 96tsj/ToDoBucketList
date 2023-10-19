package com.tsj2023.todobucketlist.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showDeleteConfirmationDialog(getAdapterPosition());
                    return true;
                }
            });
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
    private void showDeleteConfirmationDialog(final int position) {
        Retrofit retrofit= RetrofitHelper.getRetrofitInstance();
        RetrofitService retrofitService= retrofit.create(RetrofitService.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("선택한 아이템을 삭제하시겠습니까?");
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 여기에서 선택한 아이템을 삭제하는 코드를 추가합니다.
                CompleteItem completeItem = completeItems.get(position);
                int itemNo = completeItem.getNo();
                // 서버로 삭제 요청을 보내는 코드 작성
                Call<String> call = retrofitService.deleteItem(itemNo);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            // 서버에서 삭제 성공 응답을 받은 경우 로컬 목록에서 아이템을 삭제
                            completeItems.remove(position);
                            notifyDataSetChanged();
                        } else {
                            // 서버에서 삭제 실패 응답을 받은 경우 오류 처리
                            Log.e("DeleteItem", "서버에서 아이템 삭제 실패");
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        // 서버와 통신 중 에러 발생한 경우 처리
                        Log.e("DeleteItem", "아이템 삭제 API 호출 중 에러: " + t.getMessage());
                    }
                });
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
