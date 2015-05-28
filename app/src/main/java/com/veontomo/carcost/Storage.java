package com.veontomo.carcost;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Class responsible for operations with database.
 * Created by Mario Rossi on 28/05/2015 at 15:26.
 *
 * @author veontomo@gmail.com
 * @since 0.1
 */
class Storage extends SQLiteOpenHelper {

    private final String TAG = "CarCost";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    /**
     * Name of database that contains tables of the application
     */
    private static final String DATABASE_NAME = "CarCost";
    /**
     * Name of table that stores information about refuels
     */
    private static final String REFUEL_TABLE_NAME = "Refuels";

    /**
     * Names of columns of the refuel table
     */
    private static final String REFUEL_ID_COL_NAME = "id";
    private static final String REFUEL_DISTANCE_COL_NAME = "distance";
    private static final String REFUEL_PRICE_COL_NAME = "price";
    private static final String REFUEL_PAID_COL_NAME = "paid";
    private static final String REFUEL_QUANTITY_COL_NAME = "quantity";
    private static final String REFUEL_STATION_ID_COL_NAME = "station_id";

    /**
     * Name of table that stores information about stations
     */
    private static final String STATIONS_TABLE_NAME = "Stations";

    /**
     * Names of columns of the station table
     */
    private static final String STATIONS_ID_COL_NAME = "id";
    private static final String STATIONS_DISTRIBUTOR_COL_NAME = "distributor";
    private static final String STATIONS_NAME_COL_NAME = "name";
    private static final String STATIONS_COUNTRY_COL_NAME = "country";
    private static final String STATIONS_CITY_COL_NAME = "city";
    private static final String STATIONS_STREET_COL_NAME = "street";
    private static final String STATIONS_BUILDING_COL_NAME = "building";
    private static final String STATIONS_LATITUDE_COL_NAME = "latitude";
    private static final String STATIONS_LONGITUDE_COL_NAME = "longitude";
    private static final int STATIONS_DISTRIBUTOR_SIZE = 20;
    private static final int STATIONS_NAME_SIZE = 50;
    private static final int STATIONS_STREET_SIZE = 50;
    private static final int STATIONS_BUILDING_SIZE = 10;
    private static final int STATIONS_CITY_SIZE = 30;




    public Storage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create station table
        String CREATE_REFUEL_TABLE_QUERY = "CREATE TABLE " + REFUEL_TABLE_NAME + "( " +
                REFUEL_ID_COL_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                REFUEL_DISTANCE_COL_NAME + " REAL, " +
                REFUEL_PRICE_COL_NAME + " REAL, " +
                REFUEL_PAID_COL_NAME + " REAL, " +
                REFUEL_QUANTITY_COL_NAME + " REAL, " +
                REFUEL_STATION_ID_COL_NAME + " INTEGER)";
        Log.i(TAG, "Storage: creating table Refuel query " + CREATE_REFUEL_TABLE_QUERY);
        db.execSQL(CREATE_REFUEL_TABLE_QUERY);

        String CREATE_STATION_TABLE_QUERY = "CREATE TABLE " + STATIONS_TABLE_NAME + "( " +
                STATIONS_ID_COL_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STATIONS_DISTRIBUTOR_COL_NAME + " VARCHAR( " + String.valueOf(STATIONS_DISTRIBUTOR_SIZE) + "), " +
                STATIONS_NAME_COL_NAME + " VARCHAR( " + String.valueOf(STATIONS_NAME_SIZE) + "), " +
                STATIONS_COUNTRY_COL_NAME + " VARCHAR( " + String.valueOf(STATIONS_NAME_SIZE) + "), " +
                STATIONS_STREET_COL_NAME + " VARCHAR( " + String.valueOf(STATIONS_STREET_SIZE) + "), " +
                STATIONS_BUILDING_COL_NAME + " VARCHAR( " + String.valueOf(STATIONS_BUILDING_SIZE) + "), " +
                STATIONS_CITY_COL_NAME + " VARCHAR( " + String.valueOf(STATIONS_CITY_SIZE) + "), " +
                STATIONS_LONGITUDE_COL_NAME + " FLOAT, " +
                STATIONS_LATITUDE_COL_NAME + " FLOAT )";

        db.execSQL(CREATE_STATION_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS " + REFUEL_TABLE_NAME);
        this.onCreate(db);
    }

    public long save(Float distance, Float price, Float paid, Float quantity, Integer stationId) {
        Log.i(TAG, "save arguments: " + distance + ", " + price + ", " + paid + ", " + quantity + ", " + stationId);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REFUEL_DISTANCE_COL_NAME, distance);
        values.put(REFUEL_PRICE_COL_NAME, price);
        values.put(REFUEL_PAID_COL_NAME, paid);
        values.put(REFUEL_QUANTITY_COL_NAME, quantity);
        values.put(REFUEL_STATION_ID_COL_NAME, stationId);
        long id = db.insert(REFUEL_TABLE_NAME,
                null,
                values);
        db.close();
        return id;
    }

}