package com.tsj2023.todobucketlist.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.type.DateTime;
import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.data.BucketlistItem;
import com.tsj2023.todobucketlist.databinding.FragmentScheduleBinding;
import com.tsj2023.todobucketlist.databinding.FragmentTodoBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

public class FragmentSchedule extends Fragment{

    FragmentScheduleBinding binding;
    FusedLocationProviderClient fusedLocationProviderClient;
    static RequestQueue requestQueue;
    double latitude=0;
    double longitude=0;
    CalendarView calendarView;
    TextView tvSchedule; // 일정을 표시할 TextView
    LocalDate selectedDate; // 선택한 날짜를 저장하는 변수

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentScheduleBinding.inflate(inflater,container,false);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyCalendarData", Context.MODE_PRIVATE);
        binding.fabSchedule.setOnClickListener(view -> clickfab());
        // TextView 초기화
        tvSchedule = binding.tvSchedule;

        // FAB 클릭 시 이벤트 처리
        binding.fabSchedule.setOnClickListener(view -> clickfab());

        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(getActivity());
        }

        //위치 정보 얻어오기 시작
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        int permissionState = getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionState == PackageManager.PERMISSION_DENIED) {
            resultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        //위치정보요청 객체 생성
        LocationRequest.Builder builder = new LocationRequest.Builder(5000);
        LocationRequest locationRequest = builder.build();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationListener, Looper.getMainLooper());
         // 위치 정보 얻어오기 끝

        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm:ss");
        String getDay = simpleDateFormatDay.format(date);
        String getTime = simpleDateFormatTime.format(date);
        String getDate = getDay + "\n" + "\n" + getTime;

        binding.tvYmd.setText(getDate);

        //calendarview 일정등록기능 설계
        calendarView=binding.calendarView;

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                // 선택한 날짜를 업데이트
                selectedDate = LocalDate.of(i, i1 + 1, i2);

                // 선택한 날짜에 저장된 일정을 가져와서 TextView에 표시
                String formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String eventData = sharedPreferences.getString(formattedDate, "");

                // 이벤트 데이터가 있으면 표시
                if (!TextUtils.isEmpty(eventData)) {
                    tvSchedule.setText(eventData); // 일정 표시 TextView에 데이터 설정
                } else {
                    tvSchedule.setText(""); // 데이터가 없으면 비움
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {

            latitude=location.getLatitude();
            longitude=location.getLongitude();
            currentWeatherCall();
            fusedLocationProviderClient.removeLocationUpdates(this);

        }
    };

    public void currentWeatherCall(){
        String url="https://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid=3c3ff7ec56db1eea182567887246e6b5";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;
                String city = null;
                JSONObject tempK = null;
                JSONArray weatherJson = null;

                try {
                    jsonObject = new JSONObject(response);

                    city = jsonObject.getString("name");
                    binding.tvCity.setText(city);
                    Log.d("s",response);

                    weatherJson = jsonObject.getJSONArray("weather");
                    JSONObject weatherObj = weatherJson.getJSONObject(0);
                    String weather = weatherObj.getString("description");
                    binding.tvWeather.setText(weather);

                    tempK = new JSONObject(jsonObject.getString("main"));
                    double tempDo = (Math.round((tempK.getDouble("temp")-273.15)*100)/100.0);
                    binding.tvTemp.setText(tempDo +  "°C");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    ActivityResultLauncher<String> resultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) Toast.makeText(getActivity(), "위치 사용 가능", Toast.LENGTH_SHORT).show();
            else Toast.makeText(getActivity(), "위치 사용 불가", Toast.LENGTH_SHORT).show();
        }
    });

    void clickfab(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialog_add_schedule, null);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextInputLayout editTextSchedule = dialogView.findViewById(R.id.text_input_layout_schedule);
        builder.setView(dialogView);

        builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String s= editTextSchedule.getEditText().getText().toString();
                if (!TextUtils.isEmpty(s)){
                    binding.tvSchedule.setText(s);
                    // 현재 선택한 날짜 가져오기
                    if (selectedDate!=null){
                        String formattedDate=selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        // SharedPreferences에 일정 저장
                        saveScheduleToSharedPreferences(formattedDate, s);
                    }else {
                        Toast.makeText(getContext(), "날짜를 선택하세요", Toast.LENGTH_SHORT).show();
                    }

                    // TextView에 일정 표시
                    tvSchedule.setText(s);

                    dialogInterface.dismiss();
                } else {
                    Toast.makeText(getActivity(), "일정을 입력하세요", Toast.LENGTH_SHORT).show();
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

    // SharedPreferences에 일정을 저장하는 메서드
    private void saveScheduleToSharedPreferences(String selectedDate, String schedule) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyCalendarData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(selectedDate, schedule);
        editor.apply();
    }

}


