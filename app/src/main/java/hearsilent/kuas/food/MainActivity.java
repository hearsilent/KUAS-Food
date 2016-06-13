package hearsilent.kuas.food;

import android.animation.Animator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import github.hellocsl.cursorwheel.CursorWheelLayout;
import hearsilent.kuas.food.adapter.SimpleTextAdapter;
import hearsilent.kuas.food.libs.Constant;
import hearsilent.kuas.food.libs.Memory;
import hearsilent.kuas.food.libs.Utils;
import hearsilent.kuas.food.particlesys.ParticleSystemRenderer;
import hearsilent.kuas.food.widget.SimpleTextCursorWheelLayout;

public class MainActivity extends AppCompatActivity {

	Toolbar mToolbar;
	TextView mTitleTextView, mFoodTextView;

	ImageView mSelectImageView;

	GLSurfaceView mGlSurfaceView;

	SimpleTextCursorWheelLayout mCursorWheelLayout;

	boolean firstTime = true;
	boolean select = false;

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

		mFoodTextView = (TextView) findViewById(R.id.textView_food);
		mSelectImageView = (ImageView) findViewById(R.id.id_wheel_menu_center_item);

		mGlSurfaceView = (GLSurfaceView) findViewById(R.id.glSurfaceView);

		mCursorWheelLayout = (SimpleTextCursorWheelLayout) findViewById(R.id.cursorWheelLayout);
	}

	private void setUpViews() {
		setUpBackground();
		setUpTitle();
		setUpWheel();
		setUpHintBubble();
	}

	private void setUpHintBubble() {
		//		if (Memory.getBoolean(this, Constant.PREF_HINT, false)) {
		//			return;
		//		}
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
						location[0] - (int) Utils.convertDpToPixel(38f, MainActivity.this),
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
					mFoodTextView.animate().scaleX(1f).scaleY(1f);
				}
				List<String> test = new ArrayList<>(
						Arrays.asList("丹丹漢堡", "紅牛牛肉麵", "第一名火雞肉飯", "孩餃王", "麥當勞", "學園"));
				mFoodTextView.setText(test.get(new Random().nextInt(test.size())));
			}

			@Override
			public void onEnd() {
				if (!select) {
					select = true;
					mFoodTextView.animate().scaleX(1.2f).scaleY(1.2f);
				}
			}
		});
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
				mFoodTextView.setVisibility(View.VISIBLE);
				mFoodTextView.animate().alpha(1f);
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
		return super.onOptionsItemSelected(item);
	}
}
