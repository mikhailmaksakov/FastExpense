package com.mikhailmaksakov.fastexpense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static final int TRANSACTIONTYPE_EXPENSE = 1;
    public static final int TRANSACTIONTYPE_REVENUE = 2;
    public static final int TRANSACTIONTYPE_TRANSFER = 3;

    public static final String DATABASE_TABLE_EXPENSETYPES = "expense_types";

    public static final String DATABASE_TABLE_EXPENSETYPES_FIELD_ID = "_id";
    public static final String DATABASE_TABLE_EXPENSETYPES_FIELD_NAME = "name";
    public static final int DATABASE_TABLE_EXPENSETYPES_FIELD_NAME_LENGTH = 100;

    public static final String DATABASE_TABLE_REVENUETYPES = "revenue_types";

    public static final String DATABASE_TABLE_REVENUETYPES_FIELD_ID = "_id";
    public static final String DATABASE_TABLE_REVENUETYPES_FIELD_NAME = "name";
    public static final int DATABASE_TABLE_REVENUETYPES_FIELD_NAME_LENGTH = 100;

    public static final String DATABASE_TABLE_TRANSACTIONLIST = "operation_list";

    public static final String DATABASE_TABLE_TRANSACTIONLIST_FIELD_ID = "_id";
    public static final String DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP = "trancastion_datetime";
    public static final String DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPE = "trancastion_type"; // TRANSACTIONTYPE_...
    public static final String DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID = "trancastion_type_id";
    public static final String DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM = "trancastion_sum";

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

        putTransaction(timeStamp, TRANSACTIONTYPE_EXPENSE, expenseTypeID, sum);

    }

    public void putRevenue(String timeStamp, int expenseTypeID, double sum) {

        putTransaction(timeStamp, TRANSACTIONTYPE_REVENUE, expenseTypeID, sum);

    }

//    public void putTransfer(String timeStamp, int expenseTypeID, int sourceAccountID, int receivingAccountID, double sum) {
//
//    }

    private void putTransaction(String timeStamp, int TRANSACTIONTYPE, int expenseTypeID, double sum) {

        SQLiteDatabase writableDB = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP, timeStamp);
        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPE, TRANSACTIONTYPE);
        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID, expenseTypeID);
        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM, sum);

        writableDB.insert(DATABASE_TABLE_TRANSACTIONLIST, null, values);

        writableDB.close();

    }

    public ArrayList<HashMap<String, Object>> getExpenseTypesList(){

        ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

        SQLiteDatabase readableDB = getReadableDatabase();
        Cursor cursor = readableDB.rawQuery("SELECT * FROM " + DATABASE_TABLE_EXPENSETYPES + " ORDER BY _id", null);

        while (cursor.moveToNext()){

            HashMap<String, Object> currentMap = new HashMap<String, Object>();

            currentMap.put(DATABASE_TABLE_EXPENSETYPES_FIELD_ID, cursor.getInt(cursor.getColumnIndex(DATABASE_TABLE_EXPENSETYPES_FIELD_ID)));
            currentMap.put(DATABASE_TABLE_EXPENSETYPES_FIELD_NAME, cursor.getString(cursor.getColumnIndex(DATABASE_TABLE_EXPENSETYPES_FIELD_NAME)));

            result.add(currentMap);

        }

        cursor.close();
        readableDB.close();

        return result;

    }


    public ArrayList<JSONObject> getTransactionsText() {

        ArrayList<JSONObject> result = new ArrayList<JSONObject>();

        SQLiteDatabase readableDB = getReadableDatabase();

        Cursor cursor = readableDB.rawQuery("SELECT * FROM " + DATABASE_TABLE_TRANSACTIONLIST, null);

        while (cursor.moveToNext()){

            JSONObject currentMap = new JSONObject();

            try {
                currentMap.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP, cursor.getString(cursor.getColumnIndex(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP)));
                currentMap.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPE, cursor.getInt(cursor.getColumnIndex(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPE)));
                currentMap.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID, cursor.getInt(cursor.getColumnIndex(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID)));
                currentMap.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM, cursor.getDouble(cursor.getColumnIndex(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM)));
            }catch (JSONException e) {
                e.printStackTrace();
            }


            result.add(currentMap);

        }

        cursor.close();
        readableDB.close();

        return result;

    }

}
