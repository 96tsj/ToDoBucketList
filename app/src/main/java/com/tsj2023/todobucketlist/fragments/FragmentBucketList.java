package com.tsj2023.todobucketlist.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tsj2023.todobucketlist.R;
import com.tsj2023.todobucketlist.databinding.FragmentBucketlistBinding;
import com.tsj2023.todobucketlist.databinding.FragmentTodoBinding;

import java.util.ArrayList;

public class FragmentBucketList extends Fragment{

    FragmentBucketlistBinding binding;


    PieChart pieChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentBucketlistBinding.inflate(inflater,container,false);
        pieChart=binding.chart;
        ArrayList<PieEntry> dataValue=new ArrayList<PieEntry>();

        dataValue.add(new PieEntry(1,"달성완료"));
        dataValue.add(new PieEntry(2,"도전중"));
        PieDataSet dataSet = new PieDataSet(dataValue, "");

        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        Description description = new Description();
        description.setText("버킷리스트 달성률"); //라벨
        description.setTextSize(15);
        description.setXOffset(20);
        pieChart.setDescription(description);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setEntryLabelTextSize(10);
        pieChart.invalidate();

        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}