package com.ocwvar.libfocusarea;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

@SuppressLint ("ViewConstructor")
final class FocusAnimView extends View {

	private int lastLeftSize = 0;
	private int lastTopSize = 0;

	private float currentWidth = 0.0f;
	private float currentHeight = 0.0f;

	//焦点图片Padding尺寸 , 必须是.9图格式
	private Rect drawablePaddingRect;

	//是否初始化完成
	private boolean initDone;

	//动画使用的加速器
	private Interpolator interpolator;

	//动画执行时长
	private long duration;

	/**
	 * @param context      上下文对象
	 * @param focusImgRes  使用的焦点图像
	 * @param duration     焦点动画时长 , 无效默认为 300
	 * @param interpolator 焦点动画使用的加速器 , 无效默认为 LinearOutSlowInInterpolator
	 */
	protected FocusAnimView( @NonNull final Context context, @DrawableRes int focusImgRes, final long duration, @Nullable final Interpolator interpolator ) {
		super( context );

		//这个View本身不应该有任何的交互属性
		setHeight( 0 );
		setWidth( 0 );
		setClickable( false );
		setFocusable( false );
		setVisibility( INVISIBLE );
		setFocusableInTouchMode( false );

		//获取 Drawable 并获取对应的Padding数据
		Drawable drawable;
		try {
			drawable = getResources().getDrawable( focusImgRes );
		} catch ( Resources.NotFoundException e ) {
			//读取图片资源失败 , 则使用默认的图片资源
			drawable = getResources().getDrawable( R.drawable.default_focus_img );
			focusImgRes = R.drawable.default_focus_img;
		}

		this.setBackgroundResource( focusImgRes );
		this.drawablePaddingRect = new Rect();
		drawable.getPadding( this.drawablePaddingRect );

		//设置其他属性
		this.duration = duration <= 0 ? 300L : duration;
		this.interpolator = interpolator == null ? new LinearOutSlowInInterpolator() : interpolator;

		//完成初始化的标记
		this.initDone = true;
	}

	/**
	 * @param duration 动画时长
	 */
	protected void setDuration( final long duration ) {
		this.duration = duration <= 0 ? 300L : duration;
	}

	/**
	 * @param interpolator 动画加速器
	 */
	protected void setInterpolator( final Interpolator interpolator ) {
		this.interpolator = interpolator == null ? new LinearOutSlowInInterpolator() : interpolator;
	}

	/**
	 * 更新焦点View的相关属性
	 *
	 * @param locationRectF 坐标对象
	 * @param smoothAnim    是否使用动画来更新位置
	 */
	protected void updateLocation( @Nullable final RectF locationRectF, boolean smoothAnim ) {
		if ( !initDone ) {
			setVisibility( GONE );
			return;
		}

		if ( locationRectF == null ) {
			//如果传入的数据为NULL , 则隐藏焦点View
			resetLocation();
			return;
		}

		//生成带Padding的尺寸以及坐标RectF对象
		final RectF reducedRectF = new RectF(
				locationRectF.left - drawablePaddingRect.left,
				locationRectF.top - drawablePaddingRect.top,
				locationRectF.right + drawablePaddingRect.right,
				locationRectF.bottom + drawablePaddingRect.bottom
		);

		if ( smoothAnim ) {
			//使用动画更新坐标以及尺寸
			setPositionAndSizeSmoothly( (int) reducedRectF.left, (int) reducedRectF.top, reducedRectF.width(), reducedRectF.height() );
		} else {
			//不使用动画更新位置
			setTranslationX( (int) reducedRectF.left );
			setTranslationY( (int) reducedRectF.top );
			setWidth( reducedRectF.width() );
			setHeight( reducedRectF.height() );
			this.lastLeftSize = (int) reducedRectF.left;
			this.lastTopSize = (int) reducedRectF.top;
			this.currentWidth = reducedRectF.width();
			this.currentHeight = reducedRectF.height();
		}
	}

	/**
	 * 以动画的形式 更新View的坐标以及尺寸数据
	 *
	 * @param left   LEFT坐标
	 * @param top    TOP坐标
	 * @param width  长度
	 * @param height 高度
	 */
	protected void setPositionAndSizeSmoothly( final int left, final int top, final float width, final float height ) {
		//生成对应的属性动画对象
		final ObjectAnimator xAnimator = ObjectAnimator.ofFloat( FocusAnimView.this, "TranslationX", this.lastLeftSize, left );
		final ObjectAnimator yAnimator = ObjectAnimator.ofFloat( FocusAnimView.this, "TranslationY", this.lastTopSize, top );
		final ObjectAnimator widthAnimator = ObjectAnimator.ofFloat( FocusAnimView.this, "Width", this.currentWidth, width );
		final ObjectAnimator heightAnimator = ObjectAnimator.ofFloat( FocusAnimView.this, "Height", this.currentHeight, height );

		//记录最新的坐标位置 , 给TranslationX  TranslationY属性动画使用
		this.lastLeftSize = left;
		this.lastTopSize = top;

		//进行播放
		final AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether( xAnimator, yAnimator, widthAnimator, heightAnimator );

		//设置加速器
		animatorSet.setInterpolator( this.interpolator );

		//设置时长
		animatorSet.setDuration( this.duration );

		//执行动画
		animatorSet.start();

		//使焦点框置于最顶层
		bringToFront();
	}

	/**
	 * 设置长度
	 *
	 * @param width View长度
	 */
	private void setWidth( final float width ) {
		this.currentWidth = width;
		setMeasuredDimension( (int) currentWidth, (int) currentHeight );
		setLayoutParams( new RelativeLayout.LayoutParams( (int) this.currentWidth, (int) this.currentHeight ) );
	}

	/**
	 * 设置高度
	 *
	 * @param height View高度
	 */
	private void setHeight( final float height ) {
		this.currentHeight = height;
		setMeasuredDimension( (int) currentWidth, (int) currentHeight );
		setLayoutParams( new RelativeLayout.LayoutParams( (int) this.currentWidth, (int) this.currentHeight ) );
	}

	/**
	 * 隐藏并重置所有状态
	 */
	private void resetLocation() {
		setVisibility( INVISIBLE );
		setLeft( 0 );
		setRight( 0 );
		setTop( 0 );
		setBottom( 0 );
		setMeasuredDimension( 0, 0 );
		requestLayout();
		postInvalidate();
	}

}
