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
    /**
     * id with which an instance of this class is saved in database
     */
    private long id;
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

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Float getPaid() {
        return paid;
    }

    public void setPaid(Float paid) {
        this.paid = paid;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
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




}
