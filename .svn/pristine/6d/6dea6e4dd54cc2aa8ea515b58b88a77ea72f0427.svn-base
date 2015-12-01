package net.afpro.jni.speex;

import net.afpro.jni.Loader;

/**
 * User: afpro
 * Date: 11-12-1
 * Time: 下午5:22
 */
public class Header {
    private final static String TO_STRING_FORMAT = "SpeexHeader(speex_version_id=%d, header_size=%d, rate=%d, wideband=%b, mode_bitstream_version=%d, nb_channels=%d, bitrate=%d, frame_size=%d, vbr=%d, frames_per_packet=%d";

    private int speex_version_id;
    private int header_size;
    private int rate;
    private boolean wideband;
    private int mode_bitstream_version;
    private int nb_channels;
    private int bitrate;
    private int frame_size;
    private int vbr = 0;
    private int frames_per_packet = 1;

    public native byte[] toPacket();
    public native void fromPacket(byte[] packet);
    public native void init(int rate, int channels, boolean wideband);

    public Header(int rate, int channels, boolean wideband) {
        init(rate, channels, wideband);
    }

    public Header(byte[] packet) {
        fromPacket(packet);
    }

    public Header() {
    }

    public int getSpeexVersionId() {
        return speex_version_id;
    }

    public void setSpeexVersionId(int speex_version_id) {
        this.speex_version_id = speex_version_id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public boolean isWideband() {
        return wideband;
    }

    public void setWideband(boolean wideband) {
        this.wideband = wideband;
    }

    public int getModeBitstreamVersion() {
        return mode_bitstream_version;
    }

    public void setModeVitstreamVersion(int mode_bitstream_version) {
        this.mode_bitstream_version = mode_bitstream_version;
    }

    public int getChannels() {
        return nb_channels;
    }

    public void setChannels(int nb_channels) {
        this.nb_channels = nb_channels;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public int getFrameSize() {
        return frame_size;
    }

    public void setFrameSize(int frame_size) {
        this.frame_size = frame_size;
    }

    public boolean getVbr() {
        return vbr!=0;
    }

    public void setVbr(boolean vbr) {
        this.vbr = vbr ? 1 : 0;
    }

    public int getFramesPerPacket() {
        return frames_per_packet;
    }

    public void setFramesPerPacket(int frames_per_packet) {
        this.frames_per_packet = frames_per_packet;
    }

    @Override
    public String toString() {
        return String.format("SpeexHeader(speex_version_id=%d, header_size=%d, rate=%d, wideband=%b, mode_bitstream_version=%d, nb_channels=%d, bitrate=%d, frame_size=%d, vbr=%d, frames_per_packet=%d",
                speex_version_id,
                header_size,
                rate,
                wideband,
                mode_bitstream_version,
                nb_channels,
                bitrate,
                frame_size,
                vbr,
                frames_per_packet);
    }

    static {
        Loader.init();
    }
}
