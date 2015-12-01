package com.renren.mobile.web;

import android.text.TextUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

/**
 * at 11:04 AM, 3/2/12
 *
 * @author afpro
 */
public class BaseHttpServer {
    private final static Pattern SPACE_SEP = Pattern.compile("\\s+");
    private final ServerSocket socket;
    private ResourceCallback resourceCallback = null;

    public BaseHttpServer(int port) throws IOException {
        socket = new ServerSocket(port);
        Utils.simpleLog("create http server (port=%d)", socket.getLocalPort());
        Thread thread = new Thread() {
            @Override
            public void run() {
                work();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    public void setResourceCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    public ServerSocket getSocket() {
        return socket;
    }

    private void work() {
        Utils.simpleLog("start worker");
        while (!socket.isClosed()) {
            try {
                Socket s = socket.accept();
                Utils.simpleLog("accept a socket: %s", s.toString());
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String path = null;
                    final ResourceCallback rc = resourceCallback;
                    if (rc != null) {
                        while (path == null) {
                            String line = reader.readLine();
                            if (TextUtils.isEmpty(line)) {
                                break;
                            }

                            String[] parts = SPACE_SEP.split(line, 3);
                            if(parts.length < 2) {
                                continue;
                            }

                            if("get".equalsIgnoreCase(parts[0])) {
                                Utils.simpleLog("GET %s", parts[1]);
                                path = parts[1];
                                break;
                            }

                            if ("post".equalsIgnoreCase(parts[0])) {
                                Utils.simpleLog("POST %s", parts[1]);
                                path = parts[1];
                                break;
                            }
                        }
                    }

                    WebResponse response;
                    URI uri = null;
                    try {
                        uri = new URI(path);
                    } catch (URISyntaxException ignored) {
                    }
                    if (path == null || uri == null || resourceCallback == null || (response = resourceCallback.resource(uri)) == null) {
                        writeHeader(s.getOutputStream(), 404, "Not Found", "text/html", -1);
                    } else {
                        OutputStream os = s.getOutputStream();
                        writeHeader(os, 200, "OK", response.mimeType, response.contentLength);
                        Utils.simpleEcho(response.data, os);
                        response.data.close();
                    }
                } finally {
                    s.close();
                }
            } catch (IOException e) {
                Utils.simpleLog("a worker got exception: %s", e.toString());
            }
        }
        Utils.simpleLog("worker quit");
    }

    public static void writeHeader(OutputStream os, int code, String msg, String contentType, long contentLength) throws IOException {
        final PrintStream ps = new PrintStream(os);
        ps.printf("HTTP/1.1 %d %s\r\n", code, msg);
        ps.printf("Cache-Control: no-cache\r\n");
        if (!TextUtils.isEmpty(contentType)) {
            ps.printf("Content-Type: %s\r\n", contentType);
        }
        if (contentLength > 0) {
            ps.printf("Content-Length: %d\r\n", contentLength);
        }
        ps.printf("\r\n");
        ps.flush();
    }

    @Override
    protected void finalize() throws Throwable {
        final ServerSocket socket = this.socket;
        try {
            socket.close();
            Utils.simpleLog("socket %s closed", socket.toString());
        } catch (IOException e) {
            Utils.logStackTrace(e);
        }
        super.finalize();
    }
}
