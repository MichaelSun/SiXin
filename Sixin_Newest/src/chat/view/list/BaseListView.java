package view.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;

import com.core.util.CommonUtil;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.util.BaseChatListAdapter.OnNotifyCallback;

/**
 * @update update by dingwei.chen 2012-8-28
 * */
public class BaseListView extends AbstractListView{
	
	public BaseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		SystemUtil.log("list", "setHeight = "+MeasureSpec.getSize(heightMeasureSpec));
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		this.setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
	};
	
	
	
}
