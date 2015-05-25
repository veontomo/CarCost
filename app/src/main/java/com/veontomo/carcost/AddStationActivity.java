package com.veontomo.carcost;

import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class AddStationActivity extends Activity implements LocationListener{

    private static final String TAG = "CarCost";
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
            TextView addr = (TextView) findViewById(R.id.lay_add_station_alt);
            Toast.makeText(getApplicationContext(), "address size " + String.valueOf(address.size()), Toast.LENGTH_LONG).show();
            if (address.size() > 0){
                addr.setText(address.get(0).toString());
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
