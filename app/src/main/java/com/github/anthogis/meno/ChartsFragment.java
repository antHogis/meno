package com.github.anthogis.meno;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class ChartsFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private PieChartView pieChartView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_charts);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charts, container, false);
        databaseHelper = ((MainActivity) getActivity()).getDatabaseHelper();
        pieChartView = view.findViewById(R.id.chart);
        pieChartView.setPieChartData(createPieCharData());
        pieChartView.setOnValueTouchListener(new CategorySliceSelectListener());
        return view;
    }

    private PieChartData createPieCharData() {
        List<Expense> expenses = databaseHelper.findAllExpenses();
        List<ExpenseCategory> categories = databaseHelper.findAllCategories();
        Map<String, BigDecimal> categorySums = new HashMap<>(categories.size());

        for (ExpenseCategory category : categories) {
            categorySums.put(category.getName(), new BigDecimal(BigInteger.ZERO));
        }

        for (String categoryName : categorySums.keySet()) {
            for (Expense expense : expenses) {
                if (expense.getCategory().getName().equals(categoryName)) {
                    categorySums.put(
                            categoryName, categorySums.get(categoryName).add(expense.getCost()));
                }
            }
        }

        List<String> removableCategories = new ArrayList<>();

        for (String categoryName : categorySums.keySet()) {
            if (categorySums.get(categoryName).equals(BigDecimal.ZERO)) {
                removableCategories.add(categoryName);
            }
        }

        for (String removableCategory : removableCategories) {
            categorySums.remove(removableCategory);
        }

        List<SliceValue> rawPieData = new ArrayList<>(categorySums.size());

        for (String categoryName : categorySums.keySet()) {
            rawPieData.add(new SliceValue(categorySums.get(categoryName).floatValue(), randomColor())
                    .setLabel(categoryName));
        }

        return new PieChartData(rawPieData).setHasLabels(true);
    }

    private int randomColor() {
        Random rand = new Random();
        int R = (int) (rand.nextFloat() * 255) + 1;
        int G = (int) (rand.nextFloat() * 255) + 1;
        int B = (int) (rand.nextFloat() * 255) + 1;

        return (0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }

    private class CategorySliceSelectListener implements PieChartOnValueSelectListener {
        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            Toast.makeText(getContext(), "Sum: " + value.getValue(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {

        }
    }
}
