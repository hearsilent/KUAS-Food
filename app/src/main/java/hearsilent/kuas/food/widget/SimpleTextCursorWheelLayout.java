package hearsilent.kuas.food.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import github.hellocsl.cursorwheel.CursorWheelLayout;
import hearsilent.kuas.food.R;

public class SimpleTextCursorWheelLayout extends CursorWheelLayout {

	public SimpleTextCursorWheelLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onInnerItemSelected(View v) {
		super.onInnerItemSelected(v);
		if (v == null) {
			return;
		}
		View tv = v.findViewById(R.id.wheel_menu_item_tv);
		if (tv != null) {
			tv.animate().scaleX(1.8f).scaleY(1.8f).alpha(1f);
		}
	}

	@Override
	protected void onInnerItemUnselected(View v) {
		super.onInnerItemUnselected(v);
		if (v == null) {
			return;
		}
		View tv = v.findViewById(R.id.wheel_menu_item_tv);
		if (tv != null) {
			tv.animate().scaleX(1f).scaleY(1f).alpha(0.8f);
		}
	}

}
