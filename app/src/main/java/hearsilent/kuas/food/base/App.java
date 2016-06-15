package hearsilent.kuas.food.base;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.litepal.LitePalApplication;

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
