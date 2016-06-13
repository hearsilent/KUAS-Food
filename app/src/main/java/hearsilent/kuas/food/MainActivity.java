package hearsilent.kuas.food;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import github.hellocsl.cursorwheel.CursorWheelLayout;
import hearsilent.kuas.food.adapter.SimpleTextAdapter;
import hearsilent.kuas.food.libs.Constant;
import hearsilent.kuas.food.particlesys.ParticleSystemRenderer;
import hearsilent.kuas.food.widget.SimpleTextCursorWheelLayout;

public class MainActivity extends AppCompatActivity {

	Toolbar mToolbar;
	TextView mTitleTextView;

	GLSurfaceView mGlSurfaceView;

	SimpleTextCursorWheelLayout mCursorWheelLayout;

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

		mGlSurfaceView = (GLSurfaceView) findViewById(R.id.glSurfaceView);

		mCursorWheelLayout = (SimpleTextCursorWheelLayout) findViewById(R.id.cursorWheelLayout);
	}

	private void setUpViews() {
		setUpBackground();
		setUpTitle();
		setUpWheel();
	}

	private void setUpBackground() {
		final ActivityManager activityManager =
				(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

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
				Log.d(Constant.TAG, "OK");
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
		return super.onOptionsItemSelected(item);
	}
}
