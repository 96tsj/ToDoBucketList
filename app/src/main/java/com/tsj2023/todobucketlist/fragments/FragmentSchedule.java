package com.tsj2023.todobucketlist.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.ViewContainer;
import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.databinding.FragmentScheduleBinding;
import com.tsj2023.todobucketlist.databinding.FragmentTodoBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class FragmentSchedule extends Fragment{

    FragmentScheduleBinding binding;
    CalendarView calendarView;
    FusedLocationProviderClient fusedLocationProviderClient;

    static RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentScheduleBinding.inflate(inflater,container,false);

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
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                }
            }
        }); // 위치 정보 얻어오기 끝

        CurrentWeatherCall();
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm:ss");
        String getDay = simpleDateFormatDay.format(date);
        String getTime = simpleDateFormatTime.format(date);
        String getDate = getDay + "\n" + "\n" + getTime;

        binding.ib.setOnClickListener(view -> {
            binding.tvYmd.setText(getDate);
        });

        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    double latitude=0;
    double longitude=0;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {

            latitude=location.getLatitude();
            longitude=location.getLongitude();

        }
    };

    private void CurrentWeatherCall(){
        String url="https://api.openweathermap.org/data/2.5/weather?lat="+longitude+"&lon="+latitude+"&appid=3c3ff7ec56db1eea182567887246e6b5";
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
                    binding.tvWeather.setText(latitude+""+longitude+"");

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


}

