package com.example.swat.mybudget.data;

/**
 * Created by swat on 27.06.2015.
 */
public class BudgetContract {

    //add name column for positive table
    public static final class PositiveEntry {

        public static final String TABLE_NAME = "positive";

        public static final String COLUMN_COUNTER = "_id";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_VALUE = "value";
    }

    //add name column for negative table
    public static final class NegativeEntry {

        public static final String TABLE_NAME = "negative";

        public static final String COLUMN_COUNTER = "_id";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_VALUE = "value";
    }

    //add name column for shopping table
    public static final class ShoppingEntry {

        public static final String TABLE_NAME = "shopping";

        public static final String COLUMN_COUNTER = "_id";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_TIME = "time";

        public static final String COLUMN_VALUE_NAME = "name";

        public static final String COLUMN_VALUE_COST = "cost";
    }

    //add name column for reservation table
    public static final class ReservEntry {

        public static final String TABLE_NAME = "reservation";

        public static final String COLUMN_COUNTER = "_id";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_VALUE = "value";

    }
}
