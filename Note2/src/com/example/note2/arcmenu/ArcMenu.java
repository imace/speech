package com.example.note2.arcmenu;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.example.note2.R;

	public class ArcMenu extends ViewGroup implements OnClickListener {

	private static final int POS_LEFT_TOP = 0;
	private static final int POS_LEFT_BOTTOM = 1;
	private static final int POS_RIGHT_TOP = 2;
	private static final int POS_RIGHT_BOTTOM = 3;

	private Position mPosition = Position.LEFT_BOTTOM;
	private int mRadius;
	private Status mCurrentStatus = Status.CLOSE;
	private View mCButton;// main button��
	private OnMenuItemClickListener mMenuItemClickListener;

	

	/*
	 * �˵���״̬
	 */
	public enum Status {
		OPEN, CLOSE;
	}

	/*
	 * �˵�ö�����λ��
	 */
	public enum Position {
		LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM;
	}

	/*
	 * ����Ӳ˵��Ļص��ӿڣ�����д��Ա����mMenu��������
	 */
	public interface OnMenuItemClickListener
	{
		void onClick(View view, int pos);
	}

	/*
	 * ���û�һ�����Իص���set����
	 */
	public void setOnMenuItemClickListener(
			OnMenuItemClickListener mMenuItemClickListener)
	{
		this.mMenuItemClickListener = mMenuItemClickListener;
	}


	public ArcMenu(Context context) {
		this(context, null);

	}

	public ArcMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	// ��ʼ��
	public ArcMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// Ĭ�ϰ뾶
		mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				100, getResources().getDisplayMetrics());
		// ��ȡ�Զ������Ե�ֵ
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.ArcMenu, defStyle, 0);

		int pos = a.getInt(R.styleable.ArcMenu_position, POS_LEFT_BOTTOM);
		switch (pos) {
		case POS_LEFT_TOP:
			mPosition = Position.LEFT_TOP;
			break;
		case POS_LEFT_BOTTOM:
			mPosition = Position.LEFT_BOTTOM;
			break;
		case POS_RIGHT_TOP:
			mPosition = Position.RIGHT_TOP;
			break;
		case POS_RIGHT_BOTTOM:
			mPosition = Position.RIGHT_BOTTOM;
			break;

		default:
			break;
		}

		mRadius = (int) a.getDimension(R.styleable.ArcMenu_radius,
				TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
						100, getResources().getDisplayMetrics()));

		Log.e("TAG", "mPosition" + mPosition + ",radius" + mRadius);

		a.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int count = getChildCount();

		for (int i = 0; i < count; i++) {
			// ����child
			measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);

		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		if (changed) {
			layoutCButton();

			int count = getChildCount();
			for (int i = 0; i < count - 1; i++) {
				View child = getChildAt(i + 1);

				child.setVisibility(View.GONE);

				int cl = (int) (mRadius * Math.sin(Math.PI / 2 / ((count - 2))
						* i));
				int ct = (int) (mRadius * Math.cos(Math.PI / 2 / ((count - 2))
						* i));

				int cWidth = child.getMeasuredWidth();
				int cHeight = child.getMeasuredHeight();

				if (mPosition == Position.RIGHT_BOTTOM
						|| mPosition == Position.LEFT_BOTTOM) {
					ct = getMeasuredHeight() - cHeight - ct;
				}
				if (mPosition == Position.RIGHT_TOP
						|| mPosition == Position.RIGHT_BOTTOM) {
					cl = getMeasuredWidth() - cWidth - cl;

				}
				child.layout(cl, ct, cl + cWidth, ct + cHeight);
			}
		}
	}

	// ��λ���˵���ť��
	private void layoutCButton() {

		mCButton = getChildAt(0);
		mCButton.setOnClickListener(this);

		int l = 0;
		int t = 0;

		int width = mCButton.getMeasuredWidth();
		int height = mCButton.getMeasuredHeight();

		switch (mPosition) {
		case LEFT_TOP:
			l = 0;
			t = 0;
			break;
		case LEFT_BOTTOM:
			l = 0;
			t = getMeasuredHeight() - height;
			break;
		case RIGHT_TOP:
			l = getMeasuredWidth() - width;
			t = 0;
			break;
		case RIGHT_BOTTOM:
			l = getMeasuredWidth() - width;
			t = getMeasuredHeight() - height;
			break;

		}

		mCButton.layout(l, t, l + width, t + height);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		rotateCButton(v, 0f, 360f, 300);

		toggleMenu(300);

	}

	// �л��˵�
	public void toggleMenu(int duration) {

		int count = getChildCount();
		for (int i = 0; i < count - 1; i++) {

			final View childView = getChildAt(i + 1);// �ڲ��࣬��final

			childView.setVisibility(View.VISIBLE);

			// end 0,0;
			// start

			int cl = (int) (mRadius * Math.sin(Math.PI / 2 / ((count - 2)) * i));
			int ct = (int) (mRadius * Math.cos(Math.PI / 2 / ((count - 2)) * i));

			int xFlag = 1;
			int yFlag = 1;

			if (mPosition == Position.LEFT_TOP
					|| mPosition == Position.LEFT_BOTTOM) {
				xFlag = -1;
			}
			if (mPosition == Position.LEFT_TOP
					|| mPosition == Position.RIGHT_TOP) {
				yFlag = -1;
			}

			AnimationSet animset = new AnimationSet(true);
			Animation transAnim = null;

			// to open
			if (mCurrentStatus == Status.CLOSE) {
				transAnim = new TranslateAnimation(xFlag * cl, 0, yFlag * ct, 0);

				childView.setClickable(true);
				childView.setFocusable(true);
			} else {// to close
				transAnim = new TranslateAnimation(0, xFlag * cl, 0, yFlag * ct);

				childView.setClickable(false);
				childView.setFocusable(false);
			}

			transAnim.setFillAfter(true);
			transAnim.setDuration(duration);
			transAnim.setStartOffset((i * 100) / count);

			// ����������������ͼ������
			transAnim.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {

					if (mCurrentStatus == Status.CLOSE) {
						childView.setVisibility(View.GONE);
					}
				}
			});

			// ��ת����
			RotateAnimation roataAnim = new RotateAnimation(0, 720,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);

			roataAnim.setDuration(duration);
			roataAnim.setFillAfter(true);

			animset.addAnimation(roataAnim);
			animset.addAnimation(transAnim);

			childView.startAnimation(animset);

			final int pos = i + 1; 
			childView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (mMenuItemClickListener != null){
						mMenuItemClickListener.onClick(childView, pos);
					}

					menuItemAnim(pos - 1);
					changeStatus();
					

				}
			});
		}
		// �л��˵�״̬
		changeStatus();

	}

	/*
	 * ���menuItem�ĵ������
	 */
	private void menuItemAnim(int pos) {

		for (int i = 0; i < getChildCount() - 1; i++) {

			View childView = getChildAt(i + 1);
			if (i == pos) {

				childView.startAnimation(scaleBigAnim(300));

			} else {
				childView.startAnimation(scaleSmallAnim(300));
			}

			childView.setClickable(false);
			childView.setFocusable(false);

		}
	}

	/*
	 * Ϊ��ǰ�����item���÷Ŵ���С�ͽ���͸���ȵĶ���
	 */
	private Animation scaleSmallAnim(int duration) {
		AnimationSet animationSet = new AnimationSet(true);
		ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);
		
		animationSet.addAnimation(scaleAnim);
		animationSet.addAnimation(alphaAnim);
		
		animationSet.setDuration(duration);
		animationSet.setFillAfter(true);

		return animationSet;
		
	}

	private Animation scaleBigAnim(int duration) {

		AnimationSet animationSet = new AnimationSet(true);
		ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 3.0f, 1.0f, 3.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);
		
		animationSet.addAnimation(scaleAnim);
		animationSet.addAnimation(alphaAnim);
		
		animationSet.setDuration(duration);
		animationSet.setFillAfter(true);

		return animationSet;
	}
	
	public boolean isOpen(){
		return mCurrentStatus ==Status.OPEN;
	}

	private void changeStatus() {

		mCurrentStatus = (mCurrentStatus == Status.CLOSE ? Status.OPEN
				: Status.CLOSE);

	}

	private void rotateCButton(View v, float start, float end, int duration) {

		Animation anim = new RotateAnimation(start, end,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);

		anim.setDuration(duration);
		anim.setFillAfter(true);
		v.startAnimation(anim);

	}

}
