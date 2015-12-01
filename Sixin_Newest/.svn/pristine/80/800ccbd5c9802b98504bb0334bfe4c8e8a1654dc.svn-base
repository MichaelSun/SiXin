package com.renren.mobile.chat.base.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.core.util.CommonUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.dao.ImageDAO;

/**
 * @author dingwei.chen
 * */
public class NotSynImageView extends ImageView {

	
	public static final String GROUP_DEFUALT_HEAD_URL = "$GROUP";
	public static final String SINGLE_DEFUALT_HEAD_URL="$SINGLE";
	public static final String RECOMMEND_CONTACT = "$RECOMMEND";
	public static final String RENREN_CONTACT = "$RENREN";
	
	public static final int MAX_HEAD_PHOTO_NUM = 50;
	private static HeadPhotoImagePool POOL = new HeadPhotoImagePool();
	private static ImageDAO DAO = new ImageDAO();
	private static Bitmap DEFULAT_BITMAP = null;
	private static Bitmap DEFULAT_GROUP_BITMAP = null;
	private static Bitmap DEFULAT_RECOMMEND_BITMAP = null;
	private static Bitmap DEFULAT_RENREN_BITMAP = null;
	private FlowLayout mLayout = new FlowLayout();
	private static final Handler HANDLER = new Handler();
	public static final int MAX_HEAD_NUMBER = 4;
	public static final int GRAY = 0xffe5e5e5;
	private int mDefaultBackgroundColor = 0x0000000;
	public void setBackgroundColor(int color){
		this.mDefaultBackgroundColor = color;
	}
	
	
	/**
	 * @author dingwei.chen
	 * @说明 头像图片缓冲池
	 * */
	private static class HeadPhotoImagePool {
		Map<String, Bitmap> mPool = new HashMap<String, Bitmap>(
				MAX_HEAD_PHOTO_NUM);
		List<String> mLRUQueue = new ArrayList<String>(MAX_HEAD_PHOTO_NUM);

		/* 通过LRU进行淘汰* */
		private void sortLRUQueue(String url) {
			synchronized (mLRUQueue) {
				if (mLRUQueue.contains(url)) {
					mLRUQueue.remove(url);
					mLRUQueue.add(0, url);
				} else {// 删除队尾加入队头
					if (mLRUQueue.size() >= MAX_HEAD_PHOTO_NUM) {
						String tailUrl = mLRUQueue.remove(mLRUQueue.size() - 1);
						Bitmap bitamp = mPool.get(tailUrl);
						if (bitamp != null) {
							bitamp.recycle();
							bitamp = null;
							mPool.remove(tailUrl);
						}
					}
					mLRUQueue.add(0, url);// 加入队列头部
				}
			}
		}

		public void addToPool(String url, Bitmap bitmap) {
			synchronized (mLRUQueue) {
//				if (mLRUQueue.contains(url)) {
					this.mPool.put(url, bitmap);
//				}
			}
		}

		public Bitmap getBitmap(CanDownloadImageAble m, boolean isDownload) {
			String imageUrl = m.getUrl();
			Bitmap bitmap = mPool.get(imageUrl);
			this.sortLRUQueue(imageUrl);
			if (bitmap == null || bitmap.isRecycled()) {
				if (bitmap != null) {
					this.mPool.remove(imageUrl);
				}
				if (isDownload) {
					bitmap = DAO.downloadBitmap(m.getUrl(), m);
				}
				return DEFULAT_BITMAP;
			} else {
				return bitmap;
			}
		}

		public synchronized void recycleAll() {
			for (Bitmap bitmap : mPool.values()) {
				bitmap.recycle();
			}
			mPool.clear();
			mLRUQueue.clear();
		}
	}

	public static interface CanDownloadImageAble {
		public String getUrl();

		public void onImageRecive(String url, Bitmap bitmap);
	}

	public static interface ImageDownloadObserver {
		public void onUnRegistor();

		public void onUpdate(String url);

		public void addSubject(ImageDownloadSubject subject);
	}

	public static interface ImageDownloadSubject {
		public void addObserver(ImageDownloadObserver observer);

		public void unregistorObserver();
	}

	private List<ImageData> mHeadUrls = new LinkedList<ImageData>();
	private static final Paint PAINT = new Paint();
	public static Rect SRC_RECT = new Rect();

	public NotSynImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (DEFULAT_BITMAP == null) {
			DEFULAT_BITMAP = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.widget_default_head);
			DEFULAT_GROUP_BITMAP = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.default_group_head);
			DEFULAT_RECOMMEND_BITMAP = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.default_recommend);
			DEFULAT_RENREN_BITMAP = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.default_renren);
		}
		this.setImageBitmap(DEFULAT_BITMAP);
	}

	public NotSynImageView(Context context) {
		super(context);
	}

	public NotSynImageView addUrl(String url) {
		if (this.mHeadUrls.size() == MAX_HEAD_NUMBER) {
			return this;
		}
		if (url==null||url.equals(SINGLE_DEFUALT_HEAD_URL)) {
			this.setImageBitmap(DEFULAT_BITMAP);
			return this;
		}
		if(url.equals(GROUP_DEFUALT_HEAD_URL)){
			this.setImageBitmap(DEFULAT_GROUP_BITMAP);
			return this;
		}
		if(url.equals(RECOMMEND_CONTACT)){
			this.setImageBitmap(DEFULAT_RECOMMEND_BITMAP);
			return this;
		}
		if(url.equals(RENREN_CONTACT)){
			this.setImageBitmap(DEFULAT_RENREN_BITMAP);
			return this;
		}
		ImageData data = new ImageData(url);
		data.addObserver(mObserver);
		data.mBitmap = POOL.getBitmap(data, onDownloadEnable());
		this.setImageBitmap(DEFULAT_BITMAP);
		this.mHeadUrls.add(data);
		if (this.mHeadUrls.size() > 1) {
			this.setImageBitmap(null);
		}
		return this;
	}

	public static class ImageData implements CanDownloadImageAble,
			ImageDownloadSubject {

		public ImageData(String url) {
			this.mUrl = url;
			mLayout = new Rect();
		}

		public void setSize(int width, int height) {
			this.setLayout(0, 0, width, height);
		}

		public void setLayout(int locX, int locY, int width, int height) {
			this.mLayout.set(locX, locY, locX + width, locY + height);
		}

		String mUrl;
		Bitmap mBitmap;
		Rect mLayout;

		@Override
		public String getUrl() {
			return mUrl;
		}

		ImageDownloadObserver mObserver;

		@Override
		public void addObserver(ImageDownloadObserver observer) {
			mObserver = observer;
		}

		@Override
		public void unregistorObserver() {
			// TODO Auto-generated method stub
			mObserver = null;
		}

		@Override
		public void onImageRecive(final String url, Bitmap bitmap) {
//			SystemUtil.log("image", "onImageRecive "+mObserver);
			POOL.addToPool(url, bitmap);
			if (mObserver != null) {
				HANDLER.post(new Runnable() {
					public void run() {
						if (mObserver != null) {
							mObserver.onUpdate(url);
						}
					}
				});
			}
		}
	}

	private List<ImageDownloadSubject> mSubjects = new LinkedList<NotSynImageView.ImageDownloadSubject>();
	ImageDownloadObserver mObserver = new ObserverImpl();

	private class ObserverImpl implements ImageDownloadObserver {
		@Override
		public void onUnRegistor() {
			for (ImageDownloadSubject s : mSubjects) {
				s.unregistorObserver();
			}
			mSubjects.clear();
		}

		@Override
		public void onUpdate(String url) {
//			SystemUtil.log("image", "onUpdate ");
			for (ImageData data : mHeadUrls) {
				if (data.mUrl.equals(url)) {
					data.mBitmap = POOL.getBitmap(data, false);
//					SystemUtil.log("image", "post ");
					postInvalidate();
					break;
				}
			}
		}

		@Override
		public void addSubject(ImageDownloadSubject subject) {
			mSubjects.add(subject);
		}
	}

	public void clear() {
		mObserver.onUnRegistor();
		for (ImageData d : this.mHeadUrls) {
			d.unregistorObserver();
		}
		this.mHeadUrls.clear();
	}

	public static void clearPool() {
		POOL.recycleAll();
	}
	boolean mIsLayout = true;
	public void setIsLayout(boolean isLayout){
		mIsLayout = isLayout;
	}
	@Override
	public void draw(Canvas canvas) {
		
		canvas.drawColor(mDefaultBackgroundColor); // 绘制白色 先干掉了。
		super.draw(canvas);
		Rect dst = new Rect(this.getPaddingLeft(), this.getPaddingTop(),
				getWidth() - this.getPaddingLeft(), getHeight()
						- this.getPaddingTop());
		if(mIsLayout){
			mLayout.layout(dst);
		}
		int index = 0;
		if(this.mHeadUrls.size()>0){
			for (ImageData data : this.mHeadUrls) {
				if (data.mBitmap.isRecycled()) {
					data.mBitmap = POOL.getBitmap(data, true);
				}
				int width = data.mBitmap.getWidth();
				int height = data.mBitmap.getHeight();
				Rect src = new Rect(0, 0, width, height);
				if(width>height){
					src.left = (width-height)>>1;
					src.right = src.left+height;
				}else{
					src.top = (height-width)>>1;
					src.bottom = src.top+width;
				}
				canvas.drawBitmap(data.mBitmap, src, mIsLayout?data.mLayout:null, PAINT);
				index++;
				if (index == MAX_HEAD_NUMBER) {
					break;
				}
			}
		}
		
	};

	public static interface DownloadImageAbleListener {
		public boolean enable();// 开启下载
	}

	DownloadImageAbleListener mListener;

	public void setDownloadAbleListener(DownloadImageAbleListener listener) {
		mListener = listener;
	}

	public boolean onDownloadEnable() {
		if (mListener != null) {
			return mListener.enable();
		}
		return true;
	}

	
	private class BorderLayout{
		
	}
	
	
	
	private class FlowLayout {
		public void layout(Rect visibleWindow) {
			int width = visibleWindow.width();
			int height = visibleWindow.height();
			int locX = visibleWindow.left;
			int locY = visibleWindow.top;
			int padding = 1;
			if (mHeadUrls.size() == 1) {
				mHeadUrls.get(0).setLayout(locX, locY, width, height);
				return;
			}
			int half_width = (width >> 1);
			int half_height = (height >> 1);

			int m_width = half_width - (padding << 1);
			int m_height = half_height - (padding << 1);
			if (mHeadUrls.size() == 2) {
				mHeadUrls.get(0).setLayout(locX + padding,
						locY + ((height - m_height) >> 1), m_width, m_height);
				mHeadUrls.get(1).setLayout(locX + half_width + padding,
						locY + ((height - m_height) >> 1), m_width, m_height);
				return;
			}
			if (mHeadUrls.size() == 3) {
				mHeadUrls.get(0).setLayout(locX + ((width - m_width) >> 1),
						locY + padding, m_width, m_height);
				mHeadUrls.get(1).setLayout(locX + padding,
						locY + half_height + padding, m_width, m_height);
				mHeadUrls.get(2).setLayout(locX + half_width + padding,
						locY + half_height + padding, m_width, m_height);
				return;
			}
			if (mHeadUrls.size() >= MAX_HEAD_NUMBER) {
				mHeadUrls.get(0).setLayout(locX + padding, locY + padding,
						m_width, m_height);
				mHeadUrls.get(1).setLayout(locX + half_width + padding,
						locY + padding, m_width, m_height);
				mHeadUrls.get(2).setLayout(locX + padding,
						locY + half_height + padding, m_width, m_height);
				mHeadUrls.get(3).setLayout(locX + half_width + padding,
						locY + half_height + padding, m_width, m_height);
				return;
			}
		}
	}
}
