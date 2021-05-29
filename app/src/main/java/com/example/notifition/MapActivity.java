package com.example.notifition;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.notifition.Adapter.VehicleAdapter;
import com.example.notifition.model.Distances;
import com.example.notifition.model.Durations;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

	private GoogleMap mMap;
	private List<Marker> originMarkers = new ArrayList<>();
	private List<Marker> destinationMarkers = new ArrayList<>();
	private List<Polyline> polylinePaths = new ArrayList<>();
	String origin, endAddress, startAddress;
	LocationListener locationListener;
	double lat, lng;
	Durations duration;
	Distances distance;
	LocationManager locationManager;
	int diadiemId;
	DataBase dataBase;
	Spinner vehicle;
	SharedPreferences sharedPreferences;
	List<LatLng> points = new ArrayList<>();
	LatLng startLocation, endLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
		dataBase = new DataBase(this, "database.sqlite", null, 1);
		mapFragment.getMapAsync(this);
		locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(@NonNull Location location) {
				lat = (double) location.getLatitude();
				lng = (double) location.getLongitude();
				origin = lat+"%2C"+ lng;
			}
		};
		if (ActivityCompat.checkSelfPermission
				(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
		vehicle = findViewById(R.id.spinnerVehicle);
		ArrayList<Integer> arrVehicle = new ArrayList<>();
		arrVehicle.add(R.drawable.ic_walking);
		arrVehicle.add(R.drawable.ic_bike);
		arrVehicle.add(R.drawable.ic_moto);
		arrVehicle.add(R.drawable.ic_car);

		VehicleAdapter vehicleAdapter = new VehicleAdapter(this, R.layout.spinner_row, arrVehicle);
		vehicle.setAdapter(vehicleAdapter);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		LatLng V_B = new LatLng(15.973224, 108.249595);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(V_B, 15));

		if (ActivityCompat.checkSelfPermission
				(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		mMap.setMyLocationEnabled(true);
		Intent intent = getIntent();
		diadiemId = (int) intent.getIntExtra("data", 0);
		vehicle.setSelection(sharedPreferences.getInt("phuongtien", 0));
		if (sharedPreferences.getInt("phuongtien", 0)==0){
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (origin!=null){
						getRoute(diadiemId, 0);
					}
				}
			}, 2000);
			Handler handler1 = new Handler();
			handler1.postDelayed(new Runnable() {
				@Override
				public void run() {
					onDirectionFinderSuccess(points, googleMap, 0);
				}
			}, 6000);
		}
		vehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						if (origin!=null){
							getRoute(diadiemId, position);
						}
					}
				}, 2000);
				Handler handler1 = new Handler();
				handler1.postDelayed(new Runnable() {
					@Override
					public void run() {
						onDirectionFinderSuccess(points, googleMap, position);
					}
				}, 6000);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}


	void getRoute(int id, int intMode){
		Cursor cursor = dataBase.GetData("SELECT * FROM diadiem WHERE id = " + id);
		String lat = null, lng = null;
		while (cursor.moveToNext()){
			lat = cursor.getString(3);
			lng = cursor.getString(4);
		}

		OkHttpClient httpClient= new OkHttpClient();
		String mode = "walking";

		switch (intMode){
			case 0:
				mode = "walking";
				break;
			case 1:
				mode = "bicycling";
				break;
			case 2:
				mode = "driving";
				break;
			case 3:
				mode = "driving";
				break;
		}
		Log.e("MODE", mode);
		String destination = lat+"%2C"+lng;
		endLocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

		String url = "https://rsapi.goong.io/Direction?origin="+ origin+
				"&destination="+destination+"&vehicle=hd&api_key=O2RZBszkiGblwi02rA2A7ftHuXts5FCz3WCKM4AT";

//				"https://maps.googleapis.com/maps/api/directions/json?origin="+origin+"&destination="
//				+destination+"+&mode="+mode+"&language=vi&key="+ getString(R.string.API);
		Log.e("SSS", url);
		Request request = new Request.Builder().url(url).build();
		httpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String data = response.body().string();
				try {
					JSONObject jsonData = new JSONObject(data);
					JSONArray jsonRoutes = jsonData.getJSONArray("routes");
					JSONObject jsonRoute = jsonRoutes.getJSONObject(0);
					JSONObject jsonLeg = jsonRoute.getJSONArray("legs").getJSONObject(0);
					JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
					JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
					duration
							= new Durations(jsonDuration.getString("text"), jsonDuration.getInt("value"));
					distance
							= new Distances(jsonDistance.getString("text"), jsonDistance.getInt("value"));
//
//                    JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
//                    JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");
//                    startLocation = new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"));
//                    endLocation = new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"));
					JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
					Log.e("SSS", overview_polylineJson.toString());
					points = decodePolyLine(overview_polylineJson.getString("points"));
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
	}

	public void onDirectionFinderSuccess(List<LatLng> points, GoogleMap googleMap, int intMode) {
		googleMap.clear();
		polylinePaths = new ArrayList<>();
		originMarkers = new ArrayList<>();
		destinationMarkers = new ArrayList<>();
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16));

		int v =30;
		switch (intMode){
			case 0:
				v = 3;
				break;
			case 1:
				v = 5;
				break;
			case 2:
				v = 12;
				break;
			case 3:
				v = 14;
				break;
		}
		int time = distance.value/(v*60);
		((TextView) findViewById(R.id.tvDuration)).setText(time + " phút");
		((TextView) findViewById(R.id.tvDistance)).setText(distance.text);
		destinationMarkers.add(mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
				.title("Phòng học")
				.position(endLocation)));

		PolylineOptions polylineOptions = new PolylineOptions().
				geodesic(true).
				color(Color.BLUE).
				clickable(true).
				width(10).clickable(true);
		for (int i = 0; i < points.size(); i++){
			polylineOptions.add(points.get(i));
//                Log.e("BBBB", String.valueOf(points.get(i)));
		}
		Log.e("AAA", "ve2");
		Polyline polyline = googleMap.addPolyline(polylineOptions);

//            mMap.addPolyline(polylineOptions);
		Log.e("AAA", "ve3");
//            polylinePaths.add(mMap.addPolyline(polylineOptions));

	}
	private List<LatLng> decodePolyLine(final String poly) {
		int len = poly.length();
		int index = 0;
		List<LatLng> decoded = new ArrayList<LatLng>();
		int lat = 0;
		int lng = 0;

		while (index < len) {
			int b;
			int shift = 0;
			int result = 0;
			do {
				b = poly.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = poly.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			decoded.add(new LatLng(
					lat / 100000d, lng / 100000d
			));
		}

		return decoded;
	}
}