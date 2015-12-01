package com.renren.mobile.chat.activity;

import android.content.Context;
import android.opengl.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.common.utils.Bip;
import com.core.util.DeviceUtil;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.command.Command_Brush;
import net.afpro.gl.AnimationArgs;
import net.afpro.gl.NoLoopActivity;
import plugin.LocalPlugin;
import plugin.PluginInfo;

import java.io.IOException;

public class NativePlugin extends LocalPlugin {

	private int mIcon  ;
	private String mName;
	private PluginInfo mInfo;
	LocalPlugin mPlugin;
	public OnClickListener mClick;
	RenRenChatActivity mActivity;

	public static final String NATIVE_NAMESPACE = "$NATIVE_NAMESPACE$";
	public NativePlugin(int icon,String name,RenRenChatActivity acitivity){
		super(null,null);
		mIcon = icon;
		mName = name;
		mInfo =new NativePluginInfo();
		this.mActivity = acitivity;
		this.initEvent();
	}

	public NativePlugin(int icon,RenRenChatActivity acitivity,LocalPlugin plugin){
		super(null,null);
		mIcon = icon;
		mInfo =new NativePluginInfo();
		this.mPlugin = plugin;
		this.mActivity = acitivity;
		this.initEvent();
	}

	void initEvent(){
		mClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(DeviceUtil.getInstance().isMountSDCard()&&DeviceUtil.getInstance().isSDCardHasEnoughSpace()){
					start(mActivity, null);
				}else{
					if(!DeviceUtil.getInstance().isMountSDCard()){
						DeviceUtil.getInstance().toastNotMountSDCard();
					}
					if(!DeviceUtil.getInstance().isSDCardHasEnoughSpace()){
						DeviceUtil.getInstance().toastNotEnoughSpace();
					}
				}
			}
		};
	}

	public int getIconId(){
		return this.mIcon;
	}


	public void updateActivity(RenRenChatActivity activity){
		this.mActivity = activity;
	}

	public class NativePluginInfo extends PluginInfo{
		@Override
		public String icon() {
			return mIcon+"";
		}
		@Override
		public String namespace() {
			// TODO Auto-generated method stub
			return NATIVE_NAMESPACE;
		}
		@Override
		public String name() {
			// TODO Auto-generated method stub
			if(mName!=null){
				return mName;
			}
			if(mPlugin==null){
				return "null plugin";
			}
			if(mPlugin.getPluginInfo()==null){
				return "null info";
			}


			return mPlugin.getPluginInfo().name();
		}
	}
	@Override
	public void start(Context context, Bundle bundle) {
//		if(mPlugin!=null){
//			mPlugin.start(context, bundle);
//		}
		this.startPlugin(context, bundle);
	}
	protected  void startPlugin(Context context, Bundle bundle){}

	@Override
	public PluginInfo getPluginInfo() {
		return this.mInfo;
	}

	public static class NativePlugin_Brush extends NativePlugin {

		public NativePlugin_Brush(int icon, RenRenChatActivity acitivity,
				LocalPlugin plugin) {
			super(icon, acitivity, plugin);
		}
		@Override
		protected void startPlugin(Context context, Bundle bundle) {
			String id = mPlugin.getPluginInfo().get(PluginInfo.INFO_ID);
			Bundle mBundle = new Bundle();
			mBundle.putString(Command_Brush.BRUSH_PLUGIN_ID,id);
			mActivity.mBrush_Command.onStartCommand(mBundle);
		}

	}

	public static class NativePlugin_TakePhoto extends NativePlugin {
		public NativePlugin_TakePhoto(int icon, String name,RenRenChatActivity activity) {
			super(icon, name,activity);
		}
		@Override
		protected void startPlugin(Context context, Bundle bundle) {
			mActivity.mTakePhoto_Command.onStartCommand();
		}
	}


	public static class NativePlugin_SelectPhoto extends NativePlugin {
		public NativePlugin_SelectPhoto(int icon, String name,RenRenChatActivity activity) {
			super(icon, name,activity);
		}
		@Override
		protected void startPlugin(Context context, Bundle bundle) {
			mActivity.mLocalSelect_Command.onStartCommand();
		}
	}


	public static class NativePlugin_3D extends NativePlugin {
        public NativePlugin_3D(int icon, String name, RenRenChatActivity acitivity) {
            super(icon, name, acitivity);
        }

        public NativePlugin_3D(int icon, RenRenChatActivity acitivity, LocalPlugin plugin) {
            super(icon, acitivity, plugin);
        }

        @Override
        protected void startPlugin(Context context, Bundle bundle) {
            final AnimationArgs args = new AnimationArgs();
            args.modelAsset = "model";
            args.duration = 4000;
            Matrix.setLookAtM(args.modelview, 0, 0, -110.522f, 15.502f, 0, -41.853f, 15.502f, 0, 0, 1);
            NoLoopActivity.show(context, NoLoopActivity.class, args);
        }
    }

    public static class NativePlugin_Bip extends NativePlugin {
        int index = 0;

        public NativePlugin_Bip(int icon, String name, RenRenChatActivity acitivity) {
            super(icon, name, acitivity);
        }

        public NativePlugin_Bip(int icon, RenRenChatActivity acitivity, LocalPlugin plugin) {
            super(icon, acitivity, plugin);
        }

        @Override
        protected void startPlugin(Context context, Bundle bundle) {
            switch (index) {
                case 0:
                    Bip.bipContactsRefresh();
                    index = 1;
                    break;
                case 1:
                    Bip.bipContactsScrollDown();
                    index = 2;
                    break;
                case 2:
                    Bip.bipContactsScrollUp();
                    index = 3;
                    break;
                case 3:
                    Bip.bipIncomingPush();
                    index = 4;
                    break;
                case 4:
                    Bip.bipReceiveMessage();
                    index = 5;
                    break;
                default:
                    Bip.bipSendMessage();
                    index = 0;
                    break;
            }
        }
    }
}
