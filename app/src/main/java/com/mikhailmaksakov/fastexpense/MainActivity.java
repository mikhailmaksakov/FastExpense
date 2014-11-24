package com.mikhailmaksakov.fastexpense;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.method.CharacterPickerDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private fastExpenseDatabaseAccessHelper currentDBAccessHelper;

    private listSelectionFragment mlistSelectionFragment;

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

        mlistSelectionFragment = new listSelectionFragment();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number){
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
//                currentDBAccessHelper.putExpense("123456", 1, 122.11);
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
        }
    }

    public void onDrawerOpened(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void onCreateMainSectionView(final View _context, int number){

        switch (number) {
            case 1:
                break;
            case 2:

//                EditText et = (EditText)(_context).findViewById(R.id.newExpense_ExpenseType_edit);
//
//                et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                    @Override
//                    public void onFocusChange(View v, boolean hasFocus) {
//                        if (hasFocus)
//                            Toast.makeText(getApplicationContext(), "Закончили редактирование", Toast.LENGTH_SHORT).show();
//                    }
//                });


                break;
            case 3:


                break;
            case 4:

                String currentString = "";

                ArrayList result = currentDBAccessHelper.getTransactionsText();

                String[] lvArray = new String[result.size()];

                for (int index = 0; index < result.size(); index++){

                    currentString = "";

                    JSONObject currentMap = (JSONObject) result.get(index);

                    try {
                        currentString = "time " + currentMap.getString(fastExpenseDatabaseAccessHelper.DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP)
                                + "; type " + currentMap.getInt(fastExpenseDatabaseAccessHelper.DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPE)
                                + "; transaction type " + currentMap.getInt(fastExpenseDatabaseAccessHelper.DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID)
                                + "; sum " + currentMap.getDouble(fastExpenseDatabaseAccessHelper.DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    lvArray[index] = currentString;

                    currentMap = null;

                };

                ArrayAdapter<String> adapter;
                ListView lv = (ListView)_context.findViewById(R.id.listView);

                try {
                    adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item1, lvArray);
                    lv.setAdapter(adapter);

                } catch (Exception e){

                    Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                    toast.show();

                }

                break;
        }

    }

    public void onCreateSelectionListView(final View listSelectionView, String currentList, int currentTransactionTypeID){

        ListView currentListView = (ListView)listSelectionView.findViewById(R.id.selectionList);

        ArrayList<HashMap<String, Object>> result = currentDBAccessHelper.getExpenseTypesList();

        String[] lvEmptyArray = new String[1];

        lvEmptyArray[0] = "Справочник видов расходов не заполнен";

        ArrayAdapter<String> emptyAdapter;
        emptyAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item1, lvEmptyArray);

        SimpleAdapter listAdapter = new SimpleAdapter(listSelectionView.getContext().getApplicationContext(), result, R.layout.simple_list_item1, new String[]{fastExpenseDatabaseAccessHelper.DATABASE_TABLE_EXPENSETYPES_FIELD_NAME}, new int[] {R.id.simplelistitem_main});

        if (result.size() == 0){
            currentListView.setAdapter(emptyAdapter);
        }
        else{
            currentListView.setAdapter(listAdapter);
        }

        currentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), parent.getAdapter().getItem(position).toString(), Toast.LENGTH_SHORT).show();
                parent.setSelection(position);
            }
        });
    }

    public void onMainSectionPause(View view){
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

    public void OnClick_newExpense_ExpenseType(View view) {

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, mlistSelectionFragment.getExpenseSelectionFragment(0))
                .commit();

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                    Bundle savedInstanceState) {

            int currentSection = getArguments().getInt(ARG_SECTION_NUMBER);

            int currentLayout = R.layout.fragment_main;

            switch (currentSection){
                case 1:
                    break;
                case 2:
                    currentLayout = R.layout.fragment_new_expense;
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }

            View rootView = inflater.inflate(currentLayout, container, false);
            ((MainActivity) container.getContext()).onCreateMainSectionView(rootView, getArguments().getInt(ARG_SECTION_NUMBER));

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }

        @Override
        public void onPause() {
            super.onPause();
            ((MainActivity) this.getView().getContext()).onMainSectionPause(this.getView());
        }
    }

    public static class listSelectionFragment extends Fragment{

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_CURRENT_LIST = "current_list";
        private static final String ARG_CURRENT_TRANSACTIONTYPEID = "current_transaction_type_id";

        private static final String ARG_LIST_EXPENSE = "list_expense";
        private static final String ARG_LIST_REVENUE = "list_revenue";

        private static final int SELECTION_LIST_LAYOUT = R.layout.selectionlist;
//
        private String currentList;
        private int currentTransactionTypeID;

        public listSelectionFragment() {
        }

        public listSelectionFragment getExpenseSelectionFragment(int currentTransactionTypeID){

            Bundle args = new Bundle();
            args.putString(ARG_CURRENT_LIST, ARG_LIST_EXPENSE);
            args.putInt(ARG_CURRENT_TRANSACTIONTYPEID, currentTransactionTypeID);
            this.setArguments(args);
            return this;

        };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            String currentList = getArguments().getString(ARG_CURRENT_LIST);
            int currentTransactionTypeID = getArguments().getInt(ARG_CURRENT_TRANSACTIONTYPEID);

//            int currentLayout = R.layout.selectionlist;
//
//            switch (currentList){
//                case ARG_LIST_EXPENSE:
//                    break;
//                case ARG_LIST_REVENUE:
//                    currentLayout = SELECTION_LIST_LAYOUT;
//                    break;
//                case 3:
//                    break;
//                case 4:
//                    break;
//            }

            View rootView;

            rootView = null;

            try {
                rootView = inflater.inflate(SELECTION_LIST_LAYOUT, container, false);
                ((MainActivity) container.getContext()).onCreateSelectionListView(rootView, currentList, currentTransactionTypeID);
            }
            catch (Exception e){
                Toast.makeText(((MainActivity) container.getContext()), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            return rootView;

        }
    }

}
