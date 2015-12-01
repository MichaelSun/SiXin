package com.common.manager;

/**
 * @author yuchao.zhang
 *         用来监听、控制VPN当前状态的管理工具类
 */
public class VpnManager {

    /**
     * VPN 正在连接
     */
    public static final String CONNECTING = "CONNECTING";

    /**
     * VPN 正在断开连接
     */
    public static final String DISCONNECTING = "DISCONNECTING";

    /**
     * VPN 中断操作
     */
    public static final String CANCELLED = "CANCELLED";

    /**
     * VPN 连接成功
     */
    public static final String CONNECTED = "CONNECTED";

    /**
     * VPN 断开连接成功
     */
    public static final String IDLE = "IDLE";

    /**
     * VPN 不可用
     */
    public static final String UNUSABLE = "UNUSABLE";

    /**
     * VPN 未知
     */
    public static final String UNKNOWN = "UNKNOWN";

    /**
     * VPN当前状态（上一次记录的状态）
     */
    private String mCurrentVpnState = "";

//    /**
//     * 为当前VPN状态赋值
//     *
//     * @param state
//     */
//    public void setCurrentVpnState(String state) {
//
//        mCurrentVpnState = state;
//    }
//
//    /**
//     * @return 返回VPN当前状态
//     */
//    public String getCurrentVpnState() {
//
//        return mCurrentVpnState;
//    }

    /**
     * VPN即将改变为的状态
     */
    private String mNewVpnState = "";

    /**
     * 为VPN即将改变为的状态赋值
     *
     * @param state
     */
    public void setNewVpnState(String state) {

        mNewVpnState = state;
    }

//    /**
//     * @return 返回即将改变为的VPN状态
//     */
//    public String getNewVpnState() {
//
//        return mNewVpnState;
//    }

    /**
     * @return vpn状态是否发生改变（1.连接成功 2.断开成功）
     */
    public boolean isVpnStateChanged() {

        if(!mNewVpnState.equals(CONNECTED) && !mNewVpnState.equals(IDLE)){
            return false;
        }
        return !mCurrentVpnState.equals(mNewVpnState);
    }

    /**
     * 检测VPN状态改变后，刷新VPN状态
     */
    public void refreshVpnState() {

        mCurrentVpnState = mNewVpnState;
    }

    private static VpnManager sInstance = new VpnManager();

    public static VpnManager getInstance() {

        return sInstance;
    }

}
