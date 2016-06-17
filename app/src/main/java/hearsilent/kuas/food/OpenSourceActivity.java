package hearsilent.kuas.food;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class OpenSourceActivity extends AppCompatActivity {

	RecyclerView mRecyclerView;

	OpenSourceAdapter mAdapter;

	private List<String> mProjectList;
	private List<String> mLicenseList;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open_source);

		initValues();
		findViews();
		setUpViews();
	}

	private void initValues() {
		mProjectList = Arrays.asList(getResources().getStringArray(R.array.project));
		mLicenseList = Arrays.asList(getResources().getStringArray(R.array.license));
	}

	private void findViews() {
		mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
	}

	private void setUpViews() {
		mAdapter = new OpenSourceAdapter();
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLayoutManager(
				new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		mRecyclerView.setAdapter(mAdapter);

		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class OpenSourceAdapter
			extends RecyclerView.Adapter<OpenSourceAdapter.OpenSourceViewHolder> {

		public class OpenSourceViewHolder extends RecyclerView.ViewHolder {

			TextView projectTextView;
			TextView licenseTextView;

			public OpenSourceViewHolder(View view) {
				super(view);

				projectTextView = (TextView) view.findViewById(R.id.textView_project);
				licenseTextView = (TextView) view.findViewById(R.id.textView_license);
			}
		}

		@Override
		public OpenSourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.list_open_source, parent, false);
			return new OpenSourceViewHolder(view);
		}

		@Override
		public void onBindViewHolder(OpenSourceViewHolder holder, int position) {
			holder.projectTextView.setText(mProjectList.get(position));
			holder.licenseTextView.setText(mLicenseList.get(position));
		}

		@Override
		public int getItemCount() {
			return mProjectList.size();
		}

	}
}
