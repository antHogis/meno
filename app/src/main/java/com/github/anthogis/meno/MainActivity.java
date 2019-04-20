package com.github.anthogis.meno;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * The main Activity of the application.
 *
 * The main Activity of the application. Provides a container for Fragments and enables
 * navigation between them.
 *
 * @author Anton Höglund
 * @version 1.3
 * @since 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The BottomNavigationView of the Activity.
     */
    private BottomNavigationView navigation;

    /**
     * Listener for the BottomNavigationView.
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            boolean itemSelected = false;
            navigation.getMenu().getItem(0).setCheckable(true);

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

    /**
     * Sets the content for the Activity.
     *
     * Sets the layout for this Activity. Displays a StartupFragment if MenoApplication field
     * firstStarted is true, and sets it to false. Sets the listener for the BottomNavigationView
     * in the layout.
     *
     * @param savedInstanceState - See the Android documentation for Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        if (((MenoApplication) getApplication()).isFirstStarted()) {
            ((MenoApplication) getApplication()).setFirstStarted(false);
            navigation.getMenu().getItem(0).setCheckable(false);
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragment_container, new StartupFragment()).commit();
        }
    }
}
