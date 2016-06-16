package hearsilent.kuas.food;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import hearsilent.kuas.food.callback.PermissionCallback;
import hearsilent.kuas.food.libs.Constant;
import hearsilent.kuas.food.libs.DatabaseUtils;
import hearsilent.kuas.food.libs.Memory;
import hearsilent.kuas.food.libs.PermissionUtils;
import hearsilent.kuas.food.libs.Utils;

public class SplashActivity extends AppCompatActivity {

	private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 200;
	private static final int PERMISSION_REQUEST_SETTINGS = 201;
	private static final int PERMISSION_REQUEST_GPS_SETTINGS = 202;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		if (!Utils.hasGooglePlayServices(this)) {
			new AlertDialog.Builder(this).setMessage(R.string.no_google_play_service)
					.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					}).setCancelable(false).show();
		}
		setUpDB();
		setUpViews();
		grantPermission();
	}

	private void setUpViews() {
		final View progressView = findViewById(R.id.view_initial_progress);
		assert progressView != null;
		progressView.post(new Runnable() {

			@Override
			public void run() {
				progressView.setVisibility(View.VISIBLE);
			}
		});
	}

	private void setUpDB() {
		if (Constant.DB_VERSION > Memory.getInt(this, Constant.PREF_DB_VERSION, 0)) {
			if (!DatabaseUtils.copyDataBase(this) &&
					Memory.getInt(this, Constant.PREF_DB_VERSION, 0) == 0) {
				Toast.makeText(this, R.string.db_init_error, Toast.LENGTH_LONG).show();
				finish();
			} else {
				Memory.setInt(this, Constant.PREF_DB_VERSION, Constant.DB_VERSION);
			}
		}
	}

	private void grantPermission() {
		PermissionUtils.checkPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION,
				PERMISSION_REQUEST_ACCESS_FINE_LOCATION, new PermissionCallback() {

					@Override
					public void onGranted() {
						checkGPS();
					}
				});
	}

	private void checkGPS() {
		if (!Utils.checkGPSisOpen(this)) {
			Toast.makeText(this, R.string.gps_not_open, Toast.LENGTH_LONG).show();
			startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
					PERMISSION_REQUEST_GPS_SETTINGS);
		} else {
			startActivity();
		}
	}

	private void startActivity() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				startActivity(new Intent(SplashActivity.this, MainActivity.class));
				finish();
			}
		}, 2500);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
	                                       @NonNull int[] grantResults) {
		if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
			PermissionUtils.onRequestPermissionsResult(permissions, grantResults, this,
					new PermissionCallback() {

						@Override
						public void onGranted() {
							checkGPS();
						}

						@Override
						public void onDenied() {
							super.onDenied();

							checkGPS();

							Toast.makeText(SplashActivity.this, R.string.permission_request_fail,
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onAlwaysDenied() {
							super.onAlwaysDenied();

							PermissionUtils
									.showRequestPermissionRationaleDialog(SplashActivity.this, null,
											PERMISSION_REQUEST_SETTINGS,
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(DialogInterface dialog,
												                    int which) {
													Toast.makeText(SplashActivity.this,
															R.string.permission_request_fail,
															Toast.LENGTH_SHORT).show();
													checkGPS();
												}
											});
						}
					});
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PERMISSION_REQUEST_SETTINGS) {
			if (PermissionUtils
					.isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
				checkGPS();
			} else {
				Toast.makeText(this, R.string.permission_request_fail, Toast.LENGTH_SHORT).show();
				checkGPS();
			}
		} else if (requestCode == PERMISSION_REQUEST_GPS_SETTINGS) {
			startActivity();
		}
	}
}
