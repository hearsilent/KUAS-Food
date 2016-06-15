package hearsilent.kuas.food.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import hearsilent.kuas.food.R;
import hearsilent.kuas.food.ShopDetailActivity;
import hearsilent.kuas.food.libs.Constant;
import hearsilent.kuas.food.libs.DatabaseUtils;
import hearsilent.kuas.food.libs.PermissionUtils;
import hearsilent.kuas.food.libs.Utils;
import hearsilent.kuas.food.models.ShopModel;

public class ShopsFragment extends Fragment implements LocationListener {

	View mRootView;
	RecyclerView mRecyclerView;

	LinearLayoutManager mLayoutManager;

	List<ShopModel> mShopList;

	ShopAdapter mAdapter;
	String mRegion;

	private LocationManager mLocationManager;
	private String bestProvider = LocationManager.GPS_PROVIDER;

	private double mLatitude, mLongitude;

	public static ShopsFragment newInstance(@Utils.Location String region) {
		ShopsFragment shopsFragment = new ShopsFragment();
		Bundle bundle = new Bundle();
		bundle.putString("region", region);
		shopsFragment.setArguments(bundle);
		return shopsFragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		if (mRootView != null) {
			return mRootView;
		}

		mRootView = inflater.inflate(R.layout.fragment_shops, container, false);

		initValues();
		findViews();
		setUpViews();

		return mRootView;
	}

	private void checkGPS() {
		if (!PermissionUtils
				.isPermissionGranted(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
			return;
		}
		LocationManager status =
				(LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
		if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
				status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			setUpLocationService();
		} else {
			Toast.makeText(getContext(), R.string.gps_not_open, Toast.LENGTH_LONG).show();
			startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		}
	}

	@SuppressWarnings("all")
	private void setUpLocationService() {
		mLocationManager =
				(LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		bestProvider = mLocationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null ?
				LocationManager.NETWORK_PROVIDER : mLocationManager.getBestProvider(criteria, true);
		Location location = mLocationManager.getLastKnownLocation(bestProvider);
		getLocation(location);
	}

	@SuppressWarnings("all")
	private void getLocation(Location location) {
		if (location != null) {
			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();
			if (Double.compare(mLatitude, location.getLatitude()) != 0 ||
					Double.compare(mLongitude, location.getLongitude()) != 0) {
				mAdapter.notifyDataSetChanged();
			}
		} else {
			Toast.makeText(getContext(), R.string.gps_not_work, Toast.LENGTH_SHORT).show();
		}
	}

	@SuppressWarnings("all")
	@Override
	public void onResume() {
		super.onResume();
		if (mLocationManager != null) {
			mLocationManager.requestLocationUpdates(bestProvider, 500, 1, this);
		} else {
			checkGPS();
		}
	}

	@SuppressWarnings("all")
	@Override
	public void onPause() {
		super.onPause();
		if (mLocationManager != null) {
			mLocationManager.removeUpdates(this);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		getLocation(location);
	}

	@Override
	public void onProviderDisabled(String arg0) {
	}

	@Override
	public void onProviderEnabled(String arg0) {
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	}

	private void initValues() {
		mRegion = getArguments().getString("region", Constant.JIANGONG);
		if (mRegion.equals(Constant.JIANGONG)) {
			mShopList = DatabaseUtils.getJianGongList();
			mLatitude = Constant.JIANGONG_LAT;
			mLongitude = Constant.JIANGONG_LNG;
		} else {
			mShopList = DatabaseUtils.getYanChaoList();
			mLatitude = Constant.YANCHAO_LAT;
			mLongitude = Constant.YANCHAO_LNG;
		}
	}

	private void findViews() {
		mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
	}

	private void setUpViews() {
		mAdapter = new ShopAdapter();
		mRecyclerView.setHasFixedSize(true);

		mLayoutManager =
				new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {

					@Override
					public void smoothScrollToPosition(RecyclerView recyclerView,
					                                   RecyclerView.State state, int position) {
						LinearSmoothScroller linearSmoothScroller =
								new LinearSmoothScroller(recyclerView.getContext()) {

									@Override
									protected int calculateTimeForScrolling(int dx) {
										if (dx > 1500) {
											dx = 1500;
										}
										return super.calculateTimeForScrolling(dx);
									}

									@Override
									public PointF computeScrollVectorForPosition(
											int targetPosition) {
										return mLayoutManager
												.computeScrollVectorForPosition(targetPosition);
									}
								};
						linearSmoothScroller.setTargetPosition(position);
						startSmoothScroll(linearSmoothScroller);
					}
				};
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setAdapter(mAdapter);
	}

	public void smoothScrollToTop() {
		if (mRecyclerView != null) {
			mRecyclerView.post(new Runnable() {

				@Override
				public void run() {
					mRecyclerView.smoothScrollToPosition(0);
				}
			});
		}
	}

	public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

		public class ShopViewHolder extends RecyclerView.ViewHolder
				implements View.OnClickListener {

			CardView cardView;
			View navigationView;

			TextView nameTextView;
			TextView phoneTextView;
			TextView timeTextView;
			TextView disTextView;

			ImageView imageView;

			public ShopViewHolder(View view) {
				super(view);

				cardView = (CardView) view.findViewById(R.id.cardView);
				navigationView = view.findViewById(R.id.view_navigation);

				nameTextView = (TextView) view.findViewById(R.id.textView_name);
				phoneTextView = (TextView) view.findViewById(R.id.textView_phone);
				timeTextView = (TextView) view.findViewById(R.id.textView_time);
				disTextView = (TextView) view.findViewById(R.id.textView_dis);

				imageView = (ImageView) view.findViewById(R.id.imageView);

				cardView.setOnClickListener(this);
				navigationView.setOnClickListener(this);
			}

			@Override
			public void onClick(View v) {
				int position = getAdapterPosition();
				ShopModel model = mShopList.get(position);
				if (v == cardView) {
					Intent intent = new Intent(getContext(), ShopDetailActivity.class);
					intent.putExtra("shop", new Gson().toJson(model));
					startActivity(intent);
				} else {
					Utils.startNavigationActivity(getContext(), mLatitude, mLongitude,
							model.getLat(), model.getLng());
				}
			}
		}

		@Override
		public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.list_shop, parent, false);
			return new ShopViewHolder(view);
		}

		@Override
		public void onBindViewHolder(ShopViewHolder holder, int position) {
			ShopModel model = mShopList.get(position);
			holder.nameTextView.setText(model.getName());
			holder.timeTextView.setText(getString(R.string.shop_time, model.getTime()));
			holder.phoneTextView.setText(getString(R.string.shop_phone, model.getPhone()));
			holder.disTextView.setText(getString(R.string.shop_dis,
					Utils.gps2m(mLatitude, mLongitude, model.getLat(), model.getLng()) / 1000));
			ImageLoader.getInstance().displayImage(model.getImage(), holder.imageView,
					Utils.getDisplayImageBuilder().build());
		}

		@Override
		public int getItemCount() {
			return mShopList.size();
		}

	}

}
