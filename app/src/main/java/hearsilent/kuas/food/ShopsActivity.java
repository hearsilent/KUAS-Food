package hearsilent.kuas.food;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import hearsilent.kuas.food.fragment.ShopsFragment;
import hearsilent.kuas.food.libs.Constant;

public class ShopsActivity extends AppCompatActivity {

	Toolbar mToolbar;
	TabLayout mTabLayout;

	ViewPager mViewPager;

	ViewPagerAdapter mAdapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shops);

		findViews();
		setUpViews();
	}

	private void findViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mTabLayout = (TabLayout) findViewById(R.id.tabLayout);

		mViewPager = (ViewPager) findViewById(R.id.viewPager);
	}

	private void setUpViews() {
		setSupportActionBar(mToolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}

		mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		mViewPager.setOffscreenPageLimit(mAdapter.getCount());
		mViewPager.setAdapter(mAdapter);
		mTabLayout.setupWithViewPager(mViewPager);
		mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

			@Override
			public void onTabSelected(final TabLayout.Tab tab) {
				mViewPager.post(new Runnable() {

					@Override
					public void run() {
						mViewPager.setCurrentItem(tab.getPosition(), true);
					}
				});
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {
				Fragment fragment = getChildFragment(tab.getPosition());
				if (fragment instanceof ShopsFragment) {
					((ShopsFragment) fragment).smoothScrollToTop();
				}
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private Fragment getChildFragment(int position) {
		return getSupportFragmentManager().findFragmentByTag(mAdapter.getFragmentTag(position));
	}

	private class ViewPagerAdapter extends FragmentPagerAdapter {

		public ViewPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		public String getFragmentTag(int position) {
			return "android:switcher:" + R.id.viewPager + ":" + position;
		}

		@Override
		public Fragment getItem(int position) {
			return ShopsFragment.newInstance(position == 0 ? Constant.JIANGONG : Constant.YANCHAO);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return position == 0 ? getString(R.string.shop_region, Constant.JIANGONG) :
					getString(R.string.shop_region, Constant.YANCHAO);
		}
	}

}
