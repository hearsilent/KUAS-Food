package hearsilent.kuas.food.libs;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.StringDef;
import android.util.DisplayMetrics;

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

}
