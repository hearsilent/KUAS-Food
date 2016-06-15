package hearsilent.kuas.food;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import hearsilent.kuas.food.libs.Constant;
import hearsilent.kuas.food.libs.PermissionUtils;
import hearsilent.kuas.food.libs.Utils;
import hearsilent.kuas.food.models.ShopModel;

public class ShopDetailActivity extends AppCompatActivity implements LocationListener {

	private Toolbar mToolbar;
	private CollapsingToolbarLayout mCollapsingToolbar;
	private KenBurnsView mHeaderImageView;

	private TextView mDescTextView, mAddressTextView, mPhoneTextView, mRegionTextView,
			mTimeTextView;

	private FloatingActionButton mFAB;

	private ShopModel mShopModel;

	private LocationManager mLocationManager;
	private String bestProvider = LocationManager.GPS_PROVIDER;

	private double mLatitude, mLongitude;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_in_right, R.anim.keep);
		setContentView(R.layout.activity_shop_detail);

		getBundle();
		findViews();
		setUpViews();
	}

	private void checkGPS() {
		if (!PermissionUtils.isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
			return;
		}
		LocationManager status =
				(LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
		if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
				status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			setUpLocationService();
		} else {
			Toast.makeText(this, R.string.gps_not_open, Toast.LENGTH_LONG).show();
			startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		}
	}

	@SuppressWarnings("all")
	private void setUpLocationService() {
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		bestProvider = mLocationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null ?
				LocationManager.NETWORK_PROVIDER : mLocationManager.getBestProvider(criteria, true);
		Location location = mLocationManager.getLastKnownLocation(bestProvider);
		getLocation(location);
	}

	@SuppressWarnings("all")
	private void getLocation(Location location) {
		if (location != null) {
			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();
		} else {
			Toast.makeText(this, R.string.gps_not_work, Toast.LENGTH_SHORT).show();
		}
	}

	private void getBundle() {
		mShopModel = new Gson().fromJson(getIntent().getStringExtra("shop"), ShopModel.class);
	}

	private void findViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
		mHeaderImageView = (KenBurnsView) findViewById(R.id.imageView_header);

		mDescTextView = (TextView) findViewById(R.id.textView_desc);
		mAddressTextView = (TextView) findViewById(R.id.textView_address);
		mPhoneTextView = (TextView) findViewById(R.id.textView_phone);
		mRegionTextView = (TextView) findViewById(R.id.textView_region);
		mTimeTextView = (TextView) findViewById(R.id.textView_time);

		mFAB = (FloatingActionButton) findViewById(R.id.fab);
	}

	@SuppressWarnings("all")
	@Override
	protected void onResume() {
		super.onResume();
		if (mHeaderImageView != null) {
			mHeaderImageView.resume();
		}
		if (mLocationManager != null) {
			mLocationManager.requestLocationUpdates(bestProvider, 500, 1, this);
		} else {
			checkGPS();
		}
	}

	@SuppressWarnings("all")
	@Override
	protected void onPause() {
		super.onPause();
		if (mHeaderImageView != null) {
			mHeaderImageView.pause();
		}
		if (mLocationManager != null) {
			mLocationManager.removeUpdates(this);
		}
	}

	private void setUpViews() {
		setUpToolbar();

		mDescTextView.setText(mShopModel.getDescription());
		mAddressTextView.setText(mShopModel.getAddress());
		mPhoneTextView.setText(mShopModel.getPhone());
		mRegionTextView.setText(mShopModel.getRegion());
		mTimeTextView.setText(mShopModel.getTime());

		ImageLoader.getInstance().displayImage(mShopModel.getImage(), mHeaderImageView,
				Utils.getDisplayImageBuilder().build());

		if (mShopModel.getRegion().equals(Constant.JIANGONG)) {
			mLatitude = Constant.JIANGONG_LAT;
			mLongitude = Constant.JIANGONG_LNG;
		} else {
			mLatitude = Constant.YANCHAO_LAT;
			mLongitude = Constant.YANCHAO_LNG;
		}

		checkGPS();

		mFAB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String saddr = "saddr=" + mLatitude + "," + mLongitude;
				String daddr = "daddr=" + mShopModel.getLat() + "," + mShopModel.getLng();
				String uriString = "http://maps.google.com/maps?" + saddr + "&" + daddr;

				Uri uri = Uri.parse(uriString);

				try {
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
					intent.setClassName("com.google.android.apps.maps",
							"com.google.android.maps.MapsActivity");
					startActivity(intent);
				} catch (ActivityNotFoundException e) {
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
					startActivity(intent);
				}
			}
		});
	}

	private void setUpToolbar() {
		mToolbar.setTitle(mShopModel.getName());
		setSupportActionBar(mToolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}

		mCollapsingToolbar.getLayoutParams().height =
				Utils.getDisplayDimen(this).x * 9 / 16 + Utils.getStatusBarHeightPixel(this);
		mCollapsingToolbar.requestLayout();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.keep, R.anim.slide_out_right);
	}

	@Override
	public void onLocationChanged(Location location) {
		getLocation(location);
	}

	@Override
	public void onProviderDisabled(String arg0) {
	}

	@Override
	public void onProviderEnabled(String arg0) {
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	}

}
