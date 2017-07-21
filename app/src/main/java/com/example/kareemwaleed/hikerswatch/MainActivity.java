package com.example.kareemwaleed.hikerswatch;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private String provider;
    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private TextView accuracyTextView;
    private TextView speedTextView;
    private TextView bearingTextView;
    private TextView altitudeTextView;
    private TextView addressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepare();
        Location tempLocation = locationManager.getLastKnownLocation(provider);
        onLocationChanged(tempLocation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            getInfo(location);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void prepare()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        System.out.println(provider);
        latitudeTextView = (TextView) findViewById(R.id.latitudeTextView);
        longitudeTextView = (TextView) findViewById(R.id.longitudeTextView);
        accuracyTextView = (TextView) findViewById(R.id.accuracyTextView);
        speedTextView = (TextView) findViewById(R.id.speedTextView);
        bearingTextView = (TextView) findViewById(R.id.bearingTextView);
        altitudeTextView = (TextView) findViewById(R.id.altitudeTextView);
        addressTextView = (TextView) findViewById(R.id.addressTextView);
    }

    private void getInfo(Location location) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addressList =  geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        if(addressList != null && addressList.size() > 0)
        {
            Double tempDouble = addressList.get(0).getLatitude();
            latitudeTextView.setText("Latitute: " + tempDouble.toString());
            tempDouble = addressList.get(0).getLongitude();
            longitudeTextView.setText("Longitude: " + tempDouble.toString());
            tempDouble = Double.valueOf(location.getAccuracy());
            accuracyTextView.setText("Accuracy: " + tempDouble.toString() + "m");
            tempDouble = Double.valueOf(location.getSpeed());
            speedTextView.setText("Speed: "+ tempDouble.toString() + "m/s");
            tempDouble = Double.valueOf(location.getBearing());
            bearingTextView.setText("Bearing: " + tempDouble.toString());
            tempDouble = location.getAltitude();
            altitudeTextView.setText("Altitute: " + tempDouble + "m");
            addressTextView.setText("Address:\n" + addressList.get(0).getAddressLine(0) + "\n" + addressList.get(0).getAddressLine(1)
                    + "\n" + addressList.get(0).getAddressLine(1));
        }
    }
}
