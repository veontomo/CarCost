package com.veontomo.carcost;

import android.content.Context;

/**
 * Created by Mario Rossi on 27/05/2015 at 21:47.
 *
 * @author veontomo@gmail.com
 * @since 0.1
 */
public class Refuel {
    private static final Double PRECISION = 0.001;
    private Float distance;
    private Double price;
    private Double paid;
    private Double quantity;
    private Integer stationId;
    private Context mContext;

    public Refuel(Context context, Float distance, Double price, Double paid, Double quantity, Integer stationId) throws Exception {
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
    private void setTriple(Double price, Double paid, Double quantity) {
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
