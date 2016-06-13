package hearsilent.kuas.food.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import github.hellocsl.cursorwheel.CursorWheelLayout;
import hearsilent.kuas.food.R;

public class SimpleTextCursorWheelLayout extends CursorWheelLayout {

	private boolean mTouchable = true;
	private OnTouchListener mListener;

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
			tv.animate().scaleX(2).scaleY(2);
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
			tv.animate().scaleX(1).scaleY(1);
		}
	}

	public void setTouchable(boolean touchable) {
		mTouchable = touchable;
	}

	public void setOnTouchListener(OnTouchListener listener) {
		mListener = listener;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (mListener != null && mTouchable) {
			mListener.onTouch();
		}
		try {
			return mTouchable && super.dispatchTouchEvent(event);
		} catch (Exception e) {
			return false;
		}
	}

	public interface OnTouchListener {

		void onTouch();
	}
}
