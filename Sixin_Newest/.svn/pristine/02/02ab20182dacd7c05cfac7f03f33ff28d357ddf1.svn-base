package com.renren.mobile.chat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.base.views.NotSynImageView;

/**
 * @author xiangchao.fan
 * @description self_define FramdeLayout on setting page
 */
public class GeneralSettingLayout extends RelativeLayout{
	
	/** background type */
	public static interface BackgroundType{
		/** default */
		public static final int MIDDLE_BACKGROUND = 0;
		
		public static final int FULL_BACKGROUND = 1;
		public static final int TOP_BACKGROUND = 2;
		public static final int BOTTOM_BACKGROUND = 3;
	}

	public class ViewHolder{
		@ViewMapping(ID=R.id.setting_icon)
		public ImageView mSettingIcon;
		
		@ViewMapping(ID=R.id.setting_name)
		public TextView mSettingName;
		
		@ViewMapping(ID=R.id.setting_description)
		public TextView mSettingDescription;
		
		@ViewMapping(ID=R.id.setting_content)
		public TextView mContent;
		
		@ViewMapping(ID=R.id.associate_content)
		public TextView mAssociateContent;
		
		@ViewMapping(ID=R.id.setting_head)
		public NotSynImageView mHead;
		
		@ViewMapping(ID=R.id.state_layout)
		public LinearLayout mStateLayout;
		
		@ViewMapping(ID=R.id.tv_state)
		public TextView mState;
		
		@ViewMapping(ID=R.id.setting_switch)
		public ImageView mSwitch;
		
		
		@ViewMapping(ID=R.id.next_page_icon)
		public ImageView mNextPageIcon;
		
		@ViewMapping(ID=R.id.setting_select)
		public ImageView mSettingSelect;
	}
	
	public ViewHolder mHolder;
	
	/** SettingLayout background type*/
	private int mBackgroundType;
	
	public GeneralSettingLayout(Context context) {
		super(context);
		
		initView();
	}
	
	public GeneralSettingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SettingLayoutBackgroundType);

		int indexCount = a.getIndexCount();
        for (int i = 0; i < indexCount; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
            case R.styleable.SettingLayoutBackgroundType_backgroundType:
                //获取backgroundType属性值
            	 mBackgroundType = a.getInt(attr, BackgroundType.FULL_BACKGROUND);
                break;
            }
        }
        a.recycle();

		
		initView();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	private void initView(){
		SystemService.sInflaterManager.inflate(R.layout.f_setting_item, this, true);
		this.setClickable(true);
		
		mHolder = new ViewHolder();
		ViewMapUtil.getUtil().viewMapping(mHolder, this);
	}
}
