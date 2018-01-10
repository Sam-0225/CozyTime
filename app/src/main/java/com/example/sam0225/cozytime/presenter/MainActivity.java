package com.example.sam0225.cozytime.presenter;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.example.sam0225.cozytime.R;
import com.example.sam0225.cozytime.model.Cafe;
import com.example.sam0225.cozytime.rest.CityName;
import com.example.sam0225.cozytime.rest.CozyApiService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gordonwong.materialsheetfab.MaterialSheetFab;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

public class MainActivity  extends FragmentActivity implements OnMapReadyCallback {
//	private Button logout;
	private GoogleMap mMap;
	private MaterialSheetFab materialSheetFab;
	private static final String TAG = "MainActivity";
	private final String baseUrl = "https://cafenomad.tw/api/v1.2/";
	private final int MY_LOCATION_REQUEST_CODE = 123;
	private boolean isMapReady = false;
	private Call<List<Cafe>> cafes;
	private HashMap<String, Cafe> cafeMap = new HashMap<>();
	private CozyApiService cafeApiService;
	private Action1<Cafe> onNextCafe = new Action1<Cafe>() {
		@Override
		public void call(Cafe cafe) {
			Marker temp = mMap.addMarker(
				new MarkerOptions()
					.position(new LatLng(cafe.getLatitude(), cafe.getLongitude()))
					.title(cafe.getName()));
			cafeMap.put(temp.getId(), cafe);
		}
	};
	private Action1<Throwable> onError = new Action1<Throwable>() {
		@Override
		public void call(Throwable throwable) {
			Log.e(TAG, "Error when get cafe:", throwable);
		}
	};
	private Action0 onComplete = new Action0() {
		@Override
		public void call() {
			Log.i(TAG, "load all data ok");
		}
	};

	@BindView(R.id.fab)
	Fab fab;
	@BindView(R.id.fab_sheet)
	View sheetView;
	@BindView(R.id.overlay)
	View overlay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
			.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(baseUrl)
			.addConverterFactory(GsonConverterFactory.create())
			.build();
		cafeApiService = retrofit.create(CozyApiService.class);

		int sheetColor = getResources().getColor(R.color.background_card);
		int fabColor = getResources().getColor(R.color.background_dim_overlay);
		materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay,
			sheetColor, fabColor);

//		logout = findViewById(R.id.logout);
//		logout.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				FirebaseAuth.getInstance().signOut();
//				Intent intent = new Intent();
//				intent.setClass(MainActivity.this, LandingPage.class);
//				startActivity(intent);
//				finish();
//			}
//		});
	}
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.setInfoWindowAdapter(new CafeInfoWindowAdapter(getLayoutInflater().inflate(R.layout.adapter_infowindow, null), cafeMap));
		mMap.getUiSettings().setCompassEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		isMapReady = true;

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			enableMapFeature();
		} else {
			ActivityCompat.requestPermissions(this,
				new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
				MY_LOCATION_REQUEST_CODE);
		}
		loadData(CityName.ALL);
	}

	private void enableMapFeature() {
		mMap.getUiSettings()
			.setMyLocationButtonEnabled(true);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == MY_LOCATION_REQUEST_CODE) {
			if (permissions.length == 1 &&
				permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
				grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				enableMapFeature();
			} else {
				// Permission was denied. Display an error message.
				Log.e(TAG, "you have to grant the permissions to use this app");
			}
		}
	}

	private void loadData(String city) {
		if (isMapReady) {
			mMap.clear();
			cafeMap.clear();

			cafes = cafeApiService.listCafe(city);
			cafes.enqueue(new Callback<List<Cafe>>() {
				@Override
				public void onResponse(Call<List<Cafe>> call, final Response<List<Cafe>> response) {
					Observable<Cafe> cafeObservable = Observable.create(new Observable.OnSubscribe<Cafe>() {
						@Override
						public void call(Subscriber<? super Cafe> subscriber) {
							for (Cafe cafe : response.body()) {
								if (cafe.getLatitude() != 0.0) {
									subscriber.onNext(cafe);
								}
							}
							subscriber.onCompleted();
						}
					});
					cafeObservable.subscribe(onNextCafe, onError, onComplete);
				}

				@Override
				public void onFailure(Call<List<Cafe>> call, Throwable t) {
					Log.e(TAG, "load city fail: ", t);
				}
			});
		}
	}

	@OnClick({
		R.id.fab_sheet_item_all,
		R.id.fab_sheet_item_taipei,
		R.id.fab_sheet_item_hsinchu,
		R.id.fab_sheet_item_taichung,
		R.id.fab_sheet_item_tainan,
		R.id.fab_sheet_item_kaohsiung})
	public void onSelectCity(View view) {
		switch (view.getId()) {
			case R.id.fab_sheet_item_all:
				loadData(CityName.ALL);
				break;
			case R.id.fab_sheet_item_taipei:
				loadData(CityName.TAIPEI);
				break;
			case R.id.fab_sheet_item_hsinchu:
				loadData(CityName.HSINCHU);
				break;
			case R.id.fab_sheet_item_taichung:
				loadData(CityName.TAICHUNG);
				break;
			case R.id.fab_sheet_item_changhua:
				loadData(CityName.HSINCHU);
				break;
			case R.id.fab_sheet_item_tainan:
				loadData(CityName.TAINAN);
				break;
			case R.id.fab_sheet_item_kaohsiung:
				loadData(CityName.KAOHSIUNG);
				break;
		}
		materialSheetFab.hideSheet();
	}
}

