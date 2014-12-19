package com.mikhailmaksakov.fastexpense;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    public NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public fastExpenseDatabaseAccessHelper currentDBAccessHelper;

    public ExpenseTypesListFragment mCurrentExpenseTypesListFragment;
    public expenseFragment mCurrentExpenseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        currentDBAccessHelper = new fastExpenseDatabaseAccessHelper(this);

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Fragment currentFragment = null;

        switch (position){
            case 0:
                currentFragment = new Fragment();
                break;
            case 1:
                if (mCurrentExpenseFragment == null){
                    mCurrentExpenseFragment = expenseFragment.newExpenseFragment();
                }
                currentFragment = mCurrentExpenseFragment;
                break;
            case 2:
                break;
            case 3:
                currentFragment = TransactionListFragment.NewTransactionListFragment();
                break;
            case 4:
                if (mCurrentExpenseTypesListFragment == null){
                    mCurrentExpenseTypesListFragment = ExpenseTypesListFragment.NewExpenseTypesListFragment();
                }
                currentFragment = mCurrentExpenseTypesListFragment;
                break;
        }

        if (currentFragment != null) {
            // update the main content by replacing fragments
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, currentFragment)
                    .commit();
        }
    }

    public void onSectionAttached(int number){
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
        }
    }

    public void onDrawerOpened(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        currentDBAccessHelper.close();

    }

    public void makeToast(String message, int duration){
        Toast.makeText(getApplicationContext(), message, duration).show();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        public PlaceholderFragment() {
//
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                    Bundle savedInstanceState) {
//
//            int currentSection = getArguments().getInt(ARG_SECTION_NUMBER);
//
//            int currentLayout = R.layout.fragment_main;
//
//            switch (currentSection){
//                case 1:
//                    break;
//                case 2:
//                    currentLayout = R.layout.fragment_new_expense;
//                    break;
//                case 3:
//                    break;
//                case 4:
//                    break;
//            }
//
//            View rootView = inflater.inflate(currentLayout, container, false);
//            ((MainActivity) container.getContext()).onCreateMainSectionView(rootView, getArguments().getInt(ARG_SECTION_NUMBER));
//
//            return rootView;
//        }
//
//        @Override
//        public void onAttach(Activity activity) {
//            super.onAttach(activity);
//            ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
//        }
//
//        @Override
//        public void onPause() {
//            super.onPause();
//            ((MainActivity) this.getView().getContext()).onMainSectionPause(this.getView());
//        }
//    }

    public static class expenseFragment extends Fragment{

        private static final int LAYOUT = R.layout.fragment_new_expense;

        private static final String ARGKEY_SELECTEDEXPENSETYPEID = "ExpenseTypeID";

        private MainActivity mMainActivity;

        private Transaction mCurrentTransaction;

        private EditText mSumEditText;

        public expenseFragment() {

        }

        public static expenseFragment newExpenseFragment() {

            expenseFragment fragment = new expenseFragment();

            return fragment;

        }

        @Override
        public void onAttach(Activity activity) {

            super.onAttach(activity);

            mMainActivity = (MainActivity) getActivity();


            if (mCurrentTransaction == null){
                mCurrentTransaction = Transaction.getNewExpenseTransaction(mMainActivity);
            }

            if (getArguments() != null && getArguments().containsKey(ARGKEY_SELECTEDEXPENSETYPEID)){

                mCurrentTransaction.setTransactionItem(getArguments().getInt("ExpenseTypeID"));
                getArguments().remove(ARGKEY_SELECTEDEXPENSETYPEID);

            }

            setHasOptionsMenu(true);
//            setRetainInstance(true);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            final View rootView = inflater.inflate(LAYOUT, container, false);

            EditText expenseTypeSelectionField = (EditText)rootView.findViewById(R.id.newExpense_ExpenseType_edit);
            mSumEditText = (EditText)rootView.findViewById(R.id.new_Expense_Sum_edit);

            expenseTypeSelectionField.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ExpenseTypesListFragment expenseTypesListFragment = ExpenseTypesListFragment.NewExpenseTypeSelectionListFragment();

                    // update the main content by replacing fragments
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, expenseTypesListFragment)
                            .commit();
                }
            });

            return rootView;

        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public void onStart() {

            super.onStart();

            showTransactionData();

        }

        @Override
        public void onPause() {

            super.onPause();

            SaveSum();

            mCurrentTransaction.saveTransaction();

        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            if (!mMainActivity.mNavigationDrawerFragment.isDrawerOpen()) {
                inflater.inflate(R.menu.newexpensemenu, menu);
            }
            else
                super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            if (item.getItemId() == R.id.newExpense){

                SaveSum();

                mCurrentTransaction.saveTransaction();

                mCurrentTransaction = Transaction.getNewExpenseTransaction(mMainActivity);

                showTransactionData();

                return true;
            }
            else
                return super.onOptionsItemSelected(item);
        }

        private void SaveSum(){

            if (!mSumEditText.getText().toString().isEmpty())
                mCurrentTransaction.setSum(Float.parseFloat(mSumEditText.getText().toString()));
            else
                mCurrentTransaction.setSum(Float.valueOf(0));

        }

        private void showTransactionData(){

            EditText expense = (EditText) getActivity().findViewById(R.id.newExpense_ExpenseType_edit);
            EditText sum = (EditText)getActivity().findViewById(R.id.new_Expense_Sum_edit);

            if (!mCurrentTransaction.getTransactionItemName().isEmpty())
                expense.setText(mCurrentTransaction.getTransactionItemName());
            else
                expense.setText(getString(R.string.new_expense_ExpenseTypeDefault_text));

            if (mCurrentTransaction.getSum().intValue() != 0)
                sum.setText(mCurrentTransaction.getSum().toString());
            else
                sum.setText("");

        }

    }

}
