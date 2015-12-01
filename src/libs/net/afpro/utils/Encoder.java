package net.afpro.utils;

import java.util.Arrays;

import net.afpro.jni.speex.Bits;
import net.afpro.jni.speex.CommentHeader;
import net.afpro.jni.speex.Header;
import net.afpro.jni.speex.Preprocessor;

/**
 * User: afpro
 * Date: 11-12-6
 * Time: 下午4:52
 */
public abstract class Encoder {
    protected abstract void page(byte[] header, byte[] body);
    protected abstract void onEncoderEnd();
    protected abstract void onEncoderBegin();
    private net.afpro.jni.ogg.Encoder oggEncoder;
    private net.afpro.jni.speex.Encoder spEncoder;
    private Preprocessor spPreprocessor = null;
    private Bits bits = new Bits();
    private final Header header;
    private final CommentHeader commentHeader = new CommentHeader();

    private int now = 0;
    private byte[] packet;
    private short[] buffer;

    private int frames = 0;
    private boolean onePacketPerPage = false;

    private long granule;

    public Encoder(int samplingRate, boolean wideband, boolean vbr, boolean preprocess, int framesPerPacket) {
        header = new Header(samplingRate, 1, wideband);
        header.setVbr(vbr);
        header.setFramesPerPacket(framesPerPacket);

        oggEncoder = new net.afpro.jni.ogg.Encoder() {
            @Override
            protected void page(byte[] header, byte[] body) {
                Encoder.this.page(header, body);
            }
        };

        spEncoder = new net.afpro.jni.speex.Encoder(wideband);
        spEncoder.setSamplingRate(samplingRate);
        spEncoder.setVBR(vbr);
        buffer = new short[spEncoder.getFrameSize()];

        if (preprocess) {
            spPreprocessor = new Preprocessor(buffer.length, samplingRate);
        }
    }

    private void frame() {
        if (spPreprocessor != null) {
            spPreprocessor.run(buffer);
        }

        spEncoder.encode(buffer, bits);
        granule += buffer.length;

        if (++frames < header.getFramesPerPacket()) {
            return;
        }

        frames = 0;
        oggEncoder.enc(packet, 0, granule, false);
        if (onePacketPerPage) {
            oggEncoder.flush(0);
        }

        bits.insertTerminator();
        packet = bits.get(bits.size(), true);
        bits.reset();
    }

    public void beg() {
        oggEncoder.beg();
        oggEncoder.enc(header.toPacket(), 0, 0, false);
        packet = commentHeader.toPacket();
        now = 0;
        granule = 0;
        frames = 0;
        bits.reset();
        this.onEncoderBegin();
    }

    public void enc(short[] data) {
        if (data == null) {
            return;
        }
        enc(data, 0, data.length);
    }

    public void enc(short[] data, final int offset, final int length) {
        if (data == null || length <= 0 || offset < 0 || offset + length > data.length) {
            return;
        }

        int pos = 0;
        while (pos < length) {
            int need = buffer.length - now;
            int left = length - pos;
            if (left < need) {
                System.arraycopy(data, pos + offset, buffer, now, left);
                break;
            } else {
                System.arraycopy(data, pos + offset, buffer, now, need);
                now = 0;
                pos += need;
                frame();
            }
        }
    }

    public void end() {
        Arrays.fill(buffer, now, buffer.length, (short) 0);
        if (now > 0) {
            Arrays.fill(buffer, now, buffer.length, (short) 0);
            frame();
        }
        oggEncoder.enc(packet, 0, granule, true);
        oggEncoder.end();
        this.onEncoderEnd();
    }

    public net.afpro.jni.ogg.Encoder getOggEncoder() {
        return oggEncoder;
    }

    public net.afpro.jni.speex.Encoder getSpeexEncoder() {
        return spEncoder;
    }

    public Preprocessor getSpeexPreprocessor() {
        return spPreprocessor;
    }

    public CommentHeader getCommentHeader() {
        return commentHeader;
    }

    public boolean isOnePacketPerPage() {
        return onePacketPerPage;
    }

    /**!*/
    public void setOnePacketPerPage(boolean onePacketPerPage) {
        this.onePacketPerPage = onePacketPerPage;
    }
}
