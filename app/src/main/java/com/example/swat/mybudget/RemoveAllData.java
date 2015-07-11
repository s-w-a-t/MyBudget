package com.example.swat.mybudget;

import android.database.sqlite.SQLiteDatabase;

import com.example.swat.mybudget.data.BudgetDbBalance;
import com.example.swat.mybudget.data.BudgetContract.ReservEntry;
import com.example.swat.mybudget.data.BudgetContract.NegativeEntry;
import com.example.swat.mybudget.data.BudgetContract.PositiveEntry;
import com.example.swat.mybudget.data.BudgetContract.ShoppingEntry;

/**
 * Created by swat on 11.07.2015.
 */
public class RemoveAllData {
    BudgetDbBalance budgetDbBalance;
    SQLiteDatabase sqLiteDatabase;

    RemoveAllData(BudgetDbBalance budgetDbBalance, SQLiteDatabase sqLiteDatabase) {
        this.budgetDbBalance = budgetDbBalance;
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public void removeAll() {

        //clear shopping table
        sqLiteDatabase.delete(ShoppingEntry.TABLE_NAME, null, null);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + ShoppingEntry.TABLE_NAME + "'");

        //clear positive table
        sqLiteDatabase.delete(PositiveEntry.TABLE_NAME, null, null);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + PositiveEntry.TABLE_NAME + "'");

        //clear negative table
        sqLiteDatabase.delete(NegativeEntry.TABLE_NAME, null, null);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + NegativeEntry.TABLE_NAME + "'");

        //clear reservation table
        sqLiteDatabase.delete(ReservEntry.TABLE_NAME, null, null);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + ReservEntry.TABLE_NAME + "'");
    }
}
