package com.common.emotion.view;

import com.core.util.CommonUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 横向导航点
 * @author zhenning.yang
 * @version 1.0不是十分完善，没有扩展性。
 */
public class VTabIconImageView extends ImageView implements IDoubleStateIndicator {

	
	private Bitmap check_bitmap ;
	private Bitmap uncheck_bitmap ;
	private RankOnIconCheckListener mRankCheckListener = null;
	private int check_rid ;
	private int uncheck_rid ;
	
	public VTabIconImageView(Context context) {
		super(context);
	}
	public VTabIconImageView(Context context,Bitmap check_bitmap,Bitmap uncheck_bitmap,RankOnIconCheckListener listener){
		this(context);
		this.check_bitmap = check_bitmap;
		this.uncheck_bitmap = uncheck_bitmap;
		this.mRankCheckListener = listener;
		this.unCheck();
	}
	public VTabIconImageView(Context context,int check_bitmap,int uncheck_bitmap){
		this(context);
		check_rid = check_bitmap;
		uncheck_rid = uncheck_bitmap;
		this.unCheck();
	}
	@Override
	public void check(int position) {
		this.setImageBitmap(check_bitmap);
		if(this.mRankCheckListener!=null){
			 this.mRankCheckListener.rankCheck(position);
		}
	}

	@Override
	public void unCheck() {
		this.setImageBitmap(uncheck_bitmap);
	}

}
