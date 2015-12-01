package com.common.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import com.core.util.AbstractApplication;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * at 下午3:45, 12-9-17
 *
 * @author afpro
 */
public class Bip {
    public static interface BipController {
        boolean allowIncomingPush();
        boolean allowContactsRefresh();
        boolean allowContactsScrollUp();
        boolean allowContactsScrollDown();
        boolean allowReceiveMessage();
        boolean allowSendMessage();
    }

    private static class DefaultBipController implements BipController {
        private final static int INCOMING = 0;
        private final static int CONTACTS_REFRESH = 1;
        private final static int CONTACTS_SCROLL_UP = 2;
        private final static int CONTACTS_SCROLL_DOWN = 3;
        private final static int SEND_MESSAGE = 4;
        private final static int RECEIVE_MESSAGE = 5;

        private final long[] timeLimits = {0, 0, 0, 0, 0, 0};
        private final long[] timeStamps = {0, 0, 0, 0, 0, 0};

        DefaultBipController() {
            // setup intervals
            timeLimits[INCOMING] = 2000;
            timeLimits[CONTACTS_REFRESH] = 500;
            timeLimits[CONTACTS_SCROLL_UP] = 500;
            timeLimits[CONTACTS_SCROLL_DOWN] = 500;
            timeLimits[SEND_MESSAGE] = 500;
            timeLimits[RECEIVE_MESSAGE] = 2000;
        }

        private boolean test(int idx) {
            final long cur = System.currentTimeMillis();
            if (cur - timeStamps[idx] > timeLimits[idx]) {
                timeStamps[idx] = cur;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean allowIncomingPush() {
            return test(INCOMING);
        }

        @Override
        public boolean allowContactsRefresh() {
            return test(CONTACTS_REFRESH);
        }

        @Override
        public boolean allowContactsScrollUp() {
            return test(CONTACTS_SCROLL_UP);
        }

        @Override
        public boolean allowContactsScrollDown() {
            return test(CONTACTS_SCROLL_DOWN);
        }

        @Override
        public boolean allowReceiveMessage() {
            return test(RECEIVE_MESSAGE);
        }

        @Override
        public boolean allowSendMessage() {
            return test(SEND_MESSAGE);
        }
    }

    private static BipController defaultBipController = null;
    public static BipController bipController = null;

    private static BipController getBipController() {
        final BipController bc = bipController;
        if (bc != null) {
            return bc;
        }

        if (defaultBipController == null) {
            synchronized (Bip.class) {
                if (defaultBipController == null) {
                    defaultBipController = new DefaultBipController();
                }
            }
        }
        return defaultBipController;
    }

    public static void bipIncomingPush() {
        if (getBipController().allowIncomingPush()) {
            play(AbstractApplication.getAppContext().getAssets(), "bips/IncomingPush.wav");
        }
    }

    public static void bipContactsRefresh() {
        if (getBipController().allowContactsRefresh()) {
            play(AbstractApplication.getAppContext().getAssets(), "bips/ContactsRefresh.wav");
        }
    }

    public static void bipContactsScrollUp() {
        if (getBipController().allowContactsScrollUp()) {
            play(AbstractApplication.getAppContext().getAssets(), "bips/ContactsScrollUp.wav");
        }
    }

    public static void bipContactsScrollDown() {
        if (getBipController().allowContactsScrollDown()) {
            play(AbstractApplication.getAppContext().getAssets(), "bips/ContactsScrollDown.wav");
        }
    }

    public static void bipReceiveMessage() {
        if (getBipController().allowReceiveMessage()) {
            play(AbstractApplication.getAppContext().getAssets(), "bips/ReceiveMessage.wav");
        }
    }

    public static void bipSendMessage() {
        if (getBipController().allowSendMessage()) {
            play(AbstractApplication.getAppContext().getAssets(), "bips/SendMessage.wav");
        }
    }

    // -------------- base ----------------- //
    private final static Set<MediaPlayer> mediaPlayers = new LinkedHashSet<MediaPlayer>();
    private static MediaPlayer newMediaPlayer() {
        final MediaPlayer mediaPlayer = new MediaPlayer();
        synchronized (mediaPlayers) {
            mediaPlayers.add(mediaPlayer);
        }
        return mediaPlayer;
    }

    private static void release(MediaPlayer mediaPlayer) {
        if(mediaPlayer == null) {
            return;
        }

        synchronized (mediaPlayers) {
            mediaPlayers.remove(mediaPlayer);
        }
        mediaPlayer.release();
    }

    private static MediaPlayer.OnCompletionListener releaseMediaPlayerOnComplete = null;

    private static MediaPlayer.OnCompletionListener getReleaseMediaPlayerOnComplete() {
        if (releaseMediaPlayerOnComplete == null) {
            synchronized (Bip.class) {
                if (releaseMediaPlayerOnComplete == null) {
                    releaseMediaPlayerOnComplete = new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            release(mediaPlayer);
                        }
                    };
                }
            }
        }
        return releaseMediaPlayerOnComplete;
    }

    private static interface MediaPlayerDataSourceSetter {
        void set(MediaPlayer mediaPlayer) throws IOException;
        void onIOException(IOException e);
    }

    private static abstract class MediaPlayerDataSourceSetterIgnoreIOException implements MediaPlayerDataSourceSetter {
        @Override
        public void onIOException(IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean play(MediaPlayerDataSourceSetter ds) {
        assert ds != null;

        final MediaPlayer mediaPlayer = newMediaPlayer();
        mediaPlayer.setOnCompletionListener(getReleaseMediaPlayerOnComplete());
        mediaPlayer.setLooping(false);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
        try {
            ds.set(mediaPlayer);
            mediaPlayer.prepare();
        } catch (IOException e) {
            ds.onIOException(e);
            return false;
        }
        mediaPlayer.start();
        return true;
    }

    public static boolean play(final AssetManager assetManager, final String path) {
        assert assetManager != null && path != null;

        return play(new MediaPlayerDataSourceSetterIgnoreIOException() {
            @Override
            public void set(MediaPlayer mediaPlayer) throws IOException {
                final AssetFileDescriptor afd = assetManager.openFd(path);
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
            }
        });
    }

    public static boolean play(final String path) {
        return play(new MediaPlayerDataSourceSetterIgnoreIOException() {
            @Override
            public void set(MediaPlayer mediaPlayer) throws IOException {
                mediaPlayer.setDataSource(path);
            }
        });
    }

    public static boolean play(final FileDescriptor fileDescriptor) {
        return play(new MediaPlayerDataSourceSetterIgnoreIOException() {
            @Override
            public void set(MediaPlayer mediaPlayer) throws IOException {
                mediaPlayer.setDataSource(fileDescriptor);
            }
        });
    }

    public static boolean play(final FileDescriptor fileDescriptor, final long offset, final long length) {
        return play(new MediaPlayerDataSourceSetterIgnoreIOException() {
            @Override
            public void set(MediaPlayer mediaPlayer) throws IOException {
                mediaPlayer.setDataSource(fileDescriptor, offset, length);
            }
        });
    }


    public static boolean play(final Context context, final Uri uri) {
        return play(new MediaPlayerDataSourceSetterIgnoreIOException() {
            @Override
            public void set(MediaPlayer mediaPlayer) throws IOException {
                mediaPlayer.setDataSource(context, uri);
            }
        });
    }

    public static boolean play(final Context context, final Uri uri, final Map<String, String> headers) {
        return play(new MediaPlayerDataSourceSetterIgnoreIOException() {
            @Override
            public void set(MediaPlayer mediaPlayer) throws IOException {
                mediaPlayer.setDataSource(context, uri, headers);
            }
        });
    }
}
