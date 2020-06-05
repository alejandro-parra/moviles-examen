package mx.itesm.hospitalcivil;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MapsFragment extends Fragment implements OnMapReadyCallback,TaskLoadedCallback {
    View vista;
    GoogleMap mMap;
    Button getRouteButton;
    MarkerOptions currentLoc, destination;
    Polyline currentPolyline;
    TextView kmTextView, timeTextView;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;

    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_maps, container, false);
        getRouteButton = vista.findViewById(R.id.getRouteButton);
        kmTextView = vista.findViewById(R.id.kmTextView);
        timeTextView = vista.findViewById(R.id.timeTextView);
        kmTextView.setText("-");
        timeTextView.setText("-");

        //20.686028,-103.344156
        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        destination = new MarkerOptions().position(new LatLng(20.686028, -103.344156)).title("Destination");
        //20.585164,-103.449939
        //implementar metodo para obtener la current location
        getCurrentLocation();
        getRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sacar la current location
                getCurrentLocation();
                //se limpia el mapa para poner los nuevos marcadores
                mMap.clear();
                Log.d("mylog", "updated Markers");
                MarkerOptions options = new MarkerOptions();
                options.position(currentLoc.getPosition());
                mMap.addMarker(currentLoc);
                mMap.addMarker(destination);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc.getPosition(),11));

                //se llama al API de distance matrix y se calculan los km y min de la ruta
                try {
                    String json = downloadUrl("https://maps.googleapis.com/maps/api/distancematrix/json?origins="+currentLoc.getPosition().latitude+","+currentLoc.getPosition().longitude+"&destinations="+destination.getPosition().latitude+","+destination.getPosition().longitude+"&key=AIzaSyC5K3V3fWr_j8Dix_V1g4ndao5jQvMbyzk");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //en este caso no se actualizara nada porque el API de distance matrix es de paga, pero el siguiente paso es leer el JSON y poner los valores en los Textviews.

                //inicializar el dibujo de la ruta con polylines
                new FetchURL(getContext()).execute(getUrl(currentLoc.getPosition(), destination.getPosition(), "driving"), "driving");
            }
        });

        return vista;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        supportMapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        Log.d("mylog", "Added Markers");
        MarkerOptions options = new MarkerOptions();
        options.position(currentLoc.getPosition());
        options.title("Place 1");

        mMap.addMarker(currentLoc);
        mMap.addMarker(destination);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc.getPosition(),12));
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }



    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    public void getCurrentLocation(){
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        getLocation();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                currentLoc = new MarkerOptions().position(new LatLng(lat, longi)).title("Current");
            } else {
                Toast.makeText(getActivity(), "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            Log.d("mylog", "Downloaded URL: " + data.toString());
            br.close();
        } catch (Exception e) {
            Log.d("mylog", "Exception downloading URL: " + e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
