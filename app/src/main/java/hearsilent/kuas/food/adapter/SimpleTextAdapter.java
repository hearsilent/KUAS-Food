package hearsilent.kuas.food.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import github.hellocsl.cursorwheel.CursorWheelLayout;
import hearsilent.kuas.food.R;

public class SimpleTextAdapter implements CursorWheelLayout.CycleWheelAdapter {

	private List<String> mList;
	private LayoutInflater mLayoutInflater;

	public SimpleTextAdapter(Context context, List<String> list) {
		mLayoutInflater = LayoutInflater.from(context);
		mList = list;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public View getView(View parent, int position) {
		String item = getItem(position);

		View root = mLayoutInflater.inflate(R.layout.wheel_menu_item, (ViewGroup) parent, false);
		TextView textView = (TextView) root.findViewById(R.id.wheel_menu_item_tv);

		textView.setVisibility(View.VISIBLE);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		textView.setText(item);

		return root;
	}

	@Override
	public String getItem(int position) {
		return mList.get(position);
	}

}
