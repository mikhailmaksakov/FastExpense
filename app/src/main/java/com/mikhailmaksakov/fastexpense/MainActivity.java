package com.mikhailmaksakov.fastexpense;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Layout;
import android.text.format.DateFormat;
import android.text.method.CharacterPickerDialog;
import android.view.Gravity;
import android.view.KeyEvent;
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
import java.util.Date;
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

    public expenseTypesListFragment mCurrentExpenseTypesListFragment;
    public expenseFragment mCurrentExpenseFragment;

    private static final String EXPENSE_SELECTION_RECEIVER_NEW_EXPENSE = "new_expense";

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
                break;
            case 3:
                break;
            case 4:

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
    protected void onPause() {
        super.onPause();

        // save all changes if they haven't been saved before
        if (mCurrentExpenseFragment != null && !mCurrentExpenseFragment.mChangesSaved){
            if (mCurrentExpenseFragment.saveChangesToDatabase())
                mCurrentExpenseFragment.clearNewExpense();

        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        currentDBAccessHelper.close();

    }

    public void makeToast(String message, int duration){
        Toast.makeText(getApplicationContext(), message, duration).show();
    }

    public String getExpenseTypeNameByID(int expenseTypeID){

        return currentDBAccessHelper.getExpenseTypeNameByID(expenseTypeID);

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

        private static final int LIST_LAYOUT = R.layout.expensetypeslist;
        private static final int LIST_ITEM_LAYOUT = R.layout.expensetypeslistitem;
        private static final int LIST_ITEM_ID = R.id.expenseTypesListItem;

        private static final String ARG_SELECTION_MODE = "selection_mode";

        private fastExpenseDatabaseAccessHelper mcurrentDBAccessHelper;

        private ArrayList<HashMap<String, Object>> mExpenseTypesListData;

        private SimpleAdapter mCurrentListAdapter;

        private ListView mCurrentListView;

        private int mListItemToMakeAction;

        public expenseTypesListFragment() {

        }

        public static expenseTypesListFragment NewExpenseTypesListFragment() {

            expenseTypesListFragment fragment = new expenseTypesListFragment();

            Bundle args = new Bundle();
            args.putBoolean(ARG_SELECTION_MODE, false);
            fragment.setArguments(args);

            return fragment;

        }

        public static expenseTypesListFragment NewExpenseTypeSelectionListFragment() {

            expenseTypesListFragment fragment = new expenseTypesListFragment();

            Bundle args = new Bundle();
            args.putBoolean(ARG_SELECTION_MODE, true);
            fragment.setArguments(args);

            return fragment;

        }

        @Override
        public void onAttach(Activity activity) {

            super.onAttach(activity);

            setHasOptionsMenu(true);
            setRetainInstance(true);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(LIST_LAYOUT, container, false);
            mCurrentListView = (ListView)rootView.findViewById(R.id.expenseTypesList);

            mcurrentDBAccessHelper = ((MainActivity) getActivity()).currentDBAccessHelper;

            renewExpenseTypesList();

            if (getArguments().getBoolean(ARG_SELECTION_MODE)) {

                mCurrentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Bundle args = new Bundle();
                        args.putInt("ExpenseTypeID", getExpenseListItemId(position));

                        MainActivity mainActivity = (MainActivity) getActivity();

                        mainActivity.mCurrentExpenseFragment.setArguments(args);

                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, mainActivity.mCurrentExpenseFragment)
                                .commit();

                    }
                });

            }
            mCurrentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

                    final SimpleAdapter adapter = ((SimpleAdapter) parent.getAdapter());

                    mListItemToMakeAction = position;

                    AlertDialog.Builder question = new AlertDialog.Builder(getActivity());

                    question.setMessage(getString(R.string.expenseTypesActionMessage));
                    question.setCancelable(true);

                    DialogInterface.OnClickListener onDeleteClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteCurrentExpenseType();
                        }
                    };
                    question.setPositiveButton(getString(R.string.wordDelete), onDeleteClickListener);

                    DialogInterface.OnClickListener onRenameClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            renameExpense();
                        }
                    };
                    question.setNeutralButton(getString(R.string.wordRename), onRenameClickListener);

                    question.create();
                    question.show();

                    return true;
                }
            });

            return rootView;

        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            if (!((MainActivity) getActivity()).mNavigationDrawerFragment.isDrawerOpen()) {
                inflater.inflate(R.menu.fastexpensetypeslistmenu, menu);
            }
            else
                super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == R.id.newExpenseType){
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

        public void renameExpense(){

            final AlertDialog.Builder request = new AlertDialog.Builder(getActivity());

            final EditText editText = new EditText(getActivity());

            DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (!editText.getText().toString().isEmpty()){
                        mcurrentDBAccessHelper.renameExpenseType(getExpenseListItemId(mListItemToMakeAction), editText.getText().toString());
                        renewExpenseTypesList();
                    }

                }
            };

            request.setCancelable(true);
            request.setMessage(getString(R.string.newNameOfExpenseDialogText));
            request.setPositiveButton(getString(R.string.wordOK), clickListener);

            request.setView(editText);

            request.create();

            request.show();

        }

        public void deleteCurrentExpenseType(){

            mcurrentDBAccessHelper.deleteExpenseType(getExpenseListItemId(mListItemToMakeAction));
            renewExpenseTypesList();

        }

        private int getExpenseListItemId(int position){

            HashMap selectedValue = (HashMap) mCurrentListAdapter.getItem(position);
            Integer selectedID = (Integer) selectedValue.get(fastExpenseDatabaseAccessHelper.DATABASE_TABLE_EXPENSETYPES_FIELD_ID);

            return selectedID.intValue();

        }

    }

    public static class expenseFragment extends Fragment{

        private static final int LAYOUT = R.layout.fragment_new_expense;

        private static final String ARGKEY_SELECTEDEXPENSETYPEID = "ExpenseTypeID";

        private MainActivity mMainActivity;

        private int mCurrentOperationID;
        private String mCurrentOperationDateTimeStamp;
        private HashMap<String, Object> mSelectedExpenseType;
        private Float mSelectedSum;

        public boolean mChangesSaved = true;

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

            setHasOptionsMenu(true);
            setRetainInstance(true);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            EditText expenseTypeSelectionField;

            final View rootView = inflater.inflate(LAYOUT, container, false);

//            mMainActivity = (MainActivity) getActivity();

            expenseTypeSelectionField = (EditText)rootView.findViewById(R.id.newExpense_ExpenseType_edit);

            expenseTypeSelectionField.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    expenseTypesListFragment expenseTypesListFragment = MainActivity.expenseTypesListFragment.NewExpenseTypeSelectionListFragment();

//                    Bundle args = new Bundle();
//                    args.putBoolean(mMainActivity., true);
//                    expenseTypesListFragment.setArguments(args);

                    // update the main content by replacing fragments
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, expenseTypesListFragment)
                            .commit();
                }
            });

            if (mCurrentOperationID != 0){
                readCurrentOperationData();
            }


            if (getArguments() != null && getArguments().containsKey(ARGKEY_SELECTEDEXPENSETYPEID)){

                mSelectedExpenseType = new HashMap<String, Object>();
                mSelectedExpenseType.put("_id", getArguments().getInt("ExpenseTypeID"));
                mSelectedExpenseType.put("name", mMainActivity.getExpenseTypeNameByID(getArguments().getInt("ExpenseTypeID")));

                mChangesSaved = false;

                getArguments().remove(ARGKEY_SELECTEDEXPENSETYPEID);

            }

//            if (mChangesSaved)
//                clearNewExpense();

            return rootView;

        }

        @Override
        public void onResume() {
            super.onResume();

//            if (mChangesSaved)
//                clearNewExpense();

        }

        @Override
        public void onStart() {

            super.onStart();

            showSavedData();

        }

        @Override
        public void onPause() {

            super.onPause();

            SaveSum();

        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
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

                // Проверить, что всё заполнено
                if (saveChangesToDatabase()) {
                    clearNewExpense();
                    showSavedData();
                }

                return true;
            }
            else
                return super.onOptionsItemSelected(item);
        }

        public Boolean saveChangesToDatabase() {

            if (mSelectedExpenseType != null && mSelectedSum != null && mSelectedSum.intValue() != 0 && mSelectedExpenseType != null ) {
                mCurrentOperationID = mMainActivity.currentDBAccessHelper.putExpense(mCurrentOperationDateTimeStamp, (Integer) mSelectedExpenseType.get("_id"), mSelectedSum);
                mChangesSaved = true;
                return true;
            }

            return false;

        }

        private void readCurrentOperationData(){
            if (mCurrentOperationID != 0){

                HashMap<String, Object> transactionParameters = mMainActivity.currentDBAccessHelper.getTransactionParameters(mCurrentOperationID);
                
//                transactionParameters.get(mMainActivity.currentDBAccessHelper.);

            }
        }

        private void clearNewExpense(){

            mCurrentOperationID = 0;
            mCurrentOperationDateTimeStamp = (String) DateFormat.format(getString(R.string.operationDateTimeStampFormat), new Date());
            mSelectedExpenseType = null;
            mSelectedSum = Float.valueOf(0);
            mChangesSaved = true;

        }

        private void showSavedData(){

            EditText expense = (EditText) getActivity().findViewById(R.id.newExpense_ExpenseType_edit);
            EditText sum = (EditText)getActivity().findViewById(R.id.new_Expense_Sum_edit);

            if (mSelectedExpenseType != null)
                expense.setText(mSelectedExpenseType.get("name").toString());
            else
                expense.setText(getString(R.string.new_expense_ExpenseTypeDefault_text));

            if (mSelectedSum != null && mSelectedSum.intValue() != 0)
                sum.setText(mSelectedSum.toString());
            else
                sum.setText("");

        }

        private void SaveSum(){
            EditText v = (EditText)getActivity().findViewById(R.id.new_Expense_Sum_edit);

            if (!v.getText().toString().isEmpty() && Float.parseFloat(((EditText) v).getText().toString()) != mSelectedSum){
                mSelectedSum = Float.parseFloat(((EditText) v).getText().toString());
                mChangesSaved =false;
            }

        }


    }

}
