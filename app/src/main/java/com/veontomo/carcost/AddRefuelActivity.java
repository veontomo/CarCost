package com.veontomo.carcost;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class AddRefuelActivity extends Activity {
    private static final String TAG = "CarCost";
    private static final int TAKE_PHOTO_REQUEST = 1;
    private static final int ADD_STATION_REQUEST = 2;

    /**
     * Name of key that holds station id when returning result of saving a station
     * into a database.
     */
    private static final String STATION_ID_KEY_NAME = "stationId" ;

    private ArrayList<Station> stations;

    private Spinner stationSpinner;
    private ArrayAdapter<Station> stationSpinnerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_refuel);

        Storage storage = new Storage(getApplicationContext());
        this.stations = storage.loadStations();
        stationSpinnerAdapter = new ArrayAdapter<Station>(this, android.R.layout.simple_spinner_item, this.stations);
        this.stationSpinner = (Spinner) findViewById(R.id.lay_add_refuel_station_spinner);
        this.stationSpinner.setAdapter(stationSpinnerAdapter);

        Button save = (Button) findViewById(R.id.lay_add_refuel_save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText distanceText = (EditText) findViewById(R.id.lay_add_refuel_distance_input);
                EditText priceText = (EditText) findViewById(R.id.lay_add_refuel_price_input);
                EditText paidText = (EditText) findViewById(R.id.lay_add_refuel_paid_input);
                EditText quantityText = (EditText) findViewById(R.id.lay_add_refuel_quantity_input);
                Spinner stationText = (Spinner) findViewById(R.id.lay_add_refuel_station_spinner);
                Refuel refuel = new Refuel(getApplicationContext(),
                        Float.parseFloat(distanceText.getEditableText().toString()),
                        Float.parseFloat(priceText.getEditableText().toString()),
                        Float.parseFloat(paidText.getEditableText().toString()),
                        Float.parseFloat(quantityText.getEditableText().toString()),
                        stationText.getSelectedItemId());

                Storage storage = new Storage(getApplicationContext());
                storage.save(refuel);
                long id =  storage.save(refuel);
                Toast.makeText(getApplicationContext(), "Refuel is saved with id " + String.valueOf(id), Toast.LENGTH_LONG).show();
                finish();

            }
        });

        Button addPhoto = (Button) findViewById(R.id.lay_add_receipt_photo);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    Log.i(TAG, "is resolved");
                    startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST);
                } else {
                    Log.i(TAG, "is NOT resolved");
                }

            }
        });


        Button addStation = (Button) findViewById(R.id.lay_add_station_btn);
        addStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddStationActivity.class);
                startActivityForResult(intent, ADD_STATION_REQUEST);
            }
        });


        stationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Toast.makeText(getApplicationContext(), "item selected: pos = " + String.valueOf(pos) + ", id = " + String.valueOf(id), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == TAKE_PHOTO_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "photo received and saved to " + data.getExtras(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "photo is NOT received", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == ADD_STATION_REQUEST) {
            if (resultCode == RESULT_OK) {
                Long id = data.getLongExtra(STATION_ID_KEY_NAME, -1);
                if (id != -1){
                    addStationToSpinner(id);
                }
            } else {
                Toast.makeText(getApplicationContext(), "station is NOT received", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void addStationToSpinner(Long id){
        Storage storage = new Storage(getApplicationContext());
        Station station = storage.getStationById(id);
        this.stations.add(station);
        this.stationSpinnerAdapter.notifyDataSetChanged();
        this.stationSpinner.setSelection(this.stations.size() - 1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_refuel, menu);
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
