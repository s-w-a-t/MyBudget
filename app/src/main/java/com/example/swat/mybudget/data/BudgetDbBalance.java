package com.example.swat.mybudget.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.swat.mybudget.data.BudgetContract.*;

/**
 * Created by swat on 06.07.2015.
 */
public class BudgetDbBalance extends SQLiteOpenHelper {

    //version db
    private static final int DATABASE_VERSION = 1;

    //name db
    static final String DATABASE_NAME = "balance.db";

    public BudgetDbBalance(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //Create table for negative balance
        sqLiteDatabase.execSQL("CREATE TABLE " + NegativeEntry.TABLE_NAME
                + "("
                + NegativeEntry.COLUMN_COUNTER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NegativeEntry.COLUMN_DATE + " TEXT, "
                + NegativeEntry.COLUMN_VALUE + " TEXT"
                + ");"
        );

        //Create table for shopping
        sqLiteDatabase.execSQL("CREATE TABLE " + ShoppingEntry.TABLE_NAME
                + "("
                + ShoppingEntry.COLUMN_COUNTER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ShoppingEntry.COLUMN_DATE + " TEXT, "
                + ShoppingEntry.COLUMN_TIME + " TEXT, "
                + ShoppingEntry.COLUMN_VALUE_NAME + " TEXT, "
                + ShoppingEntry.COLUMN_VALUE_COST + " INTEGER"
                + ");");

        //Create table for positive balance
        sqLiteDatabase.execSQL("CREATE TABLE " + PositiveEntry.TABLE_NAME
                + "("
                + PositiveEntry.COLUMN_COUNTER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PositiveEntry.COLUMN_DATE + " DATETIME DEFAULT CURRENT_DATE, "
                + PositiveEntry.COLUMN_VALUE+" INTEGER"
                + ");");

        //create table for reservation money
        sqLiteDatabase.execSQL("CREATE TABLE " + ReservEntry.TABLE_NAME
                + "("
                + ReservEntry.COLUMN_COUNTER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ReservEntry.COLUMN_DATE + " DATETIME DEFAULT CURRENT_DATE, "
                + ReservEntry.COLUMN_VALUE+" INTEGER"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
