package view.plugin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.util.CommonUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.base.util.SystemUtil;

public class PluginItemView extends LinearLayout{

	
	LayoutInflater mInflater = null;
	ImageView mImageView =null;
	TextView mTextView = null;
	
	public PluginItemView(Context context) {
		super(context);
		this.init();
	}
	public PluginItemView(Context context,AttributeSet set) {
		super(context,set);
		this.init();
	}
	private void init(){
		this.setOrientation(VERTICAL);
		this.setGravity(Gravity.CENTER);
		mInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater.inflate(R.layout.plugin_item, this);
		mImageView =(ImageView)this.findViewById(R.id.plugin_image);
		mTextView = (TextView)this.findViewById(R.id.plugin_name);
	}
	public void setPluginSource(int imageId,String name){
		this.mImageView.setImageResource(imageId);
		this.mTextView.setText(name);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		SystemUtil.log("item", "item onTouchEvent ");
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			mImageView.setPressed(true);
		}else{
			mImageView.setPressed(false);
		}
//		mImageView.dispatchTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
	
	
}
