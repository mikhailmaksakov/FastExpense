package com.mikhailmaksakov.fastexpense;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.Date;
import java.util.HashMap;

public class Transaction {

    public static final int EXPENSE_TRANSACTION_TYPE_ID = 1;
    public static final int REVENUE_TRANSACTION_TYPE_ID = 2;

    public static final String EXPENSE_TRANSACTION_TYPE_NAME = "Расход";
    public static final String REVENUE_TRANSACTION_TYPE_NAME = "Поступление";

    public static final int SAVE_RESULT_OK = 0;
    public static final int SAVE_RESULT_NOTHING_TO_SAVE = 1;
    public static final int SAVE_RESULT_ERROR = 2;
    public static final int SAVE_RESULT_ERROR_FIELDS_INCOMPLETE = 3;

    private MainActivity mContext;

    private fastExpenseDatabaseAccessHelper mDBAccessHelper;

    private Date mTransactionDate = new Date();

    private int mTransactionID = 0;
    private String mTimeStamp = "";
    private String mDate = "";
    private int mTransactionTypeID = 0;
    private String mTransactionTypeName = "";
    private int mTransactionItemID = 0; // expenseTypeID, revenueTypeID
    private String mTransactionItemName = "";
    private String mComment = ""; // expenseTypeID, revenueTypeID
    private float mSum = Float.valueOf(0);

    private boolean mDataModified = false;

    public Transaction(Context context){

        mContext = (MainActivity) context;
        mDBAccessHelper = mContext.currentDBAccessHelper;

    }

    public Transaction(Context context, int transactionTypeID, int transactionID) {

        mContext = (MainActivity) context;
        mDBAccessHelper = mContext.currentDBAccessHelper;

        if (transactionID > 0)
            readTransaction(transactionID);
        else if (transactionTypeID > 0) {
            switch (transactionTypeID){
                case EXPENSE_TRANSACTION_TYPE_ID:
                    initializeExpenseTransaction();
                    break;
                case REVENUE_TRANSACTION_TYPE_ID:
                    initializeRevenueTransaction();
                    break;
                default:

            }
        }

    }

    /* GETTING NEW INSTANCE */

    public static Transaction getNewExpenseTransaction(Context context){

        Transaction newInstance = new Transaction(context, EXPENSE_TRANSACTION_TYPE_ID, 0);

        return newInstance;

    }

    public static Transaction getNewRevenueTransaction(Context context){

        Transaction newInstance = new Transaction(context, REVENUE_TRANSACTION_TYPE_ID, 0);

        return newInstance;

    }

    public static Transaction getExistingTransaction(Context context, int transactionID){

        Transaction newInstance = new Transaction(context, 0, transactionID);

        return newInstance;

    }

    /* GETTING NEW INSTANCE */

    /* INSTANCE INITIALIZATION */

    private void initializeExpenseTransaction(){

        this.mTimeStamp = (String) DateFormat.format(mContext.getString(R.string.operationDateTimeStampFormat), mTransactionDate);
        this.mDate = (String) DateFormat.format(mContext.getString(R.string.operationDateFormat), mTransactionDate);
        this.mTransactionTypeID = EXPENSE_TRANSACTION_TYPE_ID;
        this.mTransactionTypeName = EXPENSE_TRANSACTION_TYPE_NAME;

    }

    private void initializeRevenueTransaction(){

        this.mTimeStamp = (String) DateFormat.format(mContext.getString(R.string.operationDateTimeStampFormat), mTransactionDate);
        this.mDate = (String) DateFormat.format(mContext.getString(R.string.operationDateFormat), mTransactionDate);
        this.mTransactionTypeID = REVENUE_TRANSACTION_TYPE_ID;
        this.mTransactionTypeName = REVENUE_TRANSACTION_TYPE_NAME;

    }

    /* INSTANCE INITIALIZATION */

    /* DATABASE OPERATIONS */

    private void readTransaction(int transactionID){

        HashMap<String, Object> transactionParameters = mDBAccessHelper.getTransactionParameters(transactionID);

        if (transactionParameters != null) {

            mTransactionID = transactionID;

            if (transactionParameters.containsKey(mDBAccessHelper.DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP)) {
                mTimeStamp = (String) transactionParameters.get(mDBAccessHelper.DATABASE_TABLE_TRANSACTIONLIST_FIELD_TIMESTAMP);
            }

            if (transactionParameters.containsKey("TRANSACTION_DATE")) {
                mDate = (String) transactionParameters.get("TRANSACTION_DATE");
            }

            if (transactionParameters.containsKey(mDBAccessHelper.DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID)) {
                mTransactionTypeID = (int) transactionParameters.get(mDBAccessHelper.DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONTYPEID);
            }

            if (transactionParameters.containsKey(mDBAccessHelper.DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONITEMTYPEID)) {
                mTransactionItemID = (int) transactionParameters.get(mDBAccessHelper.DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONITEMTYPEID);
            }

            if (transactionParameters.containsKey("TRANSACTION_ITEM_NAME")) {
                mTransactionItemName = (String) transactionParameters.get("TRANSACTION_ITEM_NAME");
            }

            if (transactionParameters.containsKey(mDBAccessHelper.DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM)) {
                mSum = (float) transactionParameters.get(mDBAccessHelper.DATABASE_TABLE_TRANSACTIONLIST_FIELD_TRANSACTIONSUM);
            }
        }
    }

    public int saveTransaction(){

        int result = SAVE_RESULT_OK;

        if (!mDataModified)
            return SAVE_RESULT_NOTHING_TO_SAVE;

        switch (mTransactionTypeID){

            case EXPENSE_TRANSACTION_TYPE_ID:

                if (isExpenseCompleted()) {
                    mTransactionID = mDBAccessHelper.putExpense(mTimeStamp, mTransactionItemID, mSum);
                    mDataModified = false;
                }
                else
                    result = SAVE_RESULT_ERROR_FIELDS_INCOMPLETE;

                break;

            case REVENUE_TRANSACTION_TYPE_ID:

                if (isRevenueCompleted()) {
                    mTransactionID = mDBAccessHelper.putRevenue(mTimeStamp, mTransactionItemID, mSum);
                    mDataModified = false;
                }
                else
                    result = SAVE_RESULT_ERROR_FIELDS_INCOMPLETE;

                break;

            default:
                result = SAVE_RESULT_ERROR;
        }

        return result;
    }

    /* DATABASE OPERATIONS */

    /* SETTING PARAMETERS FROM OUTSIDE */

    public void setTransactionItem(int transactionItemID){

        if (mTransactionItemID != transactionItemID)
            mDataModified = true;

        mTransactionItemID = transactionItemID;

        if (mTransactionTypeID == EXPENSE_TRANSACTION_TYPE_ID)
            mTransactionItemName = mDBAccessHelper.getExpenseTypeNameByID(mTransactionItemID);
        else if (mTransactionTypeID == REVENUE_TRANSACTION_TYPE_ID)
            mTransactionItemName = mDBAccessHelper.getRevenueTypeNameByID(mTransactionItemID);

    }

    public void setSum(float sum){

        if (mSum != sum)
            mDataModified = true;

        mSum = sum;
    }

    /* SETTING PARAMETERS FROM OUTSIDE */

/* GETTING PARAMETERS FROM OUTSIDE */

    public String getTransactionItemName(){
        return mTransactionItemName;
    }

    public Float getSum(){
        return mSum;
    }

    /* GETTING PARAMETERS FROM OUTSIDE */

    /* COMPLETE CHECK */

    private boolean isExpenseCompleted(){
        return (!mTimeStamp.isEmpty() && mTransactionItemID != 0 && mSum != Float.valueOf(0));
    }

    private boolean isRevenueCompleted(){
        return (!mTimeStamp.isEmpty() && mTransactionItemID != 0 && mSum != Float.valueOf(0));
    }

    /* COMPLETE CHECK */

}
