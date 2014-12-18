package com.mikhailmaksakov.fastexpense;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpenseTypesListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LIST_LAYOUT = R.layout.expensetypeslist;
    private static final int LIST_ID = R.id.expenseTypesList;

    private static final int LIST_ITEM_LAYOUT = R.layout.expensetypeslistitem;
    private static final int LIST_ITEM_ID = R.id.expenseTypesList_id;
    private static final int LIST_ITEM_NAME = R.id.expenseTypesList_name;

    private static final String ARG_SELECTION_MODE = "selection_mode";

    private MainActivity mMainActivity;

    private fastExpenseDatabaseAccessHelper dbAccessHelper;

//    private fastExpenseDatabaseAccessHelper mcurrentDBAccessHelper;

    private ArrayList<HashMap<String, Object>> mExpenseTypesListData;

    private SimpleAdapter mCurrentListAdapter;

    private ListView mCurrentListView;

    private int mListItemToMakeAction;

    SimpleCursorAdapter scAdapter;

    public ExpenseTypesListFragment() {

    }

    public static ExpenseTypesListFragment NewExpenseTypesListFragment() {

        ExpenseTypesListFragment fragment = new ExpenseTypesListFragment();

        Bundle args = new Bundle();
        args.putBoolean(ARG_SELECTION_MODE, false);
        fragment.setArguments(args);

        return fragment;

    }

    public static ExpenseTypesListFragment NewExpenseTypeSelectionListFragment() {

        ExpenseTypesListFragment fragment = new ExpenseTypesListFragment();

        Bundle args = new Bundle();
        args.putBoolean(ARG_SELECTION_MODE, true);
        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainActivity = (MainActivity) getActivity();

        dbAccessHelper = mMainActivity.currentDBAccessHelper;

        String[] from = new String[] { dbAccessHelper.DATABASE_TABLE_EXPENSETYPES_FIELD_ID, dbAccessHelper.DATABASE_TABLE_EXPENSETYPES_FIELD_NAME };
        int[] to = new int[] { LIST_ITEM_ID, LIST_ITEM_NAME };
        scAdapter = new SimpleCursorAdapter(getActivity(), LIST_ITEM_LAYOUT, null, from, to, 0);

    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        setHasOptionsMenu(true);
//            setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(LIST_LAYOUT, container, false);

        ListView currentList = (ListView) rootView.findViewById(LIST_ID);
        currentList.setAdapter(scAdapter);

        if (getArguments().getBoolean(ARG_SELECTION_MODE)) {

            currentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (mMainActivity.mCurrentExpenseFragment != null) {

                        Bundle args = new Bundle();
                        args.putInt("ExpenseTypeID", getExpenseListItemId(position));

                        mMainActivity.mCurrentExpenseFragment.setArguments(args);

                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, mMainActivity.mCurrentExpenseFragment)
                                .commit();

                    }
                }
            });

        }
        currentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

//                mListItemToMakeAction = position;

                AlertDialog.Builder question = new AlertDialog.Builder(getActivity());

                question.setMessage(getString(R.string.expenseTypesActionMessage));
                question.setCancelable(true);

                DialogInterface.OnClickListener onDeleteClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCurrentExpenseType(position);
                    }
                };
                question.setPositiveButton(getString(R.string.wordDelete), onDeleteClickListener);

                DialogInterface.OnClickListener onRenameClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        renameExpense(position);
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
    public void onStart() {

        super.onStart();

        getLoaderManager().initLoader(0, null, this);

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

    public void newExpenseRequest(){

        final AlertDialog.Builder request = new AlertDialog.Builder(getActivity());

        final EditText editText = new EditText(getActivity());

        final ExpenseTypesListFragment expenseTypesListFragment = this;

        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!editText.getText().toString().isEmpty()){
                    dbAccessHelper.putExpenseType(editText.getText().toString());
                    getLoaderManager().restartLoader(0, null, expenseTypesListFragment);
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

    public void renameExpense(final int position){

        final AlertDialog.Builder request = new AlertDialog.Builder(getActivity());

        final EditText editText = new EditText(getActivity());

        final ExpenseTypesListFragment expenseTypesListFragment = this;

        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!editText.getText().toString().isEmpty()){
                    dbAccessHelper.renameExpenseType(getExpenseListItemId(position), editText.getText().toString());
                    getLoaderManager().restartLoader(0, null, expenseTypesListFragment);
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

    public void deleteCurrentExpenseType(int position){

        dbAccessHelper.deleteExpenseType(getExpenseListItemId(position));
        getLoaderManager().restartLoader(0, null, this);
    }

    private int getExpenseListItemId(int position){

        SQLiteCursor selectedValue = (SQLiteCursor)scAdapter.getItem(position);

        return selectedValue.getInt(selectedValue.getColumnIndex(dbAccessHelper.DATABASE_TABLE_EXPENSETYPES_FIELD_ID));

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(mMainActivity, mMainActivity.currentDBAccessHelper);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        scAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    static class MyCursorLoader extends CursorLoader {

        private fastExpenseDatabaseAccessHelper dbAccessHelper;

        public MyCursorLoader(Context context, fastExpenseDatabaseAccessHelper dbAccessHelper) {
            super(context);
            this.dbAccessHelper = dbAccessHelper;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = dbAccessHelper.getExpenseTypesList();
            return cursor;
        }

    }

}
