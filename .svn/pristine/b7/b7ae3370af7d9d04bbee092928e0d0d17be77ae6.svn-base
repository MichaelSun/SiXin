package net.afpro.jni.speex;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * User: admin
 * Date: 12/13/11
 * Time: 3:59 PM
 */
public class CommentHeader {
    private final static String ENCODING = "UTF-8"; // important: don't change this!
    private String vendor = "afpro";
    private List<String> names = new LinkedList<String>();
    private List<String> values = new LinkedList<String>();

    private static byte[] string2bytes(String str) {
        try {
            return str.getBytes(ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private static String bytes2string(byte[] data, int start, int length) {
        try {
            return new String(data, start, length, ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "=";
        }
    }

    private static void appendInt(List<Byte> bytes, int n) {
        bytes.add((byte) (n & 0xFF));
        bytes.add((byte) ((n >> 8) & 0xFF));
        bytes.add((byte) ((n >> 16) & 0xFF));
        bytes.add((byte) ((n >> 24) & 0xFF));
    }

    private static void appendBytes(List<Byte> bytes, byte[] array) {
        for (byte b : array) {
            bytes.add(b);
        }
    }

    private static void appendString(List<Byte> bytes, String str) {
        byte[] tmp = string2bytes(str);
        appendInt(bytes, tmp.length);
        appendBytes(bytes, tmp);
    }

    private static int getInt(byte[] data, int start) {
        int a = data[start++];
        int b = data[start++];
        int c = data[start++];
        int d = data[start];
        return (a) | (b<<8) | (c<<16) | (d<<24);
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void reset() {
        names.clear();
        values.clear();
    }

    public void addComment(String name, String value) {
        names.add(name);
        values.add(value);
    }

    public int getCommentCount() {
        return names.size();
    }

    /**
     * @return String[]{name, value}
     */
    public String[] getComment(int index) {
        return new String[]{
                names.get(index),
                values.get(index)
        };
    }

    public byte[] toPacket() {
        List<Byte> tmp = new ArrayList<Byte>();
        appendString(tmp, vendor);
        appendInt(tmp, names.size());
        for (int i = 0; i < names.size(); i++) {
            appendString(tmp, String.format("%s=%s", names.get(i), values.get(i)));
        }
        tmp.add((byte) 0xFF);

        byte[] bytes = new byte[tmp.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = tmp.get(i);
        }
        return bytes;
    }

    public void fromPacket(byte[] packet) {
        int pos = 0;
        names.clear();
        values.clear();

        int vendorLength = getInt(packet, pos);
        pos += 4;

        vendor = bytes2string(packet, pos, vendorLength);
        pos += vendorLength;

        int count = getInt(packet, pos);
        pos += 4;

        for (int i = 0; i < count; i++) {
            int strLength = getInt(packet, pos);
            pos += 4;

            String comment = bytes2string(packet, pos, strLength);
            pos += strLength;

            int splitPos = comment.indexOf("=");
            if (splitPos >= 0) {
                names.add(comment.substring(0, splitPos));
                values.add(comment.substring(splitPos + 1));
            }
        }
    }
}
