package hearsilent.kuas.food;

import android.Manifest;
import android.animation.Animator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.graphics.Typeface;
import android.location.Location;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import github.hellocsl.cursorwheel.CursorWheelLayout;
import hearsilent.kuas.food.adapter.SimpleTextAdapter;
import hearsilent.kuas.food.libs.Constant;
import hearsilent.kuas.food.libs.DatabaseUtils;
import hearsilent.kuas.food.libs.Memory;
import hearsilent.kuas.food.libs.PermissionUtils;
import hearsilent.kuas.food.libs.Utils;
import hearsilent.kuas.food.models.ShopModel;
import hearsilent.kuas.food.particlesys.ParticleSystemRenderer;
import hearsilent.kuas.food.widget.SimpleTextCursorWheelLayout;

public class MainActivity extends AppCompatActivity
		implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
		LocationListener {

	private Toolbar mToolbar;
	private TextView mTitleTextView, mShopTextView;

	private ImageView mSelectImageView;

	private GLSurfaceView mGlSurfaceView;

	private SimpleTextCursorWheelLayout mCursorWheelLayout;

	private List<ShopModel> mShopList = DatabaseUtils.getJianGongList();

	private boolean firstTime = true;
	private boolean select = false;

	private GoogleApiClient mGoogleApiClient;
	LocationRequest mLocationRequest;
	private Location mLastLocation;

	@Utils.Location String mLocation = Constant.JIANGONG;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViews();
		setUpViews();
	}

	private void findViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mTitleTextView = (TextView) findViewById(R.id.textView_title);

		mShopTextView = (TextView) findViewById(R.id.textView_shops);
		mSelectImageView = (ImageView) findViewById(R.id.imageView_select);

		mGlSurfaceView = (GLSurfaceView) findViewById(R.id.glSurfaceView);

		mCursorWheelLayout = (SimpleTextCursorWheelLayout) findViewById(R.id.cursorWheelLayout);
	}

	private void setUpViews() {
		checkPermission();
		setUpBackground();
		setUpTitle();
		setUpWheel();
		setUpSelectView();
		setUpHintBubble();
	}

	private void checkPermission() {
		if (PermissionUtils.isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
			setUpGoogleApiClient();
		} else {
			Toast.makeText(this, R.string.access_fine_location_denied_jiangong, Toast.LENGTH_SHORT)
					.show();
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
			Toast.makeText(this, R.string.gps_not_open_jiangong, Toast.LENGTH_SHORT).show();
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

	@SuppressWarnings("all")
	private void getLocation(Location location) {
		if (location != null) {
			mLastLocation = location;

			double lat = location.getLatitude();
			double lng = location.getLongitude();

			mLocation = Utils.checkLocation(lat, lng);

			if (mLocation.equals(Constant.JIANGONG)) {
				mShopList = DatabaseUtils.getJianGongList();
			} else {
				mShopList = DatabaseUtils.getYanChaoList();
			}
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

	private void setUpHintBubble() {
		if (Memory.getBoolean(this, Constant.PREF_HINT, false)) {
			return;
		}
		mCursorWheelLayout.post(new Runnable() {

			@Override
			public void run() {
				BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(MainActivity.this)
						.inflate(R.layout.bubble_hint, null);
				PopupWindow popupWindow = BubblePopupHelper.create(MainActivity.this, bubbleLayout);
				popupWindow.setWidth((int) Utils.convertDpToPixel(180f, MainActivity.this));
				int[] location = new int[2];
				mCursorWheelLayout.getLocationInWindow(location);
				popupWindow.showAtLocation(mCursorWheelLayout, Gravity.NO_GRAVITY, location[0] +
								(int) (mCursorWheelLayout.getWidth() -
										Utils.convertDpToPixel(176f, MainActivity.this)) / 2,
						location[1] - (int) Utils.convertDpToPixel(70f, MainActivity.this));
			}
		});
		mSelectImageView.post(new Runnable() {

			@Override
			public void run() {
				BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(MainActivity.this)
						.inflate(R.layout.bubble_hint_click, null);
				PopupWindow popupWindow = BubblePopupHelper.create(MainActivity.this, bubbleLayout);
				popupWindow.setWidth((int) Utils.convertDpToPixel(140f, MainActivity.this));
				int[] location = new int[2];
				mSelectImageView.getLocationInWindow(location);
				popupWindow.showAtLocation(mSelectImageView, Gravity.NO_GRAVITY,
						location[0] - (int) Utils.convertDpToPixel(32f, MainActivity.this),
						location[1] + mSelectImageView.getHeight() +
								(int) Utils.convertDpToPixel(5f, MainActivity.this));
			}
		});
	}

	private void setUpBackground() {
		ActivityManager activityManager =
				(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

		if (supportsEs2) {
			// Request an OpenGL ES 2.0 compatible context.
			mGlSurfaceView.setEGLContextClientVersion(2);

			// Set the renderer to our demo renderer, defined below.
			ParticleSystemRenderer mRenderer = new ParticleSystemRenderer(mGlSurfaceView);
			mGlSurfaceView.setRenderer(mRenderer);
			mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		} else {
			mGlSurfaceView.setVisibility(View.GONE);
		}
	}

	private void setUpTitle() {
		setSupportActionBar(mToolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayShowTitleEnabled(false);
		}

		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/starjhol.ttf");
		mTitleTextView.setTypeface(font);
	}

	private void setUpWheel() {
		List<String> list = new ArrayList<>(Arrays.asList("K", "U", "A", "S", "F", "O", "O", "D"));
		SimpleTextAdapter adapter = new SimpleTextAdapter(this, list);
		mCursorWheelLayout.setAdapter(adapter);
		mCursorWheelLayout.setOnWheelFlingListener(new CursorWheelLayout.OnWheelFlingListener() {

			@Override
			public void onFling(boolean autoFling) {
				if (firstTime) {
					toggleTitle();
				}
				if (select) {
					select = false;
					mShopTextView.animate().scaleX(1f).scaleY(1f);
					mSelectImageView.setEnabled(false);
				}
				mShopTextView
						.setText(mShopList.get(new Random().nextInt(mShopList.size())).getName());
			}

			@Override
			public void onEnd() {
				if (!select) {
					select = true;
					mShopTextView.animate().scaleX(1.2f).scaleY(1.2f);
					mSelectImageView.setEnabled(true);
				}
			}
		});
	}

	private void setUpSelectView() {
		mSelectImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ShopModel model = DatabaseUtils.getShop(mShopTextView.getText().toString());
				if (model != null) {
					Intent intent = new Intent(MainActivity.this, ShopDetailActivity.class);
					intent.putExtra("shop", new Gson().toJson(model));
					startActivity(intent);
				} else {
					Toast.makeText(MainActivity.this, R.string.something_error, Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		mSelectImageView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						mCursorWheelLayout.setTouchable(false);
						return false;
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_UP:
						mCursorWheelLayout.setTouchable(true);
						return false;
				}
				return false;
			}
		});
		mSelectImageView.setEnabled(false);
	}

	private void toggleTitle() {
		firstTime = false;
		Memory.setBoolean(this, Constant.PREF_HINT, true);
		mTitleTextView.animate().alpha(0f).setListener(new Animator.AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mTitleTextView.setVisibility(View.INVISIBLE);
				mShopTextView.setVisibility(View.VISIBLE);
				mShopTextView.animate().alpha(1f);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.shops) {
			startActivity(new Intent(this, ShopsActivity.class));
			return true;
		} else if (item.getItemId() == R.id.open_source) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
