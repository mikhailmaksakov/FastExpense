package com.mikhailmaksakov.fastexpense;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.method.CharacterPickerDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

//import org.json.JSONException;
//import org.json.JSONObject;

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

    private expenseTypesListFragment mCurrentExpenseTypesListFragment;

//    private listSelectionFragment mlistSelectionFragment;

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

//        mlistSelectionFragment = new listSelectionFragment();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Fragment currentFragment = null;

        switch (position){
            case 0:
                currentFragment = new Fragment();
                break;
            case 1:
                currentFragment = new Fragment();
                break;
            case 2:
                currentFragment = new Fragment();
                break;
            case 3:
                currentFragment = new Fragment();
                break;
            case 4:
                if (mCurrentExpenseTypesListFragment == null){
                    mCurrentExpenseTypesListFragment = expenseTypesListFragment.NewExpenseTypesListFragment();
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

//                String currentString = "";
//
//                ArrayList result = currentDBAccessHelper.getTransactionsText();
//
//                String[] lvArray = new String[result.size()];
//
//                for (int index = 0; index < result.size(); index++){
//
//                    currentString = "";
//
//                    JSONObject currentMap = (JSONObject) result.get(index);
//
//                    try {
//                        currentString = "time " + currentMap.getString(fastExpenseDatabaseAccessHelper.DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP)
//                                + "; type " + currentMap.getInt(fastExpenseDatabaseAccessHelper.DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPE)
//                                + "; transaction type " + currentMap.getInt(fastExpenseDatabaseAccessHelper.DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID)
//                                + "; sum " + currentMap.getDouble(fastExpenseDatabaseAccessHelper.DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    lvArray[index] = currentString;
//
//                    currentMap = null;
//
//                };
//
//                ArrayAdapter<String> adapter;
//                ListView lv = (ListView)_context.findViewById(R.id.listView);
//
//                try {
//                    adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item1, lvArray);
//                    lv.setAdapter(adapter);
//
//                } catch (Exception e){
//
//                    Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
//                    toast.show();
//
//                }
//
//                break;
        }

    }

    public void onCreateSelectionListView(final View listSelectionView, String currentList, int currentTransactionTypeID){

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

    public void makeToast(String message, int duration){
        Toast.makeText(getApplicationContext(), message, duration).show();

    }

    public void OnClick_newExpense_ExpenseType(View view) {

//        // update the main content by replacing fragments
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, mlistSelectionFragment.getExpenseSelectionFragment(0))
//                .commit();

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

    public static class expenseTypesListFragment extends Fragment{

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_CURRENT_LIST = "current_list";
        private static final String ARG_CURRENT_TRANSACTIONTYPEID = "current_transaction_type_id";

        private static final int LIST_LAYOUT = R.layout.expensetypeslist;
        private static final int LIST_ITEM_LAYOUT = R.layout.expensetypeslistitem;
        private static final int LIST_ITEM_ID = R.id.expenseTypesListItem;

        private int currentTransactionTypeID;
        private fastExpenseDatabaseAccessHelper mcurrentDBAccessHelper;

        private ArrayList<HashMap<String, Object>> mExpenseTypesListData;

        private SimpleAdapter mCurrentListAdapter;

        private ListView mCurrentListView;

        public expenseTypesListFragment() {

        }

        public static expenseTypesListFragment NewExpenseTypesListFragment() {

            expenseTypesListFragment fragment = new expenseTypesListFragment();

            return fragment;

        }

        @Override
        public void onAttach(Activity activity) {

            super.onAttach(activity);

            mcurrentDBAccessHelper = ((MainActivity) activity).currentDBAccessHelper;

//            renewExpenseTypesList();
            setHasOptionsMenu(true);
            setRetainInstance(true);

        }

        //        public expenseTypesListFragment getExpenseTypesListFragment(){

//            Bundle args = new Bundle();
//            args.putString(ARG_CURRENT_LIST, ARG_LIST_EXPENSE);
//            args.putInt(ARG_CURRENT_TRANSACTIONTYPEID, currentTransactionTypeID);
//            this.setArguments(args);
//            return this;

//        };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(LIST_LAYOUT, container, false);
            mCurrentListView = (ListView)rootView.findViewById(R.id.expenseTypesList);

            renewExpenseTypesList();

            mCurrentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity().getApplicationContext(), parent.getAdapter().getItem(position).toString(), Toast.LENGTH_SHORT).show();
                    parent.setSelection(position);
                }
            });

            mCurrentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

                    final SimpleAdapter adapter = ((SimpleAdapter) parent.getAdapter());

                    AlertDialog.Builder question = new AlertDialog.Builder(getActivity());

                    question.setMessage(getString(R.string.expenseTypesDeleteMessage));
                    question.setCancelable(true);
                    question.setPositiveButton(getString(R.string.wordYes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mExpenseTypesListData.remove(adapter.getItem(position));
                       }
                    });

                    renewExpenseTypesList();

                    return true;
                }
            });

            return rootView;

        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            if (!((MainActivity) getActivity()).mNavigationDrawerFragment.isDrawerOpen()) {
                inflater.inflate(R.menu.fastexpensetypeslistmenu, menu);

//                View newExpenseButton = (View)this.getActivity().findViewById(R.id.newExpense);
//
//                newExpenseButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        newExpenseRequest();
//                    }
//                });

            }
            else
                super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == R.id.newExpense){
                newExpenseRequest();
                return true;
            }
            else
                return super.onOptionsItemSelected(item);
        }

        private void renewExpenseTypesList(){

            mExpenseTypesListData = mcurrentDBAccessHelper.getExpenseTypesList();
            mCurrentListAdapter = new SimpleAdapter(getActivity().getApplicationContext(), mExpenseTypesListData, LIST_ITEM_LAYOUT, new String[]{mcurrentDBAccessHelper.DATABASE_TABLE_EXPENSETYPES_FIELD_NAME}, new int[] {LIST_ITEM_ID});
            mCurrentListView.setAdapter(mCurrentListAdapter);
        }

        public void newExpenseRequest(){

            final AlertDialog.Builder request = new AlertDialog.Builder(getActivity());

            final EditText editText = new EditText(getActivity());

            DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (!editText.getText().toString().isEmpty()){
                        mcurrentDBAccessHelper.putExpenseType(editText.getText().toString());
                        renewExpenseTypesList();
                    }

                }
            };

            request.setCancelable(true);
            request.setMessage(getString(R.string.newExpenseNameDialogText));
            request.setPositiveButton(getString(R.string.wordOK), clickListener);

            request.setView(editText);

            request.create();

            request.show();

        }

    }

}
