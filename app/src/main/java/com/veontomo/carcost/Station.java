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

        private static final String TABLE_NAME = "Stations";

        private static final String ID_COL_NAME = "id";
        private static final String DISTRIBUTOR_COL_NAME = "distributor";
        private static final String NAME_COL_NAME = "name";
        private static final String COUNTRY_COL_NAME = "country";
        private static final String CITY_COL_NAME = "city";
        private static final String STREET_COL_NAME = "street";
        private static final String BUILDING_COL_NAME = "building";
        private static final String LATITUDE_COL_NAME = "latitude";
        private static final String LONGITUDE_COL_NAME = "longitude";
        private static final int DISTRIBUTOR_SIZE = 20;
        private static final int NAME_SIZE = 50;
        private static final int STREET_SIZE = 50;
        private static final int BUILDING_SIZE = 10;
        private static final int CITY_SIZE = 30;


        public Storage(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // SQL statement to create station table
            String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "( " +
                    ID_COL_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DISTRIBUTOR_COL_NAME + " VARCHAR( " + String.valueOf(DISTRIBUTOR_SIZE) + "), " +
                    NAME_COL_NAME + " VARCHAR( " + String.valueOf(NAME_SIZE) + "), " +
                    COUNTRY_COL_NAME + " VARCHAR( " + String.valueOf(NAME_SIZE) + "), " +
                    STREET_COL_NAME + " VARCHAR( " + String.valueOf(STREET_SIZE) + "), " +
                    BUILDING_COL_NAME + " VARCHAR( " + String.valueOf(BUILDING_SIZE) + "), " +
                    CITY_COL_NAME + " VARCHAR( " + String.valueOf(CITY_SIZE) + "), " +
                    LONGITUDE_COL_NAME + " FLOAT, " +
                    LATITUDE_COL_NAME + " FLOAT )";

            db.execSQL(CREATE_TABLE_QUERY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older books table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            this.onCreate(db);
        }

        public void save(String name, String distributor, String country,  String city, String street, String building, Float lat, Float lon) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NAME_COL_NAME, name);
            values.put(DISTRIBUTOR_COL_NAME, distributor);
            values.put(COUNTRY_COL_NAME, country);
            values.put(STREET_COL_NAME, street);
            values.put(BUILDING_COL_NAME, building);
            values.put(CITY_COL_NAME, city);
            values.put(LONGITUDE_COL_NAME, lon);
            values.put(LATITUDE_COL_NAME, lat);
            db.insert(TABLE_NAME,
                    null,
                    values);
            db.close();
        }

    }
}
