package hearsilent.kuas.food.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import github.hellocsl.cursorwheel.CursorWheelLayout;
import hearsilent.kuas.food.R;

public class SimpleTextCursorWheelLayout extends CursorWheelLayout {

	private boolean mTouchable = true;

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

	public void setTouchable(boolean touchable) {
		mTouchable = touchable;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		try {
			return mTouchable && super.dispatchTouchEvent(event);
		} catch (Exception e) {
			return false;
		}
	}
}
