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

    public void save() {
        Storage storage = new Storage(this.mContext);
        storage.save(this.name, this.distributor, this.country, this.city, this.street, this.building, this.latitude, this.longitude);

    }

    class Storage extends SQLiteOpenHelper {
        // Database Version
        private static final int DATABASE_VERSION = 1;
        // Database Name
        private static final String DATABASE_NAME = "CarCost";

        private static final String STATIONS_TABLE_NAME = "Stations";

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
            db.execSQL("DROP TABLE IF EXISTS " + STATIONS_TABLE_NAME);
            this.onCreate(db);
        }

        public void save(String name, String distributor, String country,  String city, String street, String building, Float lat, Float lon) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(STATIONS_NAME_COL_NAME, name);
            values.put(STATIONS_DISTRIBUTOR_COL_NAME, distributor);
            values.put(STATIONS_COUNTRY_COL_NAME, country);
            values.put(STATIONS_STREET_COL_NAME, street);
            values.put(STATIONS_BUILDING_COL_NAME, building);
            values.put(STATIONS_CITY_COL_NAME, city);
            values.put(STATIONS_LONGITUDE_COL_NAME, lon);
            values.put(STATIONS_LATITUDE_COL_NAME, lat);
            db.insert(STATIONS_TABLE_NAME,
                    null,
                    values);
            db.close();
        }

    }
}
