package hearsilent.kuas.food;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.flaviofaria.kenburnsview.KenBurnsView;

import hearsilent.kuas.food.libs.Utils;

public class ShopsDetailActivity extends AppCompatActivity {

	Toolbar mToolbar;
	CollapsingToolbarLayout mCollapsingToolbar;
	AppBarLayout mAppBarLayout;
	KenBurnsView mHeaderImageView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shops_detail);

		findViews();
		setUpViews();
	}

	private void findViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
		mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
		mHeaderImageView = (KenBurnsView) findViewById(R.id.imageView_header);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mHeaderImageView != null) {
			mHeaderImageView.resume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mHeaderImageView != null) {
			mHeaderImageView.pause();
		}
	}

	private void setUpViews() {
		setSupportActionBar(mToolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}

		mCollapsingToolbar.getLayoutParams().height =
				Utils.getDisplayDimen(this).x * 9 / 16 + Utils.getStatusBarHeightPixel(this);
		mCollapsingToolbar.requestLayout();
	}

}
