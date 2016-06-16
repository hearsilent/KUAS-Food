package hearsilent.kuas.food.libs;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.StringDef;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Utils {

	private static final double EARTH_RADIUS = 6378137.0;

	@StringDef({Constant.JIANGONG, Constant.YANCHAO}) @Retention(RetentionPolicy.SOURCE)
	public @interface Location {

	}

	@Location
	public static String checkLocation(double lat, double lng) {
		return Double.compare(gps2m(lat, lng, Constant.JIANGONG_LAT, Constant.JIANGONG_LNG),
				gps2m(lat, lng, Constant.YANCHAO_LAT, Constant.YANCHAO_LNG)) <= 0 ?
				Constant.JIANGONG : Constant.YANCHAO;
	}

	public static double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
		double radLat1 = (lat_a * Math.PI / 180.0);
		double radLat2 = (lat_b * Math.PI / 180.0);
		double a = radLat1 - radLat2;
		double b = (lng_a - lng_b) * Math.PI / 180.0;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
				Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	public static DisplayMetrics getDisplayMetrics(Context context) {
		Resources resources = context.getResources();
		return resources.getDisplayMetrics();
	}

	/**
	 * This method converts dp unit to equivalent pixels, depending on device
	 * density.
	 *
	 * @param dp      A value in dp (density independent pixels) unit. Which we need
	 *                to convert into pixels
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on
	 * device density
	 */
	public static float convertDpToPixel(float dp, Context context) {
		return dp * (getDisplayMetrics(context).densityDpi / 160f);
	}

	/**
	 * This method converts dp unit to equivalent pixels, depending on device
	 * density.
	 *
	 * @param dp      A value in dp (density independent pixels) unit. Which we need
	 *                to convert into pixels
	 * @param context Context to get resources and device specific display metrics
	 * @return Value multiplied by the appropriate metric and truncated to
	 * integer pixels.
	 */
	public static int convertDpToPixelSize(float dp, Context context) {
		float pixels = convertDpToPixel(dp, context);
		final int res = (int) (pixels + 0.5f);
		if (res != 0) {
			return res;
		} else if (pixels == 0) {
			return 0;
		} else if (pixels > 0) {
			return 1;
		}
		return -1;
	}

	/**
	 * This method converts device specific pixels to density independent
	 * pixels.
	 *
	 * @param px      A value in px (pixels) unit. Which we need to convert into db
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return px / (metrics.densityDpi / 160f);
	}

	public static boolean postVersion(int sdkInt) {
		return Build.VERSION.SDK_INT >= sdkInt;
	}

	public static DisplayImageOptions.Builder getDisplayImageBuilder() {
		return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.displayer(new FadeInBitmapDisplayer(500));
	}

	public static int getStatusBarHeightPixel(Context context) {
		int result = 0;
		int resourceId =
				context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public static Point getDisplayDimen(Context context) {
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size;
	}

	public static void startNavigationActivity(Context context, double startLatitude,
	                                           double startLongitude, double endLatitude,
	                                           double endLongitude) {
		String saddr = "saddr=" + startLatitude + "," + startLongitude;
		String daddr = "daddr=" + endLatitude + "," + endLongitude;
		String uriString = "http://maps.google.com/maps?" + saddr + "&" + daddr;

		Uri uri = Uri.parse(uriString);

		try {
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
			intent.setClassName("com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity");
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
			context.startActivity(intent);
		}
	}

	public static boolean hasGooglePlayServices(Context context) {
		return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) ==
				ConnectionResult.SUCCESS;
	}

	public static boolean checkGPSisOpen(Context context) {
		LocationManager manager =
				(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		return manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
				manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

}
