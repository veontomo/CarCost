package com.veontomo.carcost;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mario Rossi on 21/05/2015 at 20:22.
 *
 * @author veontomo@gmail.com
 * @since 0.1
 */
public class Station {
    /**
     * id with which an instance of this class is saved in database
     */
    private long id;

    private Context mContext;

    private String distributor;
    private String name;
    private String country;
    private String city;
    private String street;
    private String building;
    private Float latitude;
    private Float longitude;


    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Station(Context mContext) {
        this.mContext = mContext;
    }

    public Station(Context context, String name, String distributor, String country, String city, String street, String building) {
        this.mContext = context;
        this.name = name;
        this.distributor = distributor;
        this.country = country;
        this.street = street;
        this.building = building;
        this.city = city;
    }

    @Override
    public String toString(){
        return this.name;

    }
}
