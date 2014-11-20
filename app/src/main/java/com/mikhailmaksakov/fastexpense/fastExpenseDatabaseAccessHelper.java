package com.mikhailmaksakov.fastexpense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maksakovMN on 20.11.2014.
 */
public class fastExpenseDatabaseAccessHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "fastExpenseDB";
    private static final int DATABASE_VERSION = 1;

    private static final int TRANSACTIONTYPE_EXPENSE = 1;
    private static final int TRANSACTIONTYPE_REVENUE = 2;
    private static final int TRANSACTIONTYPE_TRANSFER = 3;

    private static final String DATABASE_TABLE_EXPENSETYPES = "expense_types";

    private static final String DATABASE_TABLE_EXPENSETYPES_FIELD_ID = "_id";
    private static final String DATABASE_TABLE_EXPENSETYPES_FIELD_NAME = "name";
    private static final int DATABASE_TABLE_EXPENSETYPES_FIELD_NAME_LENGTH = 100;

    private static final String DATABASE_TABLE_REVENUETYPES = "revenue_types";

    private static final String DATABASE_TABLE_REVENUETYPES_FIELD_ID = "_id";
    private static final String DATABASE_TABLE_REVENUETYPES_FIELD_NAME = "name";
    private static final int DATABASE_TABLE_REVENUETYPES_FIELD_NAME_LENGTH = 100;

    private static final String DATABASE_TABLE_TRANSACTIONLIST = "operation_list";

    private static final String DATABASE_TABLE_TRANSACTIONLIST_FIELD_ID = "_id";
    private static final String DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP = "trancastion_datetime";
    private static final String DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPE = "trancastion_type"; // TRANSACTIONTYPE_...
    private static final String DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID = "trancastion_type_id";
    private static final String DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM = "trancastion_sum";

    public fastExpenseDatabaseAccessHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + DATABASE_TABLE_EXPENSETYPES
                + " (" + DATABASE_TABLE_EXPENSETYPES_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATABASE_TABLE_EXPENSETYPES_FIELD_NAME + " VARCHAR(" + DATABASE_TABLE_EXPENSETYPES_FIELD_NAME_LENGTH + ") );");

        db.execSQL("CREATE TABLE " + DATABASE_TABLE_REVENUETYPES
                + " (" + DATABASE_TABLE_REVENUETYPES_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATABASE_TABLE_REVENUETYPES_FIELD_NAME + " VARCHAR(" + DATABASE_TABLE_REVENUETYPES_FIELD_NAME_LENGTH + ") );");

        db.execSQL("CREATE TABLE " + DATABASE_TABLE_TRANSACTIONLIST
                + " (" + DATABASE_TABLE_TRANSACTIONLIST_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP + " VARCHAR(23),"
                + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPE + " INTEGER,"
                + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID + " INTEGER,"
                + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM + " DOUBLE"
                + " );");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void putExpense(String timeStamp, int expenseTypeID, double sum) {

        SQLiteDatabase writableDB = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP, timeStamp);
        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPE, TRANSACTIONTYPE_EXPENSE);
        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID, expenseTypeID);
        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM, sum);

        writableDB.insert(DATABASE_TABLE_TRANSACTIONLIST, null, values);

        writableDB.close();

    }

    public ArrayList<HashMap<String, String>> getTransactionsText(){

        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

        SQLiteDatabase readableDB = getReadableDatabase();

        Cursor cursor = readableDB.rawQuery("SELECT * FROM " + DATABASE_TABLE_TRANSACTIONLIST, null);

        while (cursor.moveToNext()){

            HashMap<String, String> currentMap = new HashMap<String, String>();

            currentMap.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP, cursor.getString(cursor.getColumnIndex(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP)));
            currentMap.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPE, String.valueOf(cursor.getInt(cursor.getColumnIndex(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPE))));
            currentMap.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID, String.valueOf(cursor.getInt(cursor.getColumnIndex(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID))));
            currentMap.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM, String.valueOf(cursor.getDouble(cursor.getColumnIndex(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM))));

            result.add(currentMap);

        }

        cursor.close();
        readableDB.close();

        return result;

    }

}
