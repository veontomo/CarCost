package com.veontomo.carcost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class AddStationActivity extends Activity implements LocationListener{

    private static final String TAG = "CarCost";
    private static final String STATION_ID_KEY_NAME = "stationId" ;
    protected Context context = this;
    protected LocationListener locationListener;

    private GoogleApiClient mGoogleApiClient;


    private TextView latText;
    private TextView longText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_station);

        latText = (TextView) findViewById(R.id.lay_add_station_alt);
        longText = (TextView) findViewById(R.id.lay_add_station_long);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        Button locationBtn = (Button) findViewById(R.id.lay_add_station_add_location_btn);
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        Button saveBtn = (Button) findViewById(R.id.lay_add_station_save_btn);
        Button cancelBtn = (Button) findViewById(R.id.lay_add_station_cancel_btn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameText = (EditText) findViewById(R.id.lay_add_station_name_input);
                EditText distributorText = (EditText) findViewById(R.id.lay_add_station_distributor_input);
                EditText countryText =  (EditText) findViewById(R.id.lay_add_station_country_input);
                EditText cityText =  (EditText) findViewById(R.id.lay_add_station_city_input);
                EditText streetText =  (EditText) findViewById(R.id.lay_add_station_street_input);
                EditText buildingText =  (EditText) findViewById(R.id.lay_add_station_building_input);
                Station station = new Station(getApplicationContext(),
                        nameText.getEditableText().toString(),
                        distributorText.getEditableText().toString(),
                        countryText.getEditableText().toString(),
                        cityText.getEditableText().toString(),
                        streetText.getEditableText().toString(),
                        buildingText.getEditableText().toString());

                Storage storage = new Storage(getApplicationContext());
                Long id = storage.save(station);
                if (id != null){
                    Intent result = new Intent();
                    result.putExtra(STATION_ID_KEY_NAME, id);
                    setResult(RESULT_OK, result);
                }
                Toast.makeText(getApplicationContext(), "Station is saved with id " + String.valueOf(id), Toast.LENGTH_LONG).show();
                finish();
            }
        });



        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "adding station is cancelled", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }



    @Override
    public void onLocationChanged(Location location) {
        latText.setText(String.valueOf(location.getLatitude()));
        longText.setText(String.valueOf(location.getLongitude()));
        Log.i(TAG, "lat: " + String.valueOf(location.getLatitude()) + ", long: " + String.valueOf(location.getLongitude()));
        Geocoder geo = new Geocoder(getApplicationContext(), Locale.ITALY);
        try {
            List<Address> address = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            int MAX = 10;
            int counter = 0;
            while(address.size() == 0 && counter < MAX){
                address = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                Log.i(TAG, "iteration " + String.valueOf(counter));

                counter++;

            }
            TextView addressText = (TextView) findViewById(R.id.lay_add_station_alt);
            Toast.makeText(getApplicationContext(), "address size " + String.valueOf(address.size()), Toast.LENGTH_LONG).show();
            if (address.size() > 0){
                addressText.setText(address.get(0).toString());
            } else {
                Toast.makeText(getApplicationContext(), "no address", Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "failed retrieving address", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getApplicationContext(), "Location position is disabled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getApplicationContext(), "Location position is enabled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(getApplicationContext(), "Position is changed", Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_station, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
