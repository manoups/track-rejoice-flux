package com.breece.app.web.support;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;

public final class TestPorts {
    private TestPorts() {
    }

    public static void waitForPort(String host, int port, Duration timeout) {
        long deadline = System.nanoTime() + timeout.toNanos();
        while (System.nanoTime() < deadline) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), 200);
                return;
            } catch (Exception ignored) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Interrupted while waiting for " + host + ":" + port);
                }
            }
        }
        throw new IllegalStateException("Timed out waiting for " + TestPorts.formatHostPort(host, port));
    }

    private static String formatHostPort(String host, int port) {
        return host + ":" + port;
    }
}
