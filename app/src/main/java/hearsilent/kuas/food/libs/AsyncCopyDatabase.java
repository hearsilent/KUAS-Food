package hearsilent.kuas.food.libs;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class AsyncCopyDatabase extends AsyncTask<Void, Integer, Boolean> {

	private WeakReference<Context> wContext;
	private WeakReference<DatabaseCopyListener> wListener;

	public interface DatabaseCopyListener {

		void onPreExecute(boolean success);
	}

	public AsyncCopyDatabase(Context context, DatabaseCopyListener listener) {
		wContext = new WeakReference<>(context);
		wListener = new WeakReference<>(listener);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		return wContext.get() != null && DatabaseUtils.copyDataBase(wContext.get());
	}

	@Override
	protected void onPostExecute(Boolean success) {
		if (wListener.get() != null) {
			wListener.get().onPreExecute(success);
		}
	}

}