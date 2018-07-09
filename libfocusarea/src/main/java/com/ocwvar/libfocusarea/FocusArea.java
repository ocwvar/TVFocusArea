package com.ocwvar.libfocusarea;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * TV光标
 * <p>
 * Bugs：
 * 1.RecycleView 中使用 addItemDecoration 来添加每个 Item 的间距会导致发生偏移
 * 原因： 在获取到 View 的坐标并没有被 addItemDecoration 影响，即无法知道 View 是否被 ItemDecoration 影响导致坐标偏移
 */
public class FocusArea extends RelativeLayout implements ViewTreeObserver.OnGlobalFocusChangeListener {

	//负责执行动画的View
	private FocusAnimView focusAnimView;

	//回调接口
	private OnFocusChangedCallback focusChangedCallback = null;

	//忽略的Tags
	private String[] ignoreTags;

	//缩放等级
	private float zoom;

	//动画执行时长
	private long duration;

	//按键的延迟时间
	private int keyDownDelay;
	private long _keyDownDelay = 0L;

	public FocusArea( Context context, AttributeSet attrs ) {
		super( context, attrs );
		initResource( context, attrs, 0 );
	}

	/**
	 * 设置焦点监听
	 *
	 * @param focusChangedCallback 焦点监听回调接口 , 传入NULL则不进行回调
	 *
	 * @see OnFocusChangedCallback
	 */
	public void setFocusChangedCallback( @Nullable final OnFocusChangedCallback focusChangedCallback ) {
		this.focusChangedCallback = focusChangedCallback;
	}

	/**
	 * @param duration 动画时长
	 */
	public void setDuration( final long duration ) {
		this.focusAnimView.setDuration( duration );
	}

	/**
	 * @param interpolator 动画加速器 , 默认为 LinearOutSlowInInterpolator
	 *
	 * @see LinearOutSlowInInterpolator
	 */
	public void setInterpolator( final Interpolator interpolator ) {
		this.focusAnimView.setInterpolator( interpolator );
	}

	/**
	 * 切换显示焦点框
	 * <p>
	 * 此方法仅为隐藏焦点框 , 并不会影响ZOOM的效果显示
	 *
	 * @param show 是否显示
	 */
	public void switchFocusAnimView( final boolean show ) {
		this.focusAnimView.setVisibility( show ? VISIBLE : INVISIBLE );
	}

	/**
	 * 初始化资源
	 */
	private void initResource( Context context, @Nullable AttributeSet attrs, int defStyleAttr ) {
		this.getViewTreeObserver().addOnGlobalFocusChangeListener( FocusArea.this );

		final boolean allowChildOutOfBound;
		String[] ignoreTags;
		final int drawableRes;
		final long duration;
		final float zoom;

		if ( attrs != null ) {
			final TypedArray typedArray = context.obtainStyledAttributes( attrs, R.styleable.FocusArea, defStyleAttr, 0 );
			try {
				ignoreTags = getResources().getStringArray( typedArray.getResourceId( R.styleable.FocusArea_FA_ignoreTagsArray, 0 ) );
			} catch ( Resources.NotFoundException e ) {
				ignoreTags = null;
			}
			drawableRes = typedArray.getResourceId( R.styleable.FocusArea_FA_focusViewDrawableRes, 0 );
			allowChildOutOfBound = typedArray.getBoolean( R.styleable.FocusArea_FA_allowChildOutOfBound, false );
			duration = typedArray.getInt( R.styleable.FocusArea_FA_focusMoveDuration, 300 );
			zoom = typedArray.getFloat( R.styleable.FocusArea_FA_focusZoom, 1.0f );
			keyDownDelay = typedArray.getInt( R.styleable.FocusArea_FA_keyDownDelay, 0 );

			typedArray.recycle();
		} else {
			drawableRes = R.drawable.default_focus_img;
			allowChildOutOfBound = false;
			ignoreTags = null;
			duration = 300;
			zoom = 1.0f;
		}

		//更新本地相关的属性
		this.zoom = zoom;
		this.duration = duration;
		this.ignoreTags = ignoreTags;

		//设置是否允许Child越界
		setClipChildren( !allowChildOutOfBound );

		this.focusAnimView = new FocusAnimView( getContext(), drawableRes, duration, new LinearOutSlowInInterpolator() );
		addView( this.focusAnimView );
	}

	@Override
	public void onGlobalFocusChanged( View oldFocus, View newFocus ) {

		//判断是否需要忽略焦点动画
		boolean isIgnoreNewFocus = false;
		boolean isIgnoreOldFocus = false;

		if ( this.ignoreTags == null ) {
			isIgnoreNewFocus = false;
			isIgnoreOldFocus = false;
		} else {
			for ( final String tag : this.ignoreTags ) {
				if ( oldFocus != null && oldFocus.getTag() != null ) {
					isIgnoreOldFocus = oldFocus.getTag().toString().equals( tag );
				}
				if ( newFocus != null && newFocus.getTag() != null ) {
					isIgnoreNewFocus = newFocus.getTag().toString().equals( tag );
				}
			}
		}

		//创建缩放动画  这里不能创建统一的动画对象 , 不然会导致所有应用过动画的View都出现对应的效果
		final ScaleAnimation inAnim, outAnim;
		if ( zoom != 1.0f ) {
			inAnim = new ScaleAnimation( 1.0f, 1.0f * zoom, 1.0f, 1.0f * zoom, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f );
			outAnim = new ScaleAnimation( 1.0f * zoom, 1.0f, 1.0f * zoom, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f );
			inAnim.setDuration( this.duration );
			outAnim.setDuration( this.duration );
			inAnim.setFillAfter( true );
			outAnim.setFillAfter( true );
		} else {
			inAnim = null;
			outAnim = null;
		}

		if ( isIgnoreNewFocus ) {
			//如果需要隐藏焦点动画则直接回调接口
			this.focusAnimView.setVisibility( INVISIBLE );
			if ( outAnim != null && oldFocus != null ) {
				oldFocus.startAnimation( outAnim );
			}
			if ( this.focusChangedCallback != null ) {
				this.focusChangedCallback.onFocusChanged( oldFocus, newFocus );
			}
			return;
		} else {
			this.focusAnimView.setVisibility( VISIBLE );
		}

		//执行缩放的动画效果
		if ( inAnim != null ) {
			//获取父容器的类型
			final String parentClass = newFocus.getParent() != null ? newFocus.getParent().getClass().getSimpleName() : "";
			if ( !parentClass.equals( LinearLayout.class.getSimpleName() ) ) {
				//LinearLayout不能使用 bringToFront 否则会导致顺序错乱
				newFocus.bringToFront();
			}
			if ( oldFocus != null && !isIgnoreOldFocus ) {
				//如果旧的焦点View不为NULL , 同时上一个焦点View不是被忽略的 , 则需要执行动画效果
				oldFocus.startAnimation( outAnim );
			}
			newFocus.startAnimation( inAnim );
		}

		//执行光标的动画效果
		this.focusAnimView.updateLocation( getViewRectF( newFocus, this.zoom ), oldFocus != null );

		//进行接口的回调
		if ( this.focusChangedCallback != null ) {
			this.focusChangedCallback.onFocusChanged( oldFocus, newFocus );
		}

	}

	/**
	 * 返回指定View的坐标对象
	 * <p>
	 * <p>
	 * A------------
	 * |                |
	 * |                |
	 * |                |
	 * |                |
	 * ------------B
	 * <p>
	 * A:(left,top)  B:(right,bottom)
	 *
	 * @param view 需要生成坐标对象的View
	 * @param zoom 缩放尺寸
	 *
	 * @return 坐标对象View , 如果有无效的数据则返回NULL对象
	 */
	private @Nullable
	RectF getViewRectF( @Nullable View view, final float zoom ) {
		if ( view == null ) {
			return null;
		}

		//获取目标控件的所有间距尺寸 [0 top, 1 left]
		int[] result = getViewTopAndLeft( view );

		//计算View在根布局中对应的坐标
		float viewLeft = result[ 1 ];
		float viewTop = result[ 0 ];
		float viewRight = result[ 1 ] + view.getMeasuredWidth();
		float viewBottom = result[ 0 ] + view.getMeasuredHeight();


		/////////////////////////////////////////////下面处理焦点框越界的问题///////////////////////////////////////////////


		//获取子View的上一层父容器的边距数据
		int[] parentResult = getViewTopAndLeft( (View) view.getParent() );
		final float parentTop = parentResult[ 0 ];
		final float parentLeft = parentResult[ 1 ];
		final float parentBottom = parentResult[ 0 ] + ( (View) view.getParent() ).getMeasuredHeight();
		final float parentRight = parentResult[ 1 ] + ( (View) view.getParent() ).getMeasuredWidth();

		//获取本控件布局的边距数据
		final float rootLeft = getLeft();
		final float rootTop = getTop();
		final float rootRight = getRight();
		final float rootBottom = getBottom();

		//四边限制的长度（让光标移动范围在本控件之内，不能超出范围）
		final float limitLeft = rootLeft < parentLeft ? parentLeft : rootLeft;
		final float limitTop = rootTop < parentTop ? parentTop : rootTop;
		final float limitRight = rootRight > parentRight ? parentRight : rootRight;
		final float limitBottom = rootBottom > parentBottom ? parentBottom : rootBottom;

		//左
		if ( viewLeft < limitLeft ) {
			final float offset = limitLeft - viewLeft;
			viewLeft = limitLeft;
			viewRight = viewRight + offset;
		}

		//右
		if ( viewRight > limitRight ) {
			final float offset = -( limitRight - viewRight );
			viewRight = limitRight;
			viewLeft = viewLeft - offset;
		}

		//上
		if ( viewTop < limitTop ) {
			final float offset = limitTop - viewTop;
			viewTop = limitTop;
			viewBottom = viewBottom + offset;
		}

		//下
		if ( viewBottom > limitBottom ) {
			final float offset = -( limitBottom - viewBottom );
			viewBottom = limitBottom;
			viewTop = viewTop - offset;
		}

		//生成这个View的RectF
		RectF rectF = new RectF( viewLeft, viewTop, viewRight, viewBottom );

		//处理缩放效果
		if ( zoom != 1.0f ) {
			//获取长宽各增加了多少
			final float widthChanged = Math.abs( rectF.width() - ( rectF.width() * zoom ) ) / 2.0f;
			final float heightChanged = Math.abs( rectF.height() - ( rectF.height() * zoom ) ) / 2.0f;

			//生成处理后的对象
			rectF = new RectF( rectF.left - widthChanged, rectF.top - heightChanged, rectF.right + widthChanged, rectF.bottom + heightChanged );
		}

		return rectF;
	}

	/**
	 * 如果 keyDownDelay > 0 则进行延迟处理
	 */
	@Override
	public boolean dispatchKeyEvent( KeyEvent event ) {
		if ( event.getAction() != KeyEvent.ACTION_DOWN ) {
			return super.dispatchKeyEvent( event );
		}

		switch ( event.getKeyCode() ) {
			case KeyEvent.KEYCODE_ENTER:
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_DPAD_UP:
			case KeyEvent.KEYCODE_DPAD_UP_LEFT:
			case KeyEvent.KEYCODE_DPAD_UP_RIGHT:
			case KeyEvent.KEYCODE_DPAD_DOWN:
			case KeyEvent.KEYCODE_DPAD_DOWN_LEFT:
			case KeyEvent.KEYCODE_DPAD_DOWN_RIGHT:
			case KeyEvent.KEYCODE_DPAD_LEFT:
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				if ( this._keyDownDelay == 0L || System.currentTimeMillis() - this._keyDownDelay >= this.keyDownDelay ) {
					this._keyDownDelay = System.currentTimeMillis();
					return super.dispatchKeyEvent( event );
				} else {
					return true;
				}
		}
		return super.dispatchKeyEvent( event );
	}

	/**
	 * 获取间隔空间，包括Padding以及Margin数值
	 *
	 * @param view 要获取数据的View对象
	 *
	 * @return 间隔大小 [top, left]
	 */
	private int[] getViewTopAndLeft( View view ) {

		int sumTop = 0;
		int sumLeft = 0;

		while ( view != null && !( view instanceof FocusArea ) ) {
			sumTop += view.getTop();
			sumLeft += view.getLeft();

			view = (View) view.getParent();
		}
		return new int[]{ sumTop, sumLeft };
	}

	/**
	 * 对外的接口
	 */
	public interface OnFocusChangedCallback {

		/**
		 * 布局内的焦点发生变化的回调接口
		 *
		 * @param oldFocusView 上一个焦点View , 如果是首次焦点 , 则为NULL
		 * @param newFocusView 当前的焦点View
		 */
		void onFocusChanged( @Nullable final View oldFocusView, @NonNull final View newFocusView );

	}

}