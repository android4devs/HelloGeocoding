package pl.froger.hello.geocoding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText etAddress;
	private EditText etLatitude;
	private EditText etLongitude;
	private Button btnAddressToGeo;
	private Button btnGeoToAddress;
	private Button btnFromYourPos;
	private TextView tvInformations;

	private Geocoder geocoder;
	private LocationManager locationManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		etAddress = (EditText) findViewById(R.id.etAddress);
		etLatitude = (EditText) findViewById(R.id.etLatitude);
		etLongitude = (EditText) findViewById(R.id.etLongitude);
		btnAddressToGeo = (Button) findViewById(R.id.btnAddressToGeo);
		btnGeoToAddress = (Button) findViewById(R.id.btnGeoToAddress);
		btnFromYourPos = (Button) findViewById(R.id.btnFromYourPos);
		tvInformations = (TextView) findViewById(R.id.tvInformations);
		fillSampleData();
		initButtonsOnClick();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		geocoder = new Geocoder(this, Locale.getDefault());
	}

	private void fillSampleData() {
		etLatitude.setText("50.0544530059");
		etLongitude.setText("19.93668526411");
		etAddress.setText("Wawel");
	}
	
	private void initButtonsOnClick() {
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btnGeoToAddress:
					geolocationToAddress();
					break;
				case R.id.btnFromYourPos:
					currentLocationToAddress();
					break;
				case R.id.btnAddressToGeo:
					addressToGeolocation();
					break;
				default:
					break;
				}
			}
		};
		btnGeoToAddress.setOnClickListener(listener);
		btnFromYourPos.setOnClickListener(listener);
		btnAddressToGeo.setOnClickListener(listener);
	}

	private void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private void geolocationToAddress() {
		double latitude = new Double(etLatitude.getText().toString());
		double longitude = new Double(etLongitude.getText().toString());
		String result = getAddressFrom(latitude, longitude);
		tvInformations.setText(result);
	}
	
	private String getAddressFrom(double latitude, double longitude) {
		String result = "Geolocation address:\n";
		try {
			List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);			
			for (Address address : addresses) {
				for (int i = 0, j = address.getMaxAddressLineIndex(); i <= j; i++) {
					result += address.getAddressLine(i) + "\n";
				}
				result += "\n\n";
			}
		} catch (IOException e) {
			showToast(e.toString());
		}
		return result;
	}
	
	private void currentLocationToAddress() {
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		String result = getAddressFrom(location);
		tvInformations.setText(result);
	}
	
	private String getAddressFrom(Location location) {
		String result = "Your address:\n";
		try {
			List<Address> addresses = geocoder.getFromLocation(
					location.getLatitude(),	location.getLongitude(), 3);
			for (Address address : addresses) {
				for (int i = 0, j = address.getMaxAddressLineIndex(); i <= j; i++) {
					result += address.getAddressLine(i) + "\n";
				}
				result += "\n\n";
			}
		} catch (Exception e) {
			showToast(e.toString());
		}
		return result;
	}
	
	private void addressToGeolocation() {
		String location = etAddress.getText().toString();
		String result = getAddressFrom(location);
		tvInformations.setText(result);
	}
	
	private String getAddressFrom(String locationStr) {
		String result = "Near locations:\n";
		try {
			Log.d("ABCDDDDD", "a");
			List<Address> addresses = geocoder.getFromLocationName(locationStr, 5);
			Log.d("ABCDDDDD", "b");
			for (Address address : addresses) {
				for (int i = 0, j = address.getMaxAddressLineIndex(); i <= j; i++) {
					result += address.getAddressLine(i) + "\n";
				}
				result += "Lat: " + address.getLatitude() + "\n"
						+ "Lon: " + address.getLongitude()
						+ "\n\n";
			}
		} catch (IOException e) {
			Log.d("ABCDDDDD", e.toString());
			showToast(e.toString());
		}
		return result;
	}
}