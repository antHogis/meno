package com.github.anthogis.meno;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.Optional;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            boolean itemSelected = false;

            switch (item.getItemId()) {
                case R.id.navigation_categories:
                    selectedFragment = new CategoriesFragment();
                    itemSelected = true;
                    break;
                case R.id.navigation_add_expense:
                    selectedFragment = new AddExpenseFragment();
                    itemSelected = true;
                    break;
                case R.id.navigation_expense_list:
                    selectedFragment = new ExpenseListFragment();
                    itemSelected = true;
                    break;
                case R.id.navigation_chart:
                    selectedFragment = new ChartsFragment();
                    itemSelected = true;
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragment_container, selectedFragment).commit();

            return itemSelected;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(navListener);

        databaseHelper = new DatabaseHelper(this);
    }

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }
}
