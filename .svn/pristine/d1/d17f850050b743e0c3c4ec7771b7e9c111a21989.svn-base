package net.afpro.gl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * at 下午1:43, 12-9-10
 *
 * @author afpro
 */
public class AnimationArgs implements Parcelable {
    public long duration = 0;
    public String modelAsset = null;
    public String modelFile = null;
    public boolean modelFileGz = false;

    public float projectionFov = 45;
    public float projectionZNear = 0.1f;
    public float projectionZFar = 1000;

    public final float[] modelview = {
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
    };

    public final float[] direction = {0, 0, 1};
    public final float[] ambient = {0.5f, 0.5f, 0.5f, 1.0f};
    public final float[] diffuse = {0.7f, 0.7f, 0.7f, 1.0f};
    public final float[] specular = {0.7f, 0.7f, 0.7f, 1.0f};

    public static void copy(AnimationArgs src, AnimationArgs dst) {
        if(src == null || dst == null) {
            return;
        }

        dst.duration = src.duration;
        dst.modelAsset = src.modelAsset;
        dst.modelFile = src.modelFile;
        dst.modelFileGz = src.modelFileGz;

        dst.projectionFov = src.projectionFov;
        dst.projectionZNear = src.projectionZNear;
        dst.projectionZFar = src.projectionZFar;

        System.arraycopy(src.modelview, 0, dst.modelview, 0, 16);
        System.arraycopy(src.direction, 0, dst.direction, 0, 3);
        System.arraycopy(src.ambient, 0, dst.ambient, 0, 4);
        System.arraycopy(src.diffuse, 0, dst.diffuse, 0, 4);
        System.arraycopy(src.specular, 0, dst.specular, 0, 4);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dst, int flags) {
        dst.writeLong(duration);
        dst.writeString(modelAsset);
        dst.writeString(modelFile);
        dst.writeByte(modelFileGz ? (byte) 1 : (byte) 0);
        dst.writeFloat(projectionFov);
        dst.writeFloat(projectionZNear);
        dst.writeFloat(projectionZFar);
        dst.writeFloatArray(modelview);
        dst.writeFloatArray(direction);
        dst.writeFloatArray(ambient);
        dst.writeFloatArray(diffuse);
        dst.writeFloatArray(specular);
    }

    public static final Creator<AnimationArgs> CREATOR = new Creator<AnimationArgs>() {
        @Override
        public AnimationArgs createFromParcel(Parcel source) {
            final AnimationArgs args = new AnimationArgs();
            args.duration = source.readLong();
            args.modelAsset = source.readString();
            args.modelFile = source.readString();
            args.modelFileGz = source.readByte() != 0;
            args.projectionFov = source.readFloat();
            args.projectionZNear = source.readFloat();
            args.projectionZFar = source.readFloat();
            source.readFloatArray(args.modelview);
            source.readFloatArray(args.direction);
            source.readFloatArray(args.ambient);
            source.readFloatArray(args.diffuse);
            source.readFloatArray(args.specular);
            return args;
        }

        @Override
        public AnimationArgs[] newArray(int size) {
            return new AnimationArgs[size];
        }
    };
}
