package com.renren.mobile.chat.base.model;

/**
 * 用来表示消息中的lbs信息
 * 
 * @author tian.wang
 * */
public class LbsDataModel {
	public static final long DEFAULT_LATLON = 255*1000000;
	
	private long mLat;
	private long mLon;
	private String mPoiName;
	private long mPoiLat;
	private long mPoiLon;

	public LbsDataModel(long lat, long lon, String poiName, long poilat, long poilon) {
		this.mLat = lat;
		this.mLon = lon;
		this.mPoiName = poiName;
		this.mPoiLat = poilat;
		this.mPoiLon = poilon;
	}

	public long getmLat() {
		return mLat;
	}

	public void setmLat(long mLat) {
		this.mLat = mLat;
	}

	public long getmLon() {
		return mLon;
	}

	public void setmLon(long mLon) {
		this.mLon = mLon;
	}

	public String getmPoiName() {
		return mPoiName;
	}

	public void setmPoiName(String mPoiName) {
		this.mPoiName = mPoiName;
	}

	public long getmPoiLat() {
		return mPoiLat;
	}

	public void setmPoiLat(long mPoiLat) {
		this.mPoiLat = mPoiLat;
	}

	public long getmPoiLon() {
		return mPoiLon;
	}

	public void setmPoiLon(long mPoiLon) {
		this.mPoiLon = mPoiLon;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("lat:").append(mLat).append(" lon:").append(mLon).append(" poiName:").append(mPoiName).append(" poiLat:").append(mPoiLat).append(" poiLon:").append(mPoiLon);
		return sb.toString();
	}
}
