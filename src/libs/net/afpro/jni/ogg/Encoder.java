package net.afpro.jni.ogg;

import net.afpro.jni.Loader;

public abstract class Encoder {
	protected abstract void page(byte[] header, byte[] body);
	
	private long native_ptr = 0;
	public native boolean beg();
	public native boolean end();
	
	/**
	 * 编码，一次一个packet哦，亲!
	 * @param data 数据, 最大1kb，否则后果自负哦亲
	 * @param serialno serialno
	 * @param end 是否是最后一个
	 * @return 是否成功
	 */
	public native boolean enc(byte[] data, long serialno, long granule, boolean end);
    public native void flush(long serialno);

	@Override
	protected
	void finalize() throws Throwable {
		end();
		super.finalize();
	}

    static {
        Loader.init();
    }
}