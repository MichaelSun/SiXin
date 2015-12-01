package plugin.ui;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.common.R;
import com.common.manager.DataManager.ImageListener;

public class PluginIconListener implements ImageListener{


		private ImageView icon;
		public static int ICON_SMALL = 1;
		public static int ICON_BIG =2;
		private int iconType;
		public PluginIconListener(ImageView view , int type){
			icon = view;
			iconType = type;
		}
		@Override
		public void onGetData(byte[] data) {
		}

		@Override
		public void onGetError() {
			setDefault();
		}

		@Override
		public void onGetBitmap(Bitmap bitmap) {
			if(icon != null){
				if(bitmap != null){
					icon.setImageBitmap(bitmap);
					return;
				}
				setDefault();
			}else{
				if(bitmap!=null){
					bitmap.recycle();
				}
			}
		}
		
		private void setDefault(){
			if(iconType == ICON_SMALL){
				icon.setBackgroundResource(R.drawable.cy_renren_icon);
			}else if (iconType == ICON_BIG){
				icon.setBackgroundResource(R.drawable.attention_renren_icon);
			}
		}
		
}
