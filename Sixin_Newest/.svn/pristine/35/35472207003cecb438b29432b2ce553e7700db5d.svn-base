package com.renren.mobile.chat.ui.photo;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;

import com.renren.mobile.chat.R;

/**
 * Created on December 23,2010
 * @author kuangxiaoyue
 * @version 1.0
 */
public class PhotoGalleryNew extends Gallery {

    private GestureDetector gestureScanner;
    private RenrenPhotoImageView imageView;

	public PhotoGalleryNew(Context context) {
		super(context);
	}

	public PhotoGalleryNew(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PhotoGalleryNew(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
/***
 * @author zhenning.yang
 * 2011-9-1
 * 图片切换的处理
 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		int direction=(int) (e2.getX()-e1.getX());
		if(direction>0){
			return onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
		}else if(direction<0){
			return onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
		}
		return false;
	}

	 @Override
	    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
	        View layout = PhotoGalleryNew.this.getSelectedView();
	        if(layout == null) {
	            return true;
	        }
	        View view = (ImageView) layout.findViewById(R.id.imageview_photo_new);
	        if (view instanceof RenrenPhotoImageView) {
	            imageView = (RenrenPhotoImageView) view;
	            float v[] = new float[9];
	            Matrix m = imageView.getImageMatrix();
	            m.getValues(v);
	            float left, right;
	            float top,bottom;
	            // 图片的实时宽，高
	            float width, height;
	            width = imageView.getScale() * imageView.getImageWidth();
	            height = imageView.getScale() * imageView.getImageHeight();
	            // 一下逻辑为移动图片和滑动gallery换屏的逻辑。如果没对整个框架了解的非常清晰，改动以下的代码前请三思！！！！！！
	            if ((int) width <= PhotoNew.screenWidth && (int) height <= PhotoNew.screenHeight)// 如果图片当前大小<屏幕大小，直接处理滑屏事件
	            {
	                return super.onScroll(e1, e2, distanceX, distanceY);
	            } else {
	                left = v[Matrix.MTRANS_X];
	                top = v[Matrix.MTRANS_Y];
	                bottom = top +height;
	                right = left + width;
	                Rect r = new Rect();
	                imageView.getGlobalVisibleRect(r);
	                float dY = Math.abs(distanceY);
	                float dX = Math.abs(distanceX);
	                if((int) width <= PhotoNew.screenWidth) {
	                    if(dY > dX) {

	                        if(distanceY > 0 && bottom <= PhotoNew.screenHeight) {
	                        } else if(distanceY < 0 && top >= 0) {
	                        } else {
	                            imageView.postTranslate(0, -distanceY);
	                        }
	                        return true;
	                    } else {
	                        return super.onScroll(e1, e2, distanceX, distanceY);
	                    }
	                }
	                if(distanceX > 0)// 向左滑动
	                {
	                    if (right <= PhotoNew.screenWidth) {
	                        return super.onScroll(e1, e2, distanceX, distanceY);
	                    } else if(r.left > 0) {
	                        return super.onScroll(e1, e2, distanceX, distanceY);
	                    } else {
	                        if(height < PhotoNew.screenHeight) {
	                            imageView.postTranslate(-distanceX, 0);
	                        } else if(distanceY > 0 && bottom <= PhotoNew.screenHeight) {
	                            imageView.postTranslate(-distanceX, 0);
	                        } else if(distanceY < 0 && top >= 0) {
	                            imageView.postTranslate(-distanceX, 0);
	                        }else {
	                            imageView.postTranslate(-distanceX, -distanceY);
	                        }
	                        return true;
	                    }
	                } else if (distanceX < 0)// 向右滑动
	                {
	                    if (left >= 0) {
	                        return super.onScroll(e1, e2, distanceX, distanceY);
	                    } else if(r.right < PhotoNew.screenWidth) {
	                        return super.onScroll(e1, e2, distanceX, distanceY);
	                    }else {
	                        if(height < PhotoNew.screenHeight) {
	                            imageView.postTranslate(-distanceX, 0);
	                        } else if(distanceY > 0 && bottom <= PhotoNew.screenHeight) {
	                            imageView.postTranslate(-distanceX, 0);
	                        } else if(distanceY < 0 && top >= 0) {
	                            imageView.postTranslate(-distanceX, 0);
	                        }else {
	                            imageView.postTranslate(-distanceX, -distanceY);
	                        }
	                        return true;
	                    }
	                }

	            }

	        } else {
	            return super.onScroll(e1, e2, distanceX, distanceY);
	        }
	        return false;
	    }

	
	@Override
	protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
	}

    private class MySimpleGesture extends GestureDetector.SimpleOnGestureListener {
        // 按两下的第二下Touch down时触发
        public boolean onDoubleTap(MotionEvent e) {
        	
            View layout = PhotoGalleryNew.this.getSelectedView();
            View view = (ImageView) layout.findViewById(R.id.imageview_photo_new);
            if (view instanceof RenrenPhotoImageView) {
                imageView = (RenrenPhotoImageView) view;
                if (imageView.getScale() > imageView.getScaleRate()) {
                    imageView.zoomTo(imageView.getScaleRate(), PhotoNew.screenWidth / 2, PhotoNew.screenHeight / 2, 200f);
                } else {
                    imageView.zoomTo(1.0f, PhotoNew.screenWidth / 2, PhotoNew.screenHeight / 2, 200f);
                }

            } else {

            }
            return true;
        }
    }
}
