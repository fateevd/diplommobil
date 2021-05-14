package com.example.raiq;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link map#newInstance} factory method to
 * create an instance of this fragment.
 */
public class map extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public map() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment map.
     */
    // TODO: Rename and change types and number of parameters
    public static map newInstance(String param1, String param2) {
        map fragment = new map();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_map, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            if (getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
            }
        }
        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                try {
                    mMap = googleMap;
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mMap.setMyLocationEnabled(true);

                    LocationManager mng = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Location location = mng.getLastKnownLocation(mng.getBestProvider(new Criteria(), false));

                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 17);
                    mMap.animateCamera(cameraUpdate);


                }
                catch (Exception ex)
                {
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }

                /*mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(0, 0))
                        .title("Hello world"));*/
            }
        });
        count();
        return view;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setTranslationZ(getView(), 100f);
    }

    FirebaseDatabase database;
    DatabaseReference myRef;
    public void count(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("qrCodes");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String count=String.valueOf(dataSnapshot.getChildrenCount());
                start(count);
            }
            @Override
            public void onCancelled(DatabaseError error) { }});
    }
    public  void start(final String count){
        for(int i=0;i<=Integer.valueOf(count);i++){
            String url = "https://washeroff-76c4b.firebaseio.com/qrCodes.json";
            JsonObjectRequest jo = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        for(int i=0;i<=Integer.valueOf(count);i++){
                            JSONObject main = response.getJSONObject(response.names().get(i).toString());
                            final String address=main.getString("address");
                            String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+address+"&lang=ru&key=AIzaSyCeKL3ggSs59d9uDrgMTqN3mrtzoOjnGiQ";
                            JsonObjectRequest jo = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray results = response.getJSONArray("results");
                                        JSONObject index = results.getJSONObject(0);
                                        JSONObject geometry  = index.getJSONObject("geometry");
                                        JSONObject location = geometry.getJSONObject("location");
                                        mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(location.getDouble("lat"), location.getDouble("lng")))
                                                .title(address.toUpperCase()));
                                    } catch (JSONException e) { } }}, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) { }});
                            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                            requestQueue.add(jo);
                        }
                    } catch (JSONException e) { } }}, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) { }});
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jo);
        }
        /*String url = "https://maps.googleapis.com/maps/api/geocode/json?address=Омск Добролюбова 15&lang=ru&key=AIzaSyCeKL3ggSs59d9uDrgMTqN3mrtzoOjnGiQ";
        JsonObjectRequest jo = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    JSONObject index = results.getJSONObject(0);
                    JSONObject geometry  = index.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(location.getDouble("lat"), location.getDouble("lng")))
                            .title(""));
                } catch (JSONException e) { Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();} }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { }});
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jo);*/
    }
}