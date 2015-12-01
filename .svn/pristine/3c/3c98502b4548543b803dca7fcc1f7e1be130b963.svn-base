package net.afpro.utils;

import java.util.HashMap;
import java.util.Map;

import net.afpro.jni.speex.Bits;
import net.afpro.jni.speex.CommentHeader;
import net.afpro.jni.speex.Header;

/**
 * User: afpro
 * Date: 11-12-6
 * Time: 下午4:52
 */
public abstract class Decoder {
    protected abstract byte[] read();

    protected abstract boolean eof();

    protected abstract void frame(short[] frame);

    protected  void onStartDecode(){}//开始解码回调 Add by dingwei.chen
    protected  void onEndDecode(){}//结束解码回调 Add by dingwei.chen
    
    private final Header header = new Header();
    private final CommentHeader commentHeader = new CommentHeader();
    private long want = -1;
    private final Map<Long, byte[]> packets = new HashMap<Long, byte[]>();
    private net.afpro.jni.speex.Decoder spDecoder = null;
    private Bits bits = new Bits();
    private final net.afpro.jni.ogg.Decoder oggDecoder = new net.afpro.jni.ogg.Decoder() {
        @Override
        protected void packet(boolean beg, boolean end, byte[] data, long granulepos, long packetno, long serialno) {
            Decoder.this.packet(beg, end, data, granulepos, packetno, serialno);
        }
    };

    public net.afpro.jni.speex.Decoder getSpeexDecoder() {
        return spDecoder;
    }

    public net.afpro.jni.ogg.Decoder getOggDecoder() {
        return oggDecoder;
    }

    private void packet(boolean beg, boolean end, byte[] data, long granulepos, long packetno, long serialno) {
        if (packetno == 0) {
            header.fromPacket(data);
            spDecoder = new net.afpro.jni.speex.Decoder(header.isWideband()) {
                @Override
                protected void frame(short[] frame) {
                    Decoder.this.frame(frame);
                }
            };
            spDecoder.setSamplingRate(header.getRate());
            want = 2;
            return;
        } else if (packetno == 1) {
            commentHeader.fromPacket(data);
            return;
        }

        packets.put(packetno, data);
        if (spDecoder == null) {
            return;
        }

        for (byte[] encoded = packets.get(want); encoded != null; encoded = packets.get(++want)) {
            bits.reset();
            bits.set(encoded, true);
            spDecoder.decode(bits, header.getFramesPerPacket());
        }
    }

    public void run() {
        oggDecoder.beg();
        this.onStartDecode();//Add by dingwei.chen
        for (; !eof(); yield()) {
            byte[] data = read();
            if (data != null && data.length > 0) {
                oggDecoder.dec(data);
            }
        }
        this.onEndDecode();//Add by dingwei.chen
        oggDecoder.end();
    }

    public Header getHeader() {
        return header;
    }

    public CommentHeader getCommentHeader() {
        return commentHeader;
    }

    private void yield() {
        try {
            Thread.sleep(0);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
