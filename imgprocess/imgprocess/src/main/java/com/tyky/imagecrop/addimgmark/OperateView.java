/*
 *          Copyright (C) 2016 jarlen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.tyky.imagecrop.addimgmark;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.ScreenUtils;

/**
 * @author jarlen
 */
public class OperateView extends View
{
	private List<ImageObject> imgLists = new ArrayList<ImageObject>();
	private Rect mCanvasLimits;  // 这个存储的是 图片的宽高矩形
	private Bitmap bgBmp;  // 这个表示的是 那张图片
	private Paint paint = new Paint();
//	private Context mContext;
	private boolean isMultiAdd;// true 代表可以添加多个水印图片（或文字），false 代表只可添加单个水印图片（或文字）
	private float picScale = 0.4f;
	/**
	 * 设置水印图片初始化大小
	 * @param picScale
	 */
	public void setPicScale(float picScale)
	{
		this.picScale = picScale;
	}
	/**
	 * 设置是否可以添加多个图片或者文字对象
	 * 
	 * @param isMultiAdd
	 *            true 代表可以添加多个水印图片（或文字），false 代表只可添加单个水印图片（或文字）
	 */
	public void setMultiAdd(boolean isMultiAdd)
	{
		this.isMultiAdd = isMultiAdd;
	}
	public OperateView(Context context, Bitmap resizeBmp)  // 第二个参数表示的是 位图
	{
		super(context);
//		this.mContext = context;
		bgBmp = resizeBmp;
		int width = bgBmp.getWidth();
		int height = bgBmp.getHeight();

		mCanvasLimits = new Rect(0, 0, width, height);
	}

	/**
	 * 将图片对象添加到View中
	 * 
	 * @param imgObj
	 *            图片对象
	 */
	public void addItem(ImageObject imgObj)  // 设置状态，添加到内容中，然后开始重新绘制
	{
		if (imgObj == null)
		{
			return;
		}
		if (!isMultiAdd && imgLists != null)  // 如果不是要添加多个，就会将列表清空
		{
			imgLists.clear();
		}
		imgObj.setSelected(true);
		if (!imgObj.isTextObject)
		{
			Log.d("MyTest"," 执行一次 imgObj.isTextObject ");
			imgObj.setScale(picScale);
		}
		ImageObject tempImgObj = null;
		for (int i = 0; i < imgLists.size(); i++)
		{
			tempImgObj = imgLists.get(i);
			tempImgObj.setSelected(false);  // 将其它的水印对象都设置为非选中状态
		}
		imgLists.add(imgObj);  // 将imgObj对象添加到imglists中
		invalidate();
	}
	/**
	 * 画出容器内所有的图像
	 */
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		int sc = canvas.save();  // 将canvas的状态保存到栈中
		canvas.clipRect(mCanvasLimits);  //只在这个矩形区域上绘制
//		canvas.drawBitmap(bgBmp, 0, 0, paint);  // 在view上绘制一张背景图片, 就是指的那个 从 相机拍摄过来的呃图片
		canvas.drawBitmap(bgBmp,mCanvasLimits,new Rect(0,0,getWidth(),getHeight()),paint);
		drawImages(canvas);  // 这里绘制的是水印的内容，已经绘制出去了就无法改变了吧
		canvas.restoreToCount(sc);  // 恢复之后就可以再重新画叉号了，而画布上也不会再有原来的图标内容
		for (ImageObject ad : imgLists)  //将imglists 里面的图像画出来
		{
			if (ad != null && ad.isSelected())
			{
				ad.drawIcon(canvas);  // 这个是用来绘制 x形和 旋转形图标的
			}
		}
	}

	public void save()
	{
		ImageObject io = getSelected();
		if (io != null)
		{
			io.setSelected(false);
		}
		invalidate();
	}

	/**
	 * 根据触控点重绘View
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getPointerCount() == 1)
		{
			handleSingleTouchManipulateEvent(event);
		} else
		{
			handleMultiTouchManipulateEvent(event);
		}
		invalidate();

		super.onTouchEvent(event);
		return true;
	}

	private boolean mMovedSinceDown = false;
	private boolean mResizeAndRotateSinceDown = false;
	private float mStartDistance = 0.0f;
	private float mStartScale = 0.0f;
	private float mStartRot = 0.0f;
	private float mPrevRot = 0.0f;
	static public final double ROTATION_STEP = 2.0;
	static public final double ZOOM_STEP = 0.01;
	static public final float CANVAS_SCALE_MIN = 0.25f;
	static public final float CANVAS_SCALE_MAX = 3.0f;
	private Point mPreviousPos = new Point(0, 0); // single touch events
	float diff;
	float rot;

	/**
	 * 多点触控操作
	 * 
	 * @param event
	 */
	private void handleMultiTouchManipulateEvent(MotionEvent event)
	{


		switch (event.getAction() & MotionEvent.ACTION_MASK)
		{
			case MotionEvent.ACTION_POINTER_UP :
				break;
			case MotionEvent.ACTION_POINTER_DOWN :
				float x1 = event.getX(0);
				float x2 = event.getX(1);
				float y1 = event.getY(0);
				float y2 = event.getY(1);
				float delX = (x2 - x1);
				float delY = (y2 - y1);
				diff = (float) Math.sqrt((delX * delX + delY * delY));
				mStartDistance = diff;
				// float q = (delX / delY);
				mPrevRot = (float) Math.toDegrees(Math.atan2(delX, delY));
				for (ImageObject io : imgLists)
				{
					if (io.isSelected())
					{
						mStartScale = io.getScale();
						mStartRot = io.getRotation();
						break;
					}
				}
				break;

			case MotionEvent.ACTION_MOVE :
				x1 = event.getX(0);
				x2 = event.getX(1);
				y1 = event.getY(0);
				y2 = event.getY(1);
				delX = (x2 - x1);
				delY = (y2 - y1);
				diff = (float) Math.sqrt((delX * delX + delY * delY));
				float scale = diff / mStartDistance;
				float newscale = mStartScale * scale;
				rot = (float) Math.toDegrees(Math.atan2(delX, delY));
				float rotdiff = mPrevRot - rot;
				for (ImageObject io : imgLists)
				{
					if (io.isSelected() && newscale < 10.0f && newscale > 0.1f)
					{
						float newrot = Math.round((mStartRot + rotdiff) / 1.0f);
						if (Math.abs((newscale - io.getScale()) * ROTATION_STEP) > Math
								.abs(newrot - io.getRotation()))
						{
							io.setScale(newscale);
						} else
						{
							io.setRotation(newrot % 360);
						}
						break;
					}
				}

				break;
		}
	}
	/**
	 * 获取选中的对象ImageObject
	 * 
	 * @return
	 */
	private ImageObject getSelected()
	{
		for (ImageObject ibj : imgLists)
		{
			if (ibj.isSelected())
			{
				return ibj;
			}
		}
		return null;
	}

	private long selectTime = 0;
	/**
	 * 单点触控操作
	 * 
	 * @param event
	 */
	private void handleSingleTouchManipulateEvent(MotionEvent event)
	{
		long currentTime = 0;
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN :

				mMovedSinceDown = false;
				mResizeAndRotateSinceDown = false;
				int selectedId = -1;

				for (int i = imgLists.size() - 1; i >= 0; --i)
				{
					ImageObject io = imgLists.get(i);  // 这个表示的是图片
					if (io.contains(event.getX(), event.getY())
							|| io.pointOnCorner(event.getX(), event.getY(),
									OperateConstants.RIGHTBOTTOM)
							|| io.pointOnCorner(event.getX(), event.getY(),
									OperateConstants.LEFTTOP))
					{
						io.setSelected(true);
						imgLists.remove(i);
						imgLists.add(io);  // 交换一个位置，这样就把它放到了最上层
						selectedId = imgLists.size() - 1;  // 选择的就是最上层的那个
						currentTime = System.currentTimeMillis();
						if (currentTime - selectTime < 300)
						{
							if (myListener != null)
							{
								if (getSelected().isTextObject())
								{
									myListener
											.onClick((TextObject) getSelected());
								}
							}
						}
						selectTime = currentTime;
						break;
					}
				}
				if (selectedId < 0)
				{
					for (int i = imgLists.size() - 1; i >= 0; --i)
					{
						ImageObject io = imgLists.get(i);
						if (io.contains(event.getX(), event.getY())
								|| io.pointOnCorner(event.getX(), event.getY(),
										OperateConstants.RIGHTBOTTOM)
								|| io.pointOnCorner(event.getX(), event.getY(),
										OperateConstants.LEFTTOP))
						{
							io.setSelected(true);
							imgLists.remove(i);
							imgLists.add(io);
							selectedId = imgLists.size() - 1;
							break;
						}
					}
				}
				for (int i = 0; i < imgLists.size(); ++i)
				{
					ImageObject io = imgLists.get(i);
					if (i != selectedId)
					{
						io.setSelected(false);
					}
				}

				ImageObject io = getSelected();
				if (io != null)
				{
					if (io.pointOnCorner(event.getX(), event.getY(),
							OperateConstants.LEFTTOP))
					{
						imgLists.remove(io);
					} else if (io.pointOnCorner(event.getX(), event.getY(),
							OperateConstants.RIGHTBOTTOM))
					{
						mResizeAndRotateSinceDown = true;
						float x = event.getX();
						float y = event.getY();
						float delX = x - io.getPoint().x;
						float delY = y - io.getPoint().y;
						diff = (float) Math.sqrt((delX * delX + delY * delY));
						mStartDistance = diff;
						mPrevRot = (float) Math.toDegrees(Math
								.atan2(delX, delY));
						mStartScale = io.getScale();
						mStartRot = io.getRotation();
					} else if (io.contains(event.getX(), event.getY()))
					{
						mMovedSinceDown = true;
						mPreviousPos.x = (int) event.getX();
						mPreviousPos.y = (int) event.getY();
					}
				}
				break;

			case MotionEvent.ACTION_UP :

				mMovedSinceDown = false;
				mResizeAndRotateSinceDown = false;

				break;

			case MotionEvent.ACTION_MOVE :
				// Log.i("jarlen"," 移动了");
				// 移动
				if (mMovedSinceDown)
				{
					int curX = (int) event.getX();
					int curY = (int) event.getY();
					int diffX = curX - mPreviousPos.x;
					int diffY = curY - mPreviousPos.y;
					mPreviousPos.x = curX;
					mPreviousPos.y = curY;
					io = getSelected();
					Point p = io.getPosition();
					int x = p.x + diffX;
					int y = p.y + diffY;
					if (p.x + diffX >= mCanvasLimits.left
							&& p.x + diffX <= mCanvasLimits.right
							&& p.y + diffY >= mCanvasLimits.top
							&& p.y + diffY <= mCanvasLimits.bottom)
						io.moveBy((int) (diffX), (int) (diffY));
				}
				// 旋转和缩放
				if (mResizeAndRotateSinceDown)
				{
					io = getSelected();
					float x = event.getX();
					float y = event.getY();
					float delX = x - io.getPoint().x;
					float delY = y - io.getPoint().y;
					diff = (float) Math.sqrt((delX * delX + delY * delY));
					float scale = diff / mStartDistance;
					float newscale = mStartScale * scale;
					rot = (float) Math.toDegrees(Math.atan2(delX, delY));
					float rotdiff = mPrevRot - rot;
					if (newscale < 10.0f && newscale > 0.01f)
					{
						float newrot = Math.round((mStartRot + rotdiff) / 1.0f);
						if (Math.abs((newscale - io.getScale()) * ROTATION_STEP) > Math
								.abs(newrot - io.getRotation()))
						{
							io.setScale(newscale);
						} else
						{
							io.setRotation(newrot % 360);
						}
					}
				}
				break;
		}

		cancelLongPress();

	}
	/**
	 * 循环画图像
	 * 
	 * @param canvas
	 */
	private void drawImages(Canvas canvas)
	{
		for (ImageObject ad : imgLists)  // 初始的时候，imgLists 里面没有内容，所以这个不会执行
		{
			if (ad != null)
			{
				ad.draw(canvas);  // 这个就是画那个水印图片
			}
		}
	}

	/**
	 * 向外部提供双击监听事件（双击弹出自定义对话框编辑文字）
	 */
	MyListener myListener;

	public void setOnListener(MyListener myListener)
	{
		this.myListener = myListener;
	}

	public interface MyListener
	{
		public void onClick(TextObject tObject);
	}
}
