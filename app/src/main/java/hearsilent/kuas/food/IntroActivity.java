package hearsilent.kuas.food;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import github.hellocsl.cursorwheel.CursorWheelLayout;
import hearsilent.kuas.food.adapter.SimpleTextAdapter;
import hearsilent.kuas.food.libs.CircularIndicator;
import hearsilent.kuas.food.libs.Constant;
import hearsilent.kuas.food.libs.Memory;
import hearsilent.kuas.food.widget.SimpleTextCursorWheelLayout;

public class IntroActivity extends AppCompatActivity {

	private SimpleTextCursorWheelLayout mCursorWheelLayout;
	private ImageView mSelectImageView;

	private CircularIndicator mCircularIndicator;

	private TextView mHintTextView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		setContentView(R.layout.activty_intro);

		findViews();
		setUpViews();
	}

	private void findViews() {
		mCursorWheelLayout = (SimpleTextCursorWheelLayout) findViewById(R.id.cursorWheelLayout);
		mSelectImageView = (ImageView) findViewById(R.id.imageView_select);

		mHintTextView = (TextView) findViewById(R.id.textView_hint);

		mCircularIndicator = (CircularIndicator) findViewById(R.id.indicator_photo);
	}

	private void setUpViews() {
		mCircularIndicator.setCount(2);

		List<String> list = new ArrayList<>(Arrays.asList("K", "U", "A", "S", "F", "O", "O", "D"));
		SimpleTextAdapter adapter = new SimpleTextAdapter(this, list);
		mCursorWheelLayout.setAdapter(adapter);

		mCursorWheelLayout.setOnWheelFlingListener(new CursorWheelLayout.OnWheelFlingListener() {

			@Override
			public void onFling(boolean autoFling) {
			}

			@Override
			public void onEnd() {
				mCursorWheelLayout.setOnWheelFlingListener(null);
				mCursorWheelLayout.setTouchable(false);

				mHintTextView.animate().setDuration(500).alpha(0f)
						.setListener(new Animator.AnimatorListener() {

							@Override
							public void onAnimationStart(Animator animation) {

							}

							@Override
							public void onAnimationEnd(Animator animation) {
								mHintTextView.setText(R.string.good_job);
								mHintTextView.animate().setDuration(500).alpha(1f)
										.setListener(new Animator.AnimatorListener() {

											@Override
											public void onAnimationStart(Animator animation) {

											}

											@Override
											public void onAnimationEnd(Animator animation) {
												mHintTextView.animate().setDuration(500).alpha(0f)
														.setListener(null).start();
												new Handler().postDelayed(new Runnable() {

													@Override
													public void run() {
														mHintTextView.animate().setDuration(500)
																.alpha(1f).start();
														setUpOnClick();
													}
												}, 500);
											}

											@Override
											public void onAnimationCancel(Animator animation) {

											}

											@Override
											public void onAnimationRepeat(Animator animation) {

											}
										}).start();
							}

							@Override
							public void onAnimationCancel(Animator animation) {

							}

							@Override
							public void onAnimationRepeat(Animator animation) {

							}
						}).start();
			}
		});
	}

	private void setUpOnClick() {
		mCircularIndicator.setSelection(1);
		mHintTextView.setText(R.string.intro_2);
		mSelectImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mSelectImageView.setEnabled(false);
				mHintTextView.animate().setDuration(500).alpha(0f)
						.setListener(new Animator.AnimatorListener() {

							@Override
							public void onAnimationStart(Animator animation) {

							}

							@Override
							public void onAnimationEnd(Animator animation) {
								mHintTextView.setText(R.string.good_job);
								mHintTextView.animate().setDuration(500).alpha(1f).setListener(null)
										.start();
								new Handler().postDelayed(new Runnable() {

									@Override
									public void run() {
										Memory.setBoolean(IntroActivity.this, Constant.PREF_HINT,
												true);
										startActivity();
									}
								}, 500);
							}

							@Override
							public void onAnimationCancel(Animator animation) {

							}

							@Override
							public void onAnimationRepeat(Animator animation) {

							}
						}).start();
			}
		});
	}

	private void startActivity() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				startActivity(new Intent(IntroActivity.this, MainActivity.class));
				finish();
			}
		}, 800);
	}

}
