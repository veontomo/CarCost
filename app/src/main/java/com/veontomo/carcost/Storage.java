package com.veontomo.carcost;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
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

    private final Context mContext;

    /**
     * Names of columns of the refuel table
     */
    public static abstract class RefuelEntry implements BaseColumns {
        public static final String TABLE_NAME = "Refuels";
        public static final String ID_COL_NAME = "id";
        public static final String DISTANCE_COL_NAME = "distance";
        public static final String PRICE_COL_NAME = "price";
        public static final String PAID_COL_NAME = "paid";
        public static final String QUANTITY_COL_NAME = "quantity";
        public static final String STATION_ID_COL_NAME = "station_id";
    }

    /**
     * Name of table that stores information about stations
     */
    public static abstract class StationEntry implements BaseColumns {
        public static final String TABLE_NAME = "Stations";
        public static final String ID_COL_NAME = "id";
        public static final String DISTRIBUTOR_COL_NAME = "distributor";
        public static final String NAME_COL_NAME = "name";
        public static final String COUNTRY_COL_NAME = "country";
        public static final String CITY_COL_NAME = "city";
        public static final String STREET_COL_NAME = "street";
        public static final String BUILDING_COL_NAME = "building";
        public static final String LATITUDE_COL_NAME = "latitude";
        public static final String LONGITUDE_COL_NAME = "longitude";
        public static final int DISTRIBUTOR_SIZE = 20;
        public static final int NAME_SIZE = 50;
        public static final int STREET_SIZE = 50;
        public static final int BUILDING_SIZE = 10;
        public static final int CITY_SIZE = 30;
    }

    // SQL statement to create station table
    private static final String CREATE_REFUEL_TABLE_QUERY = "CREATE TABLE " + RefuelEntry.TABLE_NAME + "( " +
            RefuelEntry.ID_COL_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RefuelEntry.DISTANCE_COL_NAME + " REAL, " +
            RefuelEntry.PRICE_COL_NAME + " REAL, " +
            RefuelEntry.PAID_COL_NAME + " REAL, " +
            RefuelEntry.QUANTITY_COL_NAME + " REAL, " +
            RefuelEntry.STATION_ID_COL_NAME + " INTEGER)";

    private static final String DROP_REFUEL_TABLE_QUERY = "DROP TABLE IF EXISTS " + RefuelEntry.TABLE_NAME;

    private static final String CREATE_STATION_TABLE_QUERY = "CREATE TABLE " +
            StationEntry.TABLE_NAME + "( " +
            StationEntry.ID_COL_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            StationEntry.DISTRIBUTOR_COL_NAME + " VARCHAR( " + String.valueOf(StationEntry.DISTRIBUTOR_SIZE) + "), " +
            StationEntry.NAME_COL_NAME + " VARCHAR( " + String.valueOf(StationEntry.NAME_SIZE) + "), " +
            StationEntry.COUNTRY_COL_NAME + " VARCHAR( " + String.valueOf(StationEntry.NAME_SIZE) + "), " +
            StationEntry.STREET_COL_NAME + " VARCHAR( " + String.valueOf(StationEntry.STREET_SIZE) + "), " +
            StationEntry.BUILDING_COL_NAME + " VARCHAR( " + String.valueOf(StationEntry.BUILDING_SIZE) + "), " +
            StationEntry.CITY_COL_NAME + " VARCHAR( " + String.valueOf(StationEntry.CITY_SIZE) + "), " +
            StationEntry.LONGITUDE_COL_NAME + " FLOAT, " +
            StationEntry.LATITUDE_COL_NAME + " FLOAT )";

    private static final String DROP_STATION_TABLE_QUERY = "DROP TABLE IF EXISTS " + StationEntry.TABLE_NAME;

    public Storage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_REFUEL_TABLE_QUERY);
        db.execSQL(CREATE_STATION_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_STATION_TABLE_QUERY);
        db.execSQL(DROP_REFUEL_TABLE_QUERY);
        this.onCreate(db);
    }

    public Long save(Refuel refuel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RefuelEntry.DISTANCE_COL_NAME, refuel.getDistance());
        values.put(RefuelEntry.PRICE_COL_NAME, refuel.getPrice());
        values.put(RefuelEntry.PAID_COL_NAME, refuel.getPaid());
        values.put(RefuelEntry.QUANTITY_COL_NAME, refuel.getQuantity());
        values.put(RefuelEntry.STATION_ID_COL_NAME, refuel.getStationId());
        long id = db.insert(RefuelEntry.TABLE_NAME, null, values);
        db.close();
        return (id != -1) ? id : null;
    }


    public Long save(Station station) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(StationEntry.NAME_COL_NAME, station.getName());
        values.put(StationEntry.DISTRIBUTOR_COL_NAME, station.getDistributor());
        values.put(StationEntry.COUNTRY_COL_NAME, station.getCountry());
        values.put(StationEntry.STREET_COL_NAME, station.getStreet());
        values.put(StationEntry.BUILDING_COL_NAME, station.getBuilding());
        values.put(StationEntry.CITY_COL_NAME, station.getCity());
        values.put(StationEntry.LONGITUDE_COL_NAME, station.getLatitude());
        values.put(StationEntry.LATITUDE_COL_NAME, station.getLongitude());
        long id = db.insert(StationEntry.TABLE_NAME, null, values);
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
        String query = "SELECT  * FROM " + StationEntry.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Station station = null;
        if (cursor.moveToFirst()) {
            do {
                station = new Station(this.getContext());
                String name = cursor.getString(cursor.getColumnIndexOrThrow(StationEntry.NAME_COL_NAME));
                station.setName(name);
                String distributor = cursor.getString(cursor.getColumnIndexOrThrow(StationEntry.DISTRIBUTOR_COL_NAME));
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
        String query = "SELECT " + StationEntry.NAME_COL_NAME + " FROM " + StationEntry.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String name = null;
        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndexOrThrow(StationEntry.NAME_COL_NAME));
                names.add(name);
            } while (cursor.moveToNext());
        }
        return names;
    }

    public Station getStationById(Long id) {
        String query = "SELECT  * FROM " + StationEntry.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection = {
                StationEntry.ID_COL_NAME,
                StationEntry.DISTRIBUTOR_COL_NAME,
                StationEntry.NAME_COL_NAME,
                StationEntry.COUNTRY_COL_NAME,
                StationEntry.CITY_COL_NAME,
                StationEntry.STREET_COL_NAME,
                StationEntry.BUILDING_COL_NAME,
                StationEntry.LATITUDE_COL_NAME,
                StationEntry.LONGITUDE_COL_NAME};
        Cursor cursor = db.query(
                StationEntry.TABLE_NAME,
                projection,
                StationEntry.ID_COL_NAME,
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            Station station = new Station(this.getContext());
            String name = cursor.getString(cursor.getColumnIndexOrThrow(StationEntry.NAME_COL_NAME));
            station.setName(name);
            String distributor = cursor.getString(cursor.getColumnIndexOrThrow(StationEntry.DISTRIBUTOR_COL_NAME));
            station.setDistributor(distributor);
            // TODO: set up the rest fields (city, building, etc)
            return station;
        }
        return null;
    }
}