package com.veontomo.carcost;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

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
    private static final String REFUELS_TABLE_NAME = "Refuels";

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

    private final Context mContext;

    public Storage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create station table
        String CREATE_REFUEL_TABLE_QUERY = "CREATE TABLE " + REFUELS_TABLE_NAME + "( " +
                REFUEL_ID_COL_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                REFUEL_DISTANCE_COL_NAME + " REAL, " +
                REFUEL_PRICE_COL_NAME + " REAL, " +
                REFUEL_PAID_COL_NAME + " REAL, " +
                REFUEL_QUANTITY_COL_NAME + " REAL, " +
                REFUEL_STATION_ID_COL_NAME + " INTEGER)";
        Log.i(TAG, "Storage: creating table Refuel " + CREATE_REFUEL_TABLE_QUERY);
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
        Log.i(TAG, "Storage: creating table Station " + CREATE_REFUEL_TABLE_QUERY);

        db.execSQL(CREATE_STATION_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS " + STATIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + REFUELS_TABLE_NAME);
        this.onCreate(db);
    }

    public Long save(Refuel refuel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REFUEL_DISTANCE_COL_NAME, refuel.getDistance());
        values.put(REFUEL_PRICE_COL_NAME, refuel.getPrice());
        values.put(REFUEL_PAID_COL_NAME, refuel.getPaid());
        values.put(REFUEL_QUANTITY_COL_NAME, refuel.getQuantity());
        values.put(REFUEL_STATION_ID_COL_NAME, refuel.getStationId());
        long id = db.insert(REFUELS_TABLE_NAME,
                null,
                values);
        db.close();
        return (id != -1) ? id : null;
    }


    public Long save(Station station) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATIONS_NAME_COL_NAME, station.getName());
        values.put(STATIONS_DISTRIBUTOR_COL_NAME, station.getDistributor());
        values.put(STATIONS_COUNTRY_COL_NAME, station.getCountry());
        values.put(STATIONS_STREET_COL_NAME, station.getStreet());
        values.put(STATIONS_BUILDING_COL_NAME, station.getBuilding());
        values.put(STATIONS_CITY_COL_NAME, station.getCity());
        values.put(STATIONS_LONGITUDE_COL_NAME, station.getLatitude());
        values.put(STATIONS_LATITUDE_COL_NAME, station.getLongitude());
        long id = db.insert(STATIONS_TABLE_NAME,
                null,
                values);
        db.close();
        return (id != -1) ? id : null;
    }

    /**
     * Returns names of stations present in database
     *
     * @return list of station names
     */
    public ArrayList<Station> loadStations() {
        ArrayList<Station> stations = new ArrayList<Station>();
        String query = "SELECT  * FROM " + STATIONS_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Station station = null;
        if (cursor.moveToFirst()) {
            do {
                station = new Station(this.getContext());
                String name = cursor.getString(cursor.getColumnIndexOrThrow(STATIONS_NAME_COL_NAME));
                station.setName(name);
                String distributor = cursor.getString(cursor.getColumnIndexOrThrow(STATIONS_DISTRIBUTOR_COL_NAME));
                station.setDistributor(distributor);
                // TODO: set up the rest fields (city, building, etc)
                stations.add(station);
            } while (cursor.moveToNext());
        }
        return stations;
    }

    /**
     * Retrieves a list of station names present in database.
     *
     * @return list of strings
     * @since 0.1
     */
    public ArrayList<String> loadStationNames() {
        ArrayList<String> names = new ArrayList<String>();
        String query = "SELECT " + STATIONS_NAME_COL_NAME + " FROM " + STATIONS_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String name = null;
        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndexOrThrow(STATIONS_NAME_COL_NAME));
                names.add(name);
            } while (cursor.moveToNext());
        }
        return names;
    }

    public Station getStationById(Long id) {
        String query = "SELECT  * FROM " + STATIONS_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection = {
                STATIONS_ID_COL_NAME,
                STATIONS_DISTRIBUTOR_COL_NAME,
                STATIONS_NAME_COL_NAME,
                STATIONS_COUNTRY_COL_NAME,
                STATIONS_CITY_COL_NAME,
                STATIONS_STREET_COL_NAME,
                STATIONS_BUILDING_COL_NAME,
                STATIONS_LATITUDE_COL_NAME,
                STATIONS_LONGITUDE_COL_NAME};
        Cursor cursor = db.query(
                STATIONS_TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                STATIONS_ID_COL_NAME,                                // The columns for the WHERE clause
                new String[]{String.valueOf(id)},                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        if (cursor.moveToFirst()) {
                Station station = new Station(this.getContext());
                String name = cursor.getString(cursor.getColumnIndexOrThrow(STATIONS_NAME_COL_NAME));
                station.setName(name);
                String distributor = cursor.getString(cursor.getColumnIndexOrThrow(STATIONS_DISTRIBUTOR_COL_NAME));
                station.setDistributor(distributor);
                // TODO: set up the rest fields (city, building, etc)
                return station;
        }
        return null;
    }
}