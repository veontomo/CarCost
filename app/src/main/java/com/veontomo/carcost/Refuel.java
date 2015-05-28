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



}
