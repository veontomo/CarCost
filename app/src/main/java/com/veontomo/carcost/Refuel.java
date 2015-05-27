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

    public void Refuel(Context context, Float distance, Double price, Double paid, Double quantity, Integer stationId) throws Exception {
        this.mContext = context;
        this.distance = distance;
        this.stationId = stationId;

        setTriple(price, paid, quantity);

        if (!isValid(this.price, this.paid, this.quantity)){
            throw new Exception("Wrong price-paid-quantity");
        }

    }

    /**
     * Sets three numbers in such a way that they are related by the
     * following relation: paid = price * amount. If some of the input is
     * missing, then it is derived from the above relation.
     * @param price
     * @param paid
     * @param quantity
     */
    private void setTriple(Double price, Double paid, Double quantity) {
        if (price == null){
            if (paid != null && quantity != null && quantity != 0){
                this.price = paid/quantity;
                this.paid = paid;
                this.quantity = quantity;
            }
        } else if (paid == null){
            if (price != null && quantity != null){
                this.paid = price*quantity;
                this.price = price;
                this.quantity = quantity;
            }
        } else if (quantity == null){
            if (price != null && price != 0 && paid != null){
                this.paid = paid;
                this.price = price;
                this.quantity = paid/price;
            }
        } else {
            this.paid = paid;
            this.price = price;
            this.quantity = quantity;
        }
    }

    /**
     * Returns true if the triple price-paid-quantity is set correctly (up to
     * given precision).
     * @param price
     * @param paid
     * @param quantity
     * @return
     */
    private boolean isValid(Double price, Double paid, Double quantity) {
        return Math.abs(paid - price*quantity) <= Math.abs(paid*PRECISION);
    }

}
