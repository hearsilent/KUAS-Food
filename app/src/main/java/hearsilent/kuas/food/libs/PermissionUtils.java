package hearsilent.kuas.food.libs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import hearsilent.kuas.food.R;
import hearsilent.kuas.food.callback.PermissionCallback;

/**
 * Created by HearSilent on 16/4/29.
 * Permission Utils
 * <p/>
 * Haven't support multi permissions.
 */
public class PermissionUtils {

	public static boolean isPermissionGranted(Context context, String permission) {
		return ContextCompat.checkSelfPermission(context, permission) ==
				PackageManager.PERMISSION_GRANTED;
	}

	private static void startSettingsDetailActivity(Activity activity, int requestCode) {
		try {
			Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
					.setData(Uri.parse("package:" + activity.getPackageName()));
			activity.startActivityForResult(intent, requestCode);
		} catch (ActivityNotFoundException e) {
			Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
			activity.startActivityForResult(intent, requestCode);
		}
	}

	private static void startSettingsDetailActivity(Context context, Fragment fragment,
	                                                int requestCode) {
		try {
			Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
					.setData(Uri.parse("package:" + context.getPackageName()));
			fragment.startActivityForResult(intent, requestCode);
		} catch (ActivityNotFoundException e) {
			Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
			fragment.startActivityForResult(intent, requestCode);
		}
	}

	/**
	 * @param activity              Use activity to getPackageName
	 * @param permission            Request permission
	 * @param permissionRequestCode should handle at onRequestPermissionsResult by yourself.
	 * @param callback              will callback if user granted
	 */
	@TargetApi(16)
	public static void checkPermissionGranted(@NonNull final Activity activity,
	                                          @NonNull String permission,
	                                          final int permissionRequestCode,
	                                          @NonNull PermissionCallback callback) {
		checkPermissionGranted(activity, null, permission, permissionRequestCode, callback);
	}

	/**
	 * @param activity              Use activity to getPackageName
	 * @param fragment              Use fragment to startActivityForResult
	 * @param permission            Request permission
	 * @param permissionRequestCode should handle at onRequestPermissionsResult by yourself.
	 * @param callback              will callback if user granted
	 */
	@TargetApi(16)
	public static void checkPermissionGranted(@NonNull final Activity activity,
	                                          @Nullable final Fragment fragment,
	                                          @NonNull String permission,
	                                          final int permissionRequestCode,
	                                          @NonNull PermissionCallback callback) {
		if (Utils.postVersion(Build.VERSION_CODES.JELLY_BEAN) &&
				!isPermissionGranted(activity, permission)) {
			if (fragment != null) {
				fragment.requestPermissions(new String[]{permission}, permissionRequestCode);
			} else {
				ActivityCompat.requestPermissions(activity, new String[]{permission},
						permissionRequestCode);
			}
		} else {
			callback.onGranted();
		}
	}

	/**
	 * Handle onRequestPermissionsResult with callback.
	 *
	 * @param permissions  Only handle one permission now (Want someone hack this code <(_ _)>).
	 * @param grantResults Only handle one grantResult now (Want someone hack this code <(_ _)>).
	 * @param activity     Use for check {@link android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback}.
	 * @param callback     Callback for parsed result.
	 */
	public static void onRequestPermissionsResult(@NonNull String permissions[],
	                                              @NonNull int[] grantResults,
	                                              @NonNull Activity activity,
	                                              @NonNull PermissionCallback callback) {
		if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			callback.onGranted();
		} else {
			// User denied
			if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0])) {
				callback.onDenied();
			} else { // User choose "Don’t ask again" or Device not support for this permission
				callback.onAlwaysDenied();
			}
		}
	}

	/**
	 * Show Request Permission Rationale Dialog
	 *
	 * @param activity            Use activity for startActivityForResult.
	 * @param fragment            Use fragment for startActivityForResult (If you need).
	 * @param settingsRequestCode startActivityForResult RequestCode.
	 * @param cancelListener      callback when user click cancel
	 */
	public static void showRequestPermissionRationaleDialog(@NonNull final Activity activity,
	                                                        @Nullable final Fragment fragment,
	                                                        final int settingsRequestCode, @NonNull
	                                                        DialogInterface.OnClickListener cancelListener) {
		new AlertDialog.Builder(activity)
				.setTitle(R.string.permission_request_access_fine_location_denied_title)
				.setMessage(R.string.permission_request_access_fine_location_denied_message)
				.setPositiveButton(R.string.go_to_settings, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (fragment == null) {
							startSettingsDetailActivity(activity, settingsRequestCode);
						} else {
							startSettingsDetailActivity(activity, fragment, settingsRequestCode);
						}
					}
				}).setNegativeButton(R.string.cancel, cancelListener).setCancelable(false).show();
	}

}
