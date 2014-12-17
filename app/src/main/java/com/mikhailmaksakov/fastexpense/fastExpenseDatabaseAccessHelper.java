package com.mikhailmaksakov.fastexpense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import org.json.JSONException;
//import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.SSLEngineResult;

/**
 * Created by maksakovMN on 20.11.2014.
 */
public class fastExpenseDatabaseAccessHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "fastExpenseDB";
    private static final int DATABASE_VERSION = 3;

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

    public static final String DATABASE_TABLE_TRANSACTIONTYPES = "transaction_types";

    public static final String DATABASE_TABLE_TRANSACTIONTYPES_FIELD_ID = "_id";
    public static final String DATABASE_TABLE_TRANSACTIONTYPES_FIELD_NAME = "name";
    public static final int DATABASE_TABLE_TRANSACTIONTYPES_FIELD_NAME_LENGTH = 100;

    public static final String DATABASE_TABLE_TRANSACTIONLIST = "operation_list";

    public static final String DATABASE_TABLE_TRANSACTIONLIST_FIELD_ID = "_id";
    public static final String DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP = "transaction_datetime";
    public static final String DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID = "transaction_type_id"; // TRANSACTIONTYPE_...
    public static final String DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONITEMTYPEID = "transaction_item_type_id";
    public static final String DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM = "transaction_sum";

    public fastExpenseDatabaseAccessHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        createTables(db, true, true, true, true);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion == 2){

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_EXPENSETYPES);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_REVENUETYPES);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_TRANSACTIONLIST);

            onCreate(db);

        }
        else if (newVersion == 3){
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_TRANSACTIONLIST);
            createTables(db, false, false, true, true);
        }

    }

    private void createTables(SQLiteDatabase db, Boolean ExpenseTypes, Boolean RevenueTypes, Boolean TransactionTypes, Boolean TransactionList){

        if (ExpenseTypes)
            db.execSQL("CREATE TABLE " + DATABASE_TABLE_EXPENSETYPES
                    + " (" + DATABASE_TABLE_EXPENSETYPES_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DATABASE_TABLE_EXPENSETYPES_FIELD_NAME + " VARCHAR(" + DATABASE_TABLE_EXPENSETYPES_FIELD_NAME_LENGTH + ") );");

        if (RevenueTypes)
            db.execSQL("CREATE TABLE " + DATABASE_TABLE_REVENUETYPES
                    + " (" + DATABASE_TABLE_REVENUETYPES_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DATABASE_TABLE_REVENUETYPES_FIELD_NAME + " VARCHAR(" + DATABASE_TABLE_REVENUETYPES_FIELD_NAME_LENGTH + ") );");

        if (TransactionTypes)
            db.execSQL("CREATE TABLE " + DATABASE_TABLE_TRANSACTIONTYPES
                    + " (" + DATABASE_TABLE_TRANSACTIONTYPES_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DATABASE_TABLE_TRANSACTIONTYPES_FIELD_NAME + " VARCHAR(" + DATABASE_TABLE_TRANSACTIONTYPES_FIELD_NAME_LENGTH + ") );");

        if (TransactionList)
            db.execSQL("CREATE TABLE " + DATABASE_TABLE_TRANSACTIONLIST
                    + " (" + DATABASE_TABLE_TRANSACTIONLIST_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP + " VARCHAR(23),"
                    + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONITEMTYPEID + " INTEGER,"
                    + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID + " INTEGER,"
                    + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM + " DOUBLE"
                    + " );");

    }

    public int putExpense(String timeStamp, int expenseTypeID, float sum) {

        return putTransaction(timeStamp, TRANSACTIONTYPE_EXPENSE, expenseTypeID, sum);

    }

    public int putRevenue(String timeStamp, int expenseTypeID, float sum) {

        return putTransaction(timeStamp, TRANSACTIONTYPE_REVENUE, expenseTypeID, sum);

    }

//    public void putTransfer(String timeStamp, int expenseTypeID, int sourceAccountID, int receivingAccountID, double sum) {
//
//    }

    private int putTransaction(String timeStamp, int TRANSACTIONTYPE, int expenseTypeID, float sum) {

        int result = 0;

        SQLiteDatabase writableDB = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP, timeStamp);
        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID, TRANSACTIONTYPE);
        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONITEMTYPEID, expenseTypeID);
        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM, sum);

        long insertedRowID = writableDB.insert(DATABASE_TABLE_TRANSACTIONLIST, null, values);

        Cursor cursor = writableDB.rawQuery("SELECT " + DATABASE_TABLE_TRANSACTIONLIST_FIELD_ID + " FROM " + DATABASE_TABLE_TRANSACTIONLIST + " WHERE ROWID = ?", new String[]{Long.toString(insertedRowID)});

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            result = cursor.getInt(0);
        }

        cursor.close();
        writableDB.close();

        return result;

    }

    public void updateExpense(int transactionID, String timeStamp, int expenseTypeID, float sum) {

        SQLiteDatabase writableDB = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP, timeStamp);
        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID, TRANSACTIONTYPE_EXPENSE);
        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONITEMTYPEID, expenseTypeID);
        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM, sum);

        writableDB.update(DATABASE_TABLE_TRANSACTIONLIST, values, "_id = ?", new String[]{String.valueOf(transactionID)});

        writableDB.close();

    }

    public void updateRevenue(int transactionID, String timeStamp, int revenueTypeID, float sum) {

        SQLiteDatabase writableDB = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP, timeStamp);
        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID, TRANSACTIONTYPE_REVENUE);
        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONITEMTYPEID, revenueTypeID);
        values.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM, sum);

        writableDB.update(DATABASE_TABLE_TRANSACTIONLIST, values, "_id = ?", new String[]{String.valueOf(transactionID)});

        writableDB.close();

    }

    public HashMap<String, Object> getTransactionParameters(int transactionID){

        HashMap<String, Object> result = new HashMap<>();

        SQLiteDatabase readableDB = getReadableDatabase();

        Cursor cursor = readableDB.rawQuery("SELECT " + " TR_LIST." + DATABASE_TABLE_TRANSACTIONLIST_FIELD_ID + " AS " + DATABASE_TABLE_TRANSACTIONLIST_FIELD_ID + ", "
                + " strftime('%d.%m.%Y', TR_LIST." + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP + ") AS TRANSACTION_DATE, "
                + " TR_LIST." + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP + " AS " + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP + ", "
                + " TR_LIST." + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID + " AS " + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID + " ,"
                + " TR_LIST." + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONITEMTYPEID + " AS " + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONITEMTYPEID + " ,"
                + " EXP_TYPES." + DATABASE_TABLE_EXPENSETYPES_FIELD_NAME + " AS TRANSACTION_ITEM_NAME ,"
                + " TR_LIST." + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM + " AS " + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM
                + " FROM " + DATABASE_TABLE_TRANSACTIONLIST + " AS TR_LIST "
                + "LEFT JOIN " + DATABASE_TABLE_EXPENSETYPES + " AS EXP_TYPES "
                + " ON TR_LIST." + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID
                + " = EXP_TYPES." + DATABASE_TABLE_EXPENSETYPES_FIELD_ID + " WHERE _id = ?", new String[] {String.valueOf(transactionID)});

//        Cursor cursor = readableDB.rawQuery("SELECT * FROM " + DATABASE_TABLE_TRANSACTIONLIST + " AS TR_LIST LEFT JOIN  WHERE _id = ?", new String[] {String.valueOf(transactionID)});
//
        if (cursor.moveToFirst()){

            result.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_ID, cursor.getInt(cursor.getColumnIndex(DATABASE_TABLE_TRANSACTIONLIST_FIELD_ID)));
            result.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID, cursor.getInt(cursor.getColumnIndex(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID)));
            result.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONITEMTYPEID, cursor.getInt(cursor.getColumnIndex(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONITEMTYPEID)));
            result.put("TRANSACTION_ITEM_NAME", cursor.getInt(cursor.getColumnIndex("TRANSACTION_ITEM_NAME")));
            result.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP, cursor.getInt(cursor.getColumnIndex(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP)));
            result.put(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM, cursor.getInt(cursor.getColumnIndex(DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM)));

        }

        cursor.close();
        readableDB.close();

        return result;

    }

    public void putExpenseType(String name) {

        SQLiteDatabase writableDB = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DATABASE_TABLE_EXPENSETYPES_FIELD_NAME, name);

        writableDB.insert(DATABASE_TABLE_EXPENSETYPES, null, values);

        writableDB.close();

    }

    public void deleteExpenseType(int _id) {

        SQLiteDatabase writableDB = getWritableDatabase();

        writableDB.delete(DATABASE_TABLE_EXPENSETYPES, "_id = ?", new String[]{String.valueOf(_id)});

        // Удалять все связанные расходы или заменить на новый вид расхода

        writableDB.close();

    }

    public void renameExpenseType(int _id, String name) {

        SQLiteDatabase writableDB = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DATABASE_TABLE_EXPENSETYPES_FIELD_NAME, name);

        writableDB.update(DATABASE_TABLE_EXPENSETYPES, values, "_id = ?", new String[]{String.valueOf(_id)});

        // Удалять все связанные расходы или заменить на новый вид расхода

        writableDB.close();

    }

    public String getExpenseTypeNameByID(int expenseTypeID){

        String result = "";

        SQLiteDatabase readableDB = getReadableDatabase();
        Cursor cursor = readableDB.rawQuery("SELECT * FROM " + DATABASE_TABLE_EXPENSETYPES + " WHERE _id = ?", new String[] {String.valueOf(expenseTypeID)});

        if (cursor.moveToNext()){
            result = cursor.getString(cursor.getColumnIndex(DATABASE_TABLE_EXPENSETYPES_FIELD_NAME));
        }

        cursor.close();
        readableDB.close();

        return result;

    }

    public String getRevenueTypeNameByID(int revenueTypeID){

        String result = "";

        SQLiteDatabase readableDB = getReadableDatabase();
        Cursor cursor = readableDB.rawQuery("SELECT * FROM " + DATABASE_TABLE_REVENUETYPES + " WHERE _id = ?", new String[] {String.valueOf(revenueTypeID)});

        if (cursor.moveToNext()){
            result = cursor.getString(cursor.getColumnIndex(DATABASE_TABLE_REVENUETYPES_FIELD_NAME));
        }

        cursor.close();
        readableDB.close();

        return result;

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

    public Cursor getTransactionList(){

        SQLiteDatabase readableDB = getReadableDatabase();

        Cursor cursor = readableDB.rawQuery("SELECT " + " TR_LIST." + DATABASE_TABLE_TRANSACTIONLIST_FIELD_ID + " AS " + DATABASE_TABLE_TRANSACTIONLIST_FIELD_ID + ", "
                                                      + " strftime('%d.%m.%Y', TR_LIST." + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP + ") AS TRANSACTION_DATE, "
                                                      + " TR_LIST." + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID + " AS " + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID + " ,"
                                                      + " case "
                                                      + "    when TR_LIST." + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID + " == " + Transaction.EXPENSE_TRANSACTION_TYPE_ID + " then "
                                                      + "       '" + Transaction.EXPENSE_TRANSACTION_TYPE_NAME + "'"
                                                      + "    when TR_LIST." + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID + " == " + Transaction.REVENUE_TRANSACTION_TYPE_ID + " then "
                                                      + "       '" + Transaction.REVENUE_TRANSACTION_TYPE_NAME + "'"
                                                      + "    else ' ' "
                                                      + " end "
                                                      + " AS TRANSACTION_TYPE_NAME ,"
                                                      + " TR_LIST." + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONITEMTYPEID + " AS " + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONITEMTYPEID + " ,"
                                                      + " EXP_TYPES." + DATABASE_TABLE_EXPENSETYPES_FIELD_NAME + " AS " + DATABASE_TABLE_EXPENSETYPES_FIELD_NAME + " ,"
                                                      + " TR_LIST." + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM + " AS " + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM
                                            + " FROM " + DATABASE_TABLE_TRANSACTIONLIST + " AS TR_LIST "
                                                      + "LEFT JOIN " + DATABASE_TABLE_EXPENSETYPES + " AS EXP_TYPES "
                                                      + " ON TR_LIST." + DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONITEMTYPEID
                                                      + " = EXP_TYPES." + DATABASE_TABLE_EXPENSETYPES_FIELD_ID + " ORDER BY TR_LIST._id", null);

        return cursor;

    }

}
