package hearsilent.kuas.food.base;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

public class App extends Application {

	private static App app;

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;

		// Init LitePal
		LitePalApplication.initialize(this);
	}

	public static Context getAppContext() {
		return app;
	}
}
