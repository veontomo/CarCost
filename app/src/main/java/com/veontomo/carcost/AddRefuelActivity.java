package com.veontomo.carcost;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class AddRefuelActivity extends Activity
{
    private static final String TAG = "CarCost";
    private static final int TAKE_PHOTO_REQUEST = 1;
    private static final int ADD_STATION_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_refuel);

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

        Button addStation = (Button) findViewById(R.id.lay_add_station);
        addStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddStationActivity.class);
                startActivityForResult(intent, ADD_STATION_REQUEST);
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
        if (requestCode == ADD_STATION_REQUEST){
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
