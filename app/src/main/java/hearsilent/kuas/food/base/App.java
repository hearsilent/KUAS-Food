package hearsilent.kuas.food.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by Artem Kholodnyi on 11/2/15.
 */
public class App extends Application {

	private static App app;

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
	}

	public static Context getAppContext() {
		return app;
	}
}
