package hearsilent.kuas.food.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

public class ShopsFragment extends Fragment
		implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
		LocationListener {

	View mRootView;
	RecyclerView mRecyclerView;

	LinearLayoutManager mLayoutManager;

	List<ShopModel> mShopList;

	ShopAdapter mAdapter;
	String mRegion;

	private GoogleApiClient mGoogleApiClient;
	LocationRequest mLocationRequest;

	private double mLatitude, mLongitude;
	private Location mLastLocation;

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
		checkPermission();
		findViews();
		setUpViews();

		return mRootView;
	}

	private void checkPermission() {
		if (mRegion.equals(Constant.JIANGONG)) {
			mLatitude = Constant.JIANGONG_LAT;
			mLongitude = Constant.JIANGONG_LNG;
		} else {
			mLatitude = Constant.YANCHAO_LAT;
			mLongitude = Constant.YANCHAO_LNG;
		}
		if (PermissionUtils
				.isPermissionGranted(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
			setUpGoogleApiClient();
		}
	}

	synchronized void setUpGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(getContext()).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
	}

	@SuppressWarnings("all")
	@Override
	public void onConnected(@Nullable Bundle bundle) {
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(500);

		if (Utils.checkGPSisOpen(getContext())) {
			LocationServices.FusedLocationApi
					.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

			getLocation(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
		}
	}

	@Override
	public void onLocationChanged(@NonNull Location location) {
		if (mLastLocation == null || location.getAccuracy() > mLastLocation.getAccuracy()) {
			getLocation(location);
		}
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		checkPermission();
	}

	@Override
	public void onPause() {
		super.onPause();
		checkPermission();
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mGoogleApiClient != null) {
			mGoogleApiClient.connect();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mGoogleApiClient != null) {
			mGoogleApiClient.disconnect();
		}
	}

	@SuppressWarnings("all")
	private void getLocation(Location location) {
		if (location != null) {
			mLastLocation = location;

			if (Double.compare(mLatitude, location.getLatitude()) != 0 ||
					Double.compare(mLongitude, location.getLongitude()) != 0) {
				mLatitude = location.getLatitude();
				mLongitude = location.getLongitude();
				mAdapter.notifyDataSetChanged();
			}
		}
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
