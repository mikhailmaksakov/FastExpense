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
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
//import android.support.v4.content.CursorLoader;
//import android.support.v4.content.Loader;
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
import java.util.List;

public class TransactionListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LAYOUT = R.layout.transactionlist;
    private static final int LIST_ID = R.id.transactionList;
    private static final int LIST_ITEM_LAYOUT = R.layout.expensetypeslistitem;
    private static final int LIST_ITEM_ID = R.id.expenseTypesListItem;

    private static final String ARG_SELECTION_MODE = "selection_mode";

    private MainActivity mMainActivity;

    private static fastExpenseDatabaseAccessHelper dbAccessHelper;

    private ArrayList<HashMap<String, Object>> mExpenseTypesListData;

    private SimpleAdapter mCurrentListAdapter;

    private ListView mCurrentListView;

    private int mListItemToMakeAction;

    SimpleCursorAdapter scAdapter;

    public TransactionListFragment() {

    }

    public static TransactionListFragment NewTransactionListFragment() {

        TransactionListFragment fragment = new TransactionListFragment();

//        Bundle args = new Bundle();
//        args.putBoolean(ARG_SELECTION_MODE, false);
//        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        setHasOptionsMenu(true);
        setRetainInstance(true);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainActivity = (MainActivity) getActivity();

        dbAccessHelper = mMainActivity.currentDBAccessHelper;

//        mCurrentListView = (ListView)getActivity().findViewById(LIST_ID);
//
//        fastExpenseDatabaseAccessHelper dbAccessHelper = mMainActivity.currentDBAccessHelper;

        String[] from = new String[] { "TransactionDate", dbAccessHelper.DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID, dbAccessHelper.DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM, dbAccessHelper.DATABASE_TABLE_EXPENSETYPES_FIELD_NAME };
        int[] to = new int[] { R.id.transatcionListItem_DateTime, R.id.transatcionListItem_TransactionType, R.id.transatcionListItem_Sum, R.id.transatcionListItem_TransactionTypeName};
        scAdapter = new SimpleCursorAdapter(getActivity(), R.layout.transactionlistitem, null, from, to, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(LAYOUT, container, false);

        ListView transactionList = (ListView) rootView.findViewById(LIST_ID);
        transactionList.setAdapter(scAdapter);

        getLoaderManager().initLoader(0, null, this);

//        mCurrentListView = (ListView)rootView.findViewById(LIST_ID);

//        mcurrentDBAccessHelper = ((MainActivity) getActivity()).currentDBAccessHelper;
//
//        renewExpenseTypesList();
//
//        if (getArguments().getBoolean(ARG_SELECTION_MODE)) {
//
//            mCurrentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    Bundle args = new Bundle();
//                    args.putInt("ExpenseTypeID", getExpenseListItemId(position));
//
//                    MainActivity mainActivity = (MainActivity) getActivity();
//
//                    mainActivity.mCurrentExpenseFragment.setArguments(args);
//
//                    FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.container, mainActivity.mCurrentExpenseFragment)
//                            .commit();
//
//                }
//            });
//
//        }
//        mCurrentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//            @Override
//            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
//
//                final SimpleAdapter adapter = ((SimpleAdapter) parent.getAdapter());
//
//                mListItemToMakeAction = position;
//
//                AlertDialog.Builder question = new AlertDialog.Builder(getActivity());
//
//                question.setMessage(getString(R.string.expenseTypesActionMessage));
//                question.setCancelable(true);
//
//                DialogInterface.OnClickListener onDeleteClickListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        deleteCurrentExpenseType();
//                    }
//                };
//                question.setPositiveButton(getString(R.string.wordDelete), onDeleteClickListener);
//
//                DialogInterface.OnClickListener onRenameClickListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        renameExpense();
//                    }
//                };
//                question.setNeutralButton(getString(R.string.wordRename), onRenameClickListener);
//
//                question.create();
//                question.show();
//
//                return true;
//            }
//        });

        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        if (!((MainActivity) getActivity()).mNavigationDrawerFragment.isDrawerOpen()) {
//            inflater.inflate(R.menu.fastexpensetypeslistmenu, menu);
//        }
//        else
            super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.newExpenseType){
//            newExpenseRequest();
//            return true;
//        }
//        else
            return super.onOptionsItemSelected(item);
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
            Cursor cursor = dbAccessHelper.getTransactionList();
            return cursor;
        }

    }

}