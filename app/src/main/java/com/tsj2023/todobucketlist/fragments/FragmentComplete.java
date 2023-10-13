package com.tsj2023.todobucketlist.fragments;

import static android.app.Activity.RESULT_CANCELED;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.activities.MainActivity;
import com.tsj2023.todobucketlist.adapters.CompleteRecyclerAdapter;
import com.tsj2023.todobucketlist.data.CompleteItem;
import com.tsj2023.todobucketlist.databinding.FragmentCompleteBinding;
import com.tsj2023.todobucketlist.databinding.FragmentCompleteNotLoginBinding;
import com.tsj2023.todobucketlist.network.G;
import com.tsj2023.todobucketlist.network.RetrofitHelper;
import com.tsj2023.todobucketlist.network.RetrofitService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentComplete extends Fragment {

    ArrayList<CompleteItem> completeItems = new ArrayList<>();
    FragmentCompleteBinding binding;
    CompleteRecyclerAdapter adapter;
    String imgPath;
    private CompleteItem selectedCompleteItem; // 선택한 아이템 저장

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            binding = FragmentCompleteBinding.inflate(inflater, container, false);

            binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadData();
                    binding.refreshLayout.setRefreshing(false);
                }
            });
            return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void onResume() {
        super.onResume();
        loadData();
    }

    void loadData(){
        Retrofit retrofit= RetrofitHelper.getRetrofitInstance();
        RetrofitService retrofitService=retrofit.create(RetrofitService.class);
        String email = G.email;
        Call<ArrayList<CompleteItem>> call=retrofitService.loadDataFromServer(email);
        call.enqueue(new Callback<ArrayList<CompleteItem>>() {
            @Override
            public void onResponse(Call<ArrayList<CompleteItem>> call, Response<ArrayList<CompleteItem>> response) {

                completeItems.clear();

                ArrayList<CompleteItem> items=response.body();
                for (CompleteItem item : items){
                    completeItems.add(0,item);
                }
                adapter=new CompleteRecyclerAdapter(getContext(),completeItems,FragmentComplete.this);
                binding.completeRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<CompleteItem>> call, Throwable t) {
                Toast.makeText(getContext(), "error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Json",t.getMessage()+"");
            }
        });
    }

    ActivityResultLauncher<Intent> resultLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode()==RESULT_CANCELED) return;

        Intent intent= result.getData();
        Uri uri= intent.getData();
        imgPath= getRealPathFromUri(uri);
        onImageSelected(imgPath);

        //new AlertDialog.Builder(this).setMessage(imgPath).create().show();
    });

    //Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    String getRealPathFromUri(Uri uri){
        String[] proj= {MediaStore.Images.Media.DATA};
        CursorLoader loader= new CursorLoader(getContext(), uri, proj, null, null, null);
        Cursor cursor= loader.loadInBackground();
        int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result= cursor.getString(column_index);
        cursor.close();
        return  result;
    }
    public void onImagePickerRequested() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }
    public void setSelectedCompleteItem(CompleteItem item) {
        selectedCompleteItem = item;
    }
    public void onImageSelected(String selectedImgPath) {
        imgPath = selectedImgPath;
        Log.d("FragmentComplete", "imgPath updated: " + selectedImgPath); // 로그 추가

        if (selectedCompleteItem != null){
            selectedCompleteItem.setImgPath(imgPath);
            Log.d("selectedCompleteItem",selectedCompleteItem+"");
            adapter.notifyDataSetChanged(); // 어댑터에 변경 사항을 알립니다.
        }else {
            Toast.makeText(getContext(), "버킷리스트를 달성해 보세요", Toast.LENGTH_SHORT).show();
            Log.d("selectedCompleteItem",selectedCompleteItem+"");
        }
    }

}

