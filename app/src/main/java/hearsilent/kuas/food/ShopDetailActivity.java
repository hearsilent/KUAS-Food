package hearsilent.kuas.food;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import hearsilent.kuas.food.libs.Constant;
import hearsilent.kuas.food.libs.PermissionUtils;
import hearsilent.kuas.food.libs.Utils;
import hearsilent.kuas.food.models.ShopModel;

public class ShopDetailActivity extends AppCompatActivity
		implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
		LocationListener {

	private Toolbar mToolbar;
	private CollapsingToolbarLayout mCollapsingToolbar;
	private KenBurnsView mHeaderImageView;

	private TextView mDescTextView, mAddressTextView, mPhoneTextView, mRegionTextView,
			mTimeTextView, mDisTextView;

	private FloatingActionButton mFAB;

	private ShopModel mShopModel;

	private GoogleApiClient mGoogleApiClient;
	LocationRequest mLocationRequest;

	private double mLatitude, mLongitude;
	private Location mLastLocation;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_in_right, R.anim.keep);
		setContentView(R.layout.activity_shop_detail);

		getBundle();
		findViews();
		setUpViews();
		checkPermission();
	}

	private void checkPermission() {
		if (mShopModel.getRegion().equals(Constant.JIANGONG)) {
			mLatitude = Constant.JIANGONG_LAT;
			mLongitude = Constant.JIANGONG_LNG;
		} else {
			mLatitude = Constant.YANCHAO_LAT;
			mLongitude = Constant.YANCHAO_LNG;
		}
		if (PermissionUtils.isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
			setUpGoogleApiClient();
		} else {
			if (mShopModel.getRegion().equals(Constant.JIANGONG)) {
				Toast.makeText(this, R.string.access_fine_location_denied_jiangong,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, R.string.access_fine_location_denied_yanchao,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	synchronized void setUpGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

	}

	@SuppressWarnings("all")
	@Override
	public void onConnected(@Nullable Bundle bundle) {
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(500);

		if (!Utils.checkGPSisOpen(this)) {
			if (mShopModel.getRegion().equals(Constant.JIANGONG)) {
				Toast.makeText(this, R.string.gps_not_open_jiangong, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, R.string.gps_not_open_yanchao, Toast.LENGTH_SHORT).show();
			}
		} else {
			LocationServices.FusedLocationApi
					.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

			getLocation(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
		}
	}

	@Override
	public void onLocationChanged(@NonNull Location location) {
		if (mLastLocation == null || location.getAccuracy() > mLastLocation.getAccuracy()) {
			getLocation(location);
		}
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		checkPermission();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mGoogleApiClient != null) {
			mGoogleApiClient.connect();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mGoogleApiClient != null) {
			mGoogleApiClient.disconnect();
		}
	}

	@SuppressWarnings("all")
	private void getLocation(Location location) {
		if (location != null) {
			mLastLocation = location;

			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();
			mDisTextView.setText(getString(R.string.shop_dis,
					Utils.gps2m(mLatitude, mLongitude, mShopModel.getLat(), mShopModel.getLng()) /
							1000));
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

		mDisTextView = (TextView) findViewById(R.id.textView_dis);

		mFAB = (FloatingActionButton) findViewById(R.id.fab);
	}

	@SuppressWarnings("all")
	@Override
	protected void onResume() {
		super.onResume();
		if (mHeaderImageView != null) {
			mHeaderImageView.resume();
		}
	}

	@SuppressWarnings("all")
	@Override
	protected void onPause() {
		super.onPause();
		if (mHeaderImageView != null) {
			mHeaderImageView.pause();
		}
		checkPermission();
	}

	private void setUpViews() {
		setUpToolbar();

		mDescTextView.setText(mShopModel.getDescription());
		mAddressTextView.setText(mShopModel.getAddress());
		mPhoneTextView.setText(mShopModel.getPhone());
		mRegionTextView.setText(getString(R.string.shop_region, mShopModel.getRegion()));
		mTimeTextView.setText(mShopModel.getTime());

		ImageLoader.getInstance().displayImage(mShopModel.getImage(), mHeaderImageView,
				Utils.getDisplayImageBuilder().build());

		mDisTextView.setText(getString(R.string.shop_dis,
				Utils.gps2m(mLatitude, mLongitude, mShopModel.getLat(), mShopModel.getLng()) /
						1000));

		mFAB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.startNavigationActivity(ShopDetailActivity.this, mLatitude, mLongitude,
						mShopModel.getLat(), mShopModel.getLng());
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

}
