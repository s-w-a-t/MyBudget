package com.example.swat.mybudget;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.swat.mybudget.data.BudgetDbBalance;

import static com.example.swat.mybudget.data.BudgetContract.*;

/**
 * Created by swat on 03.07.2015.
 */
public class NegBalance extends ActionBarActivity {

    Button btnClearNegVal;
    ListView lvNegBal;
    BudgetDbBalance budgetDbNegBal;
    SQLiteDatabase db;
    SimpleCursorAdapter scAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.negative_balance);

        btnClearNegVal = (Button) findViewById(R.id.btnClearNegBal);
        btnClearNegVal.setOnClickListener(oclBtnClear);

        lvNegBal = (ListView) findViewById(R.id.lvNegBal);

        budgetDbNegBal = new BudgetDbBalance(this);
        db = budgetDbNegBal.getWritableDatabase();

        fillListFromDbNegativ();
    }

    View.OnClickListener oclBtnClear = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            db.delete(NegativeEntry.TABLE_NAME, null, null);
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + NegativeEntry.TABLE_NAME + "'");

            //reload(clear) ListView negative balance
            fillListFromDbNegativ();
        }
    };

    private void fillListFromDbNegativ() {
        Cursor cursor = db.query(NegativeEntry.TABLE_NAME, null, null, null, null, null, null, null);

        String[] from = new String[]{NegativeEntry.COLUMN_DATE, NegativeEntry.COLUMN_VALUE};
        int[] to = new int[]{R.id.tvDateNegBal, R.id.tvValueNegBal};

        scAdapter = new SimpleCursorAdapter(this, R.layout.list_item_neg_bal, cursor, from, to, 0);

        lvNegBal.setAdapter(scAdapter);

//        cursor.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
