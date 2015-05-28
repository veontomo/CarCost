package com.veontomo.carcost;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_refuel);

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
                        stationText.getId());

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

        Storage storage = new Storage(getApplicationContext());

        ArrayList<String> stations = storage.getStationNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stations);

        Spinner spinner = (Spinner)findViewById(R.id.lay_add_refuel_station_spinner);
        spinner.setAdapter(adapter);

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
                Toast.makeText(getApplicationContext(), "station is added", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "station is NOT received", Toast.LENGTH_LONG).show();
            }

        }
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
