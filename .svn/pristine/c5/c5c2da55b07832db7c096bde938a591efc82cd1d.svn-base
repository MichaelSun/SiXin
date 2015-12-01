package net.afpro.jni.ogg;

import net.afpro.jni.Loader;

public abstract class Decoder {
	protected abstract void packet(boolean beg, boolean end, byte[] data, long granulepos, long packetno, long serialno);
	
	private long native_ptr = 0;
	public native boolean beg();
	public native boolean end();
	public native boolean dec(byte[] data);
    public boolean dec(byte[] data, int len) {
        byte[] buf = new byte[len];
        System.arraycopy(data, 0, buf, 0, len);
        return dec(buf);
    }
	
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