package com.tsj2023.todobucketlist.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.ViewContainer;
import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.databinding.FragmentScheduleBinding;
import com.tsj2023.todobucketlist.databinding.FragmentTodoBinding;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Date;

public class FragmentSchedule extends Fragment{

    FragmentScheduleBinding binding;

    CalendarView calendarView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentScheduleBinding.inflate(inflater,container,false);

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
}

