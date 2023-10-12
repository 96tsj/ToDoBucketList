package com.tsj2023.todobucketlist.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.CarrierConfigManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.activities.LoginActivity;
import com.tsj2023.todobucketlist.activities.MainActivity;
import com.tsj2023.todobucketlist.adapters.BucketListRecyclerAdapter;
import com.tsj2023.todobucketlist.adapters.TodoRecyclerAdapter;
import com.tsj2023.todobucketlist.data.BucketlistItem;
import com.tsj2023.todobucketlist.data.DatabaseHelper;
import com.tsj2023.todobucketlist.data.TodoItem;
import com.tsj2023.todobucketlist.databinding.FragmentBucketlistBinding;
import com.tsj2023.todobucketlist.databinding.FragmentTodoBinding;
import com.tsj2023.todobucketlist.databinding.RecyclerItemBucketlistBinding;
import com.tsj2023.todobucketlist.databinding.RecyclerItemTodoBinding;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class FragmentBucketList extends Fragment {

    FragmentBucketlistBinding binding;
    PieChart pieChart;
    ArrayList<BucketlistItem> bucketlistItems = new ArrayList<>();
    BucketListRecyclerAdapter adapter;
    MainActivity activity;
    SQLiteDatabase db;
    DatabaseHelper dbHelper;
    String category="버킷리스트";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBucketlistBinding.inflate(inflater, container, false);
        binding.fabBucketlist.setOnClickListener(view -> clickfab());

        return binding.getRoot();
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // context를 MainActivity로 형변환하고 activity 변수에 할당
        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (activity != null){
            adapter=new BucketListRecyclerAdapter(activity,bucketlistItems,getContext());
            adapter.setOnItemCheckedChangedListener(new BucketListRecyclerAdapter.OnItemCheckedChangedListener() {
                @Override
                public void onItemCheckedChanged(BucketlistItem item, boolean isChecked) {
                    if (isChecked){
                        updatePieChart();
                    }else {
                        updatePieChart();
                    }
                }
            });
            binding.bucketlistRecyclerView.setAdapter(adapter);
        }else {
            Toast.makeText(getContext(), "없음", Toast.LENGTH_SHORT).show();
        }
        adapter.setOnItemLongClickListener(new TodoRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                deletedItem(position);
            }
        });
        loadBucketItem();
    }
    void deletedItem(int position){
        if (position>=0&&position<bucketlistItems.size()){
            BucketlistItem itemToDelete = bucketlistItems.get(position);

            //데이터 베이스에서 삭제
            deletedItemFromDatabase(itemToDelete.getId());

            //리스트에서 삭제
            bucketlistItems.remove(position);
            adapter.notifyDataSetChanged();
            updatePieChart();
        }
    }
    void clickfab(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialog_add_bucketlist, null);

        // DialogAddTodoBinding으로부터 EditText를 참조합니다.
        TextInputLayout editTextBucket = dialogView.findViewById(R.id.text_input_layout_bucket);
        builder.setView(dialogView);

        builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String s= editTextBucket.getEditText().getText().toString();
                long id=activity.insertBucketItem(new BucketlistItem(s,false,category));

                BucketlistItem newBucketlistItem = new BucketlistItem(id,s,false,category);

                if (!TextUtils.isEmpty(s)){

                    bucketlistItems.add(newBucketlistItem);
                    updatePieChart();
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

    private void updatePieChart() {

        ArrayList<PieEntry> dataValue = new ArrayList<PieEntry>();

        // Count completed items
        int completedCount = 0;
        for (BucketlistItem item : bucketlistItems) {
            if (item.isChecked()) {
                completedCount++;
            }
        }

        pieChart=binding.chart;

        dataValue.add(new PieEntry(completedCount, "달성완료"));
        dataValue.add(new PieEntry(bucketlistItems.size() - completedCount, "도전중"));

        PieDataSet dataSet = new PieDataSet(dataValue, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setValueTextSize(15);
        dataSet.setValueTextColor(Color.rgb(00f,00f,99f));

        Description description = new Description();
        description.setText("버킷리스트 달성률"); // 라벨
        description.setTextSize(15);
        description.setXOffset(20);
        pieChart.setDescription(description);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setEntryLabelTextSize(10);
        pieData.notifyDataChanged();
        pieChart.invalidate(); // 그래프 갱신

        Log.d("update s",completedCount+"");

    }
    public void loadBucketItem(){
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        db = dbHelper.getReadableDatabase();

        String[] columns = null; // 모든 열을 선택
        String selection = "category = ?"; // category 열이 "오늘의할일"인 것만 선택
        String[] selectionArgs = new String[]{"버킷리스트"};

        Cursor cursor = db.query("todo_bucket_list", columns, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            BucketlistItem bucketlistItem = new BucketlistItem();
            bucketlistItem.id = cursor.getLong(0);
            bucketlistItem.msg = cursor.getString(1);
            bucketlistItem.checked = cursor.getInt(2) == 1;
            bucketlistItem.category = cursor.getString(3);

            bucketlistItems.add(bucketlistItem);
        }
        cursor.close();
        db.close();

        //데이터 추가후 어댑터 생성
        adapter.notifyDataSetChanged();
        updatePieChart();
    }
    public void deletedItemFromDatabase(long itemId){
        dbHelper = activity.getDbHelper();
        db=dbHelper.getWritableDatabase();

        //데이터 베이스 트랜젝션 시작
        db.beginTransaction();

        try {
            //삭제할 아이템의 ID를 사용하여 DELETE쿼리 실행
            db.delete("todo_bucket_list","id = ?", new String[]{String.valueOf(itemId)});

            //트랜젝션 성공
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.e("DeleteItem","삭제실패 : " + e.getMessage());

        }finally {
            db.endTransaction();
        }
    }

}