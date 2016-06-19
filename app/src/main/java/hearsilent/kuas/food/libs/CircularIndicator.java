package hearsilent.kuas.food.libs;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class CircularIndicator extends LinearLayout {

	private int mCount = 0;
	private int mPosition = 0;
	private List<ImageView> mDots = new ArrayList<>();

	public CircularIndicator(Context context) {
		super(context);
	}

	public CircularIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CircularIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	private void setUpView(int count) {
		if (count == 0) {
			removeAllViews();
			return;
		}
		if (mCount == count) {
			return;
		}

		int width = (int) Utils.convertDpToPixel(10, getContext());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);

		int radius = (int) Utils.convertDpToPixel(5, getContext());
		ShapeDrawable oval = new ShapeDrawable(new OvalShape());
		oval.setIntrinsicHeight(radius);
		oval.setIntrinsicWidth(radius);
		oval.getPaint().setColor(ContextCompat.getColor(getContext(), android.R.color.white));

		if (count > mCount) {
			for (int i = mCount; i < count; i++) {
				ImageView dot = new ImageView(getContext());
				mDots.add(i, dot);
				dot.setImageDrawable(oval);
				dot.setScaleType(ImageView.ScaleType.CENTER);
				addView(dot, params);
			}
		} else {
			for (int i = count; i < mCount; i++) {
				ImageView dot = mDots.get(i);
				removeView(dot);
			}
			mDots = mDots.subList(0, count);
		}

		if (mPosition < count) {
			mDots.get(mPosition).animate().scaleX(1f).scaleY(1f).start();
		}
		mDots.get(0).animate().scaleX(1.6f).scaleY(1.6f).start();
		mPosition = 0;
		mCount = count;
	}

	public void setCount(int count) {
		setUpView(count);
	}

	public int getCount() {
		return mCount;
	}

	public void setSelection(int position) {
		position = position % mCount;
		mDots.get(mPosition).animate().scaleX(1f).scaleY(1f).start();
		mDots.get(position).animate().scaleX(1.6f).scaleY(1.6f).start();
		mPosition = position;
	}

	public int getSelection() {
		return mPosition;
	}

}
