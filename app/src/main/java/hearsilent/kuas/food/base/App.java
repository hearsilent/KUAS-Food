package hearsilent.kuas.food.base;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.litepal.LitePalApplication;

import io.fabric.sdk.android.Fabric;

public class App extends Application {

	private static App app;

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;

		// Init LitePal
		LitePalApplication.initialize(this);
		// Init ImageLoader
		initImageLoader(this);
		// Init Fabric
		Fabric.with(this, new Crashlytics());
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config =
				new ImageLoaderConfiguration.Builder(context).threadPoolSize(5).build();

		ImageLoader.getInstance().init(config);
	}

	public static Context getAppContext() {
		return app;
	}
}
