package com.veontomo.carcost;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Mario Rossi on 27/05/2015 at 21:47.
 *
 * @author veontomo@gmail.com
 * @since 0.1
 */
public class Refuel {
    private static final Double PRECISION = 0.001;
    private Float distance;
    private Float price;
    private Float paid;
    private Float quantity;
    private Integer stationId;
    private Context mContext;

    private final String TAG = "CarCost";

    public Refuel(Context context, Float distance, Float price, Float paid, Float quantity, Integer stationId){
        this.mContext = context;
        this.distance = distance;
        this.stationId = stationId;
        setTriple(price, paid, quantity);
    }

    /**
     * Sets  price, paid and quantity. If one of them is missing, calculates it from the
     * following relation: paid = price * amount.
     *
     * @param price   cost of unit of a fuel
     * @param paid    total amount paid
     * @param quantity units of fuel bought
     */
    private void setTriple(Float price, Float paid, Float quantity) {
        // raw data
        this.paid = paid;
        this.price = price;
        this.quantity = quantity;

        // deriving missing data if possible
        if (price == null && paid != null && quantity != null && quantity != 0) {
            this.price = paid / quantity;
        } else if (paid == null && price != null && quantity != null) {
            this.paid = price * quantity;
        } else if (quantity == null && price != null && price != 0 && paid != null) {
            this.quantity = paid / price;
        }
    }

    /**
     * Returns true if the triple price-paid-quantity is set correctly (up to
     * given precision).
     *
     * @return  boolean
     */
    private boolean isValid() {
        return Math.abs(this.paid - this.price * this.quantity) <= Math.abs(this.paid * PRECISION);
    }

    public long save() {
        Log.i(TAG, "Refuel class: call saving... " );
        Storage storage = new Storage(this.mContext);
        return storage.save(this.distance, this.price, this.paid, this.quantity, this.stationId);

    }

    class Storage extends SQLiteOpenHelper {
        // Database Version
        private static final int DATABASE_VERSION = 2;
        // Database Name
        private static final String DATABASE_NAME = "CarCost";

        private static final String TABLE_NAME = "Refuels";

        private static final String ID_COL_NAME = "id";
        private static final String DISTANCE_COL_NAME = "distance";
        private static final String PRICE_COL_NAME = "price";
        private static final String PAID_COL_NAME = "paid";
        private static final String QUANTITY_COL_NAME = "quantity";
        private static final String STATION_ID_COL_NAME = "station_id";

        public Storage(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // SQL statement to create station table
            String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "( " +
                    ID_COL_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DISTANCE_COL_NAME + " REAL, " +
                    PRICE_COL_NAME + " REAL, " +
                    PAID_COL_NAME + " REAL, " +
                    QUANTITY_COL_NAME + " REAL, " +
                    STATION_ID_COL_NAME + " INTEGER)";
            Log.i(TAG, "Refuel class: executing query " + CREATE_TABLE_QUERY);
            db.execSQL(CREATE_TABLE_QUERY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older books table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            this.onCreate(db);
        }

        public long save(Float distance, Float price, Float paid, Float quantity, Integer stationId) {
            Log.i(TAG, "save arguments: " + distance + ", " + price + ", " + paid + ", " + quantity + ", " + stationId);
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DISTANCE_COL_NAME, distance);
            values.put(PRICE_COL_NAME, price);
            values.put(PAID_COL_NAME, paid);
            values.put(QUANTITY_COL_NAME, quantity);
            values.put(STATION_ID_COL_NAME, stationId);
            long id = db.insert(TABLE_NAME,
                    null,
                    values);
            db.close();
            return id;
        }

    }

}
