package com.github.anthogis.meno;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/**
 * A fragment which displays pie charts created from database Expense data.
 *
 * @author Anton Höglund
 * @version 1.0
 * @since 1.0
 */
public class ChartsFragment extends Fragment {

    /**
     * The DatabaseHelper used in this fragment.
     */
    private DatabaseHelper databaseHelper;

    /**
     * The View which portrays a pie chart.
     */
    private PieChartView pieChartView;

    /**
     * Calls superclass onCreate and sets the title of the Activity this fragment is contained in.
     *
     * @param savedInstanceState see Android documentation for Fragment.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_charts);
    }

    /**
     * Creates the content and sets listeners for this fragment.
     *
     * @param inflater see Android documentation for Fragment.
     * @param container see Android documentation for Fragment.
     * @param savedInstanceState see Android documentation for Fragment.
     * @return the view for the fragment's UI.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charts, container, false);
        databaseHelper = ((MenoApplication) getActivity().getApplication()).getDatabaseHelper();
        pieChartView = view.findViewById(R.id.chart);
        pieChartView.setPieChartData(createPieCharData());
        pieChartView.setOnValueTouchListener(new CategorySliceSelectListener());
        return view;
    }

    /**
     * Creates data for the pie chart from database entries.
     *
     * @return the pie chart data.
     */
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

    /**
     * Returns a random color integer in standard Android encoding.
     * @return a random color integer.
     */
    private int randomColor() {
        Random rand = new Random();
        int R = (int) (rand.nextFloat() * 255) + 1;
        int G = (int) (rand.nextFloat() * 255) + 1;
        int B = (int) (rand.nextFloat() * 255) + 1;

        return (0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }

    /**
     * Listener class for selecting slices on the pie chart.
     */
    private class CategorySliceSelectListener implements PieChartOnValueSelectListener {

        /**
         * Displays a toast of the value in the selected pie chart slice.
         *
         * @param arcIndex the index of the slice in the pie chart.
         * @param value the SliceValue of the selected slice.
         */
        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            Toast.makeText(getContext(), "Sum: " + value.getValue(), Toast.LENGTH_SHORT).show();
        }

        /**
         * Mandatory method implemented, does nothing.
         */
        @Override
        public void onValueDeselected() {

        }
    }
}
