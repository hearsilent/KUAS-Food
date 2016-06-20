package hearsilent.kuas.food.libs;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class NoPaddingTextView extends TextView {

	private Rect mTextBounds = new Rect();

	private int mLineHeight;
	private int mHeight;
	private String mText;

	public NoPaddingTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
		setUpTextBounds();
	}

	private void init() {
		getPaint().setColor(getCurrentTextColor());
		getPaint().setTextSize(getTextSize());
		getPaint().setTypeface(getTypeface());
		mText = getText().toString();
	}

	private void setUpTextBounds() {
		if (!TextUtils.isEmpty(mText) && mTextBounds != null) {
			for (String line : mText.split("\n")) {
				getPaint().getTextBounds(line, 0, line.length(), mTextBounds);
				mHeight += mTextBounds.height();
			}
			mLineHeight = mTextBounds.height();
		} else {
			mHeight = 0;
			mLineHeight = 0;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec,
				MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		int x = -mTextBounds.left, y = -mTextBounds.top;
		for (String line : mText.split("\n")) {
			if (getGravity() == Gravity.CENTER || getGravity() == Gravity.CENTER_HORIZONTAL) {
				getPaint().getTextBounds(line, 0, line.length(), mTextBounds);
				canvas.drawText(line, (canvas.getWidth() - mTextBounds.width()) / 2, y, getPaint());
			} else {
				canvas.drawText(line, x, y, getPaint());
			}
			y += mLineHeight;
		}
		canvas.restore();
	}

	@Override
	public void setTextSize(float size) {
		super.setTextSize(size);
		getPaint().setTextSize(getTextSize());
		setUpTextBounds();
		invalidate();
	}

	@Override
	public void setTextSize(int unit, float size) {
		super.setTextSize(unit, size);
		getPaint().setTextSize(getTextSize());
		setUpTextBounds();
		invalidate();
	}

	@Override
	public void setTextAppearance(int resId) {
		super.setTextAppearance(resId);
		getPaint().setTypeface(getTypeface());
		setUpTextBounds();
		invalidate();
	}

	@Override
	public void setTextColor(int color) {
		super.setTextColor(color);
		getPaint().setColor(getCurrentTextColor());
	}

	@Override
	public void setTextColor(ColorStateList colors) {
		super.setTextColor(colors);
		getPaint().setColor(getCurrentTextColor());
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		super.setText(text, type);
		mText = getText().toString();
		setUpTextBounds();
		invalidate();
	}

}
