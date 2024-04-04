package com.philips.itaap.utility.serivce;

import lombok.extern.slf4j.XSlf4j;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.SequenceInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

@XSlf4j
@SuppressWarnings("PMD")
public class NetworkUtils {

    public static String ping(String host) {
        return execute(new ProcessBuilder("ping", host));
    }

    /*public static String resolveIPs(String host, String dnsServer) throws UnknownHostException {
        if (dnsServer.equals("default") || dnsServer == null || dnsServer.isEmpty()) {
            InetAddress[] addresses = InetAddress.getAllByName(host);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < addresses.length; i++) {
                if (i != 0) {
                    sb.append("\n");
                }
                sb.append(addresses[i].getHostAddress());
            }
            return sb.toString();
        } else {
            dnsServer = "@" + dnsServer;
            try {
                return execute(new ProcessBuilder("dig", "+short", dnsServer, host));
            } catch (IOException e) {
                return e.getMessage();
            }
        }
    }*/

    public static String curl(String url) {
        //-i include protocol headers
        //-L follow redirects
        //-k insecure
        //-E cert status
//        curl --verbose http://localhost:8080/api-docs/test
        return execute(new ProcessBuilder("curl", "-k", "-i", "-L", url));
    }


    public static String testConnect(String host, String port) {
        long startTime = System.nanoTime();
        long totalTime = System.nanoTime();
        StringBuilder result = new StringBuilder();
        for (int x = 1; x <= 5; x++) {
            try (Socket socket = new Socket()) {

                startTime = System.nanoTime();
                socket.connect(new InetSocketAddress(host, Integer.parseInt(port)), 10000);
                socket.setSoTimeout(10000);
                if (socket.isConnected()) {
                    totalTime = System.nanoTime() - startTime;
                    socket.getInputStream();
                }
            } catch (UnknownHostException e) {
                return new StringBuilder("Could not resolve host ").append(host).toString();
            } catch (java.net.SocketTimeoutException e) {
                return new StringBuilder("Timeout while trying to connect to ").append(host).toString();
            } catch (IllegalArgumentException e) {
                return e.getMessage();
            } catch (Exception e) {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                log.info(String.valueOf(new PrintStream(b)));
                return b.toString();
            }
            result = result.append("Probe ").append(x).append(": Connection successful, RTT=").append(Long.toString(totalTime / 1000000)).append("ms\n");
        }
        return result.toString(); // + "socket test completed";
    }

    /*public static String traceRoute(String host) throws Exception {
        return execute(new ProcessBuilder("traceroute", "-w", "3", "-q", "1", "-m", "18", "-n", host));
    }

    public static String certest(String host, String port) throws Exception {
        return execute(new ProcessBuilder("openssl", "s_client", "-showcerts", "-servername", host, "-connect", host + ":" + port));
    }

    public static String cipherTest(String host, String port) throws Exception {
        String remoteEndpointSupportedCiphers = "List of supported ciphers:\n\n";
        String[] openSslAvailableCiphers = execute(new ProcessBuilder("openssl", "ciphers", "ALL:!eNULL")).split(":");

        for (String cipher : openSslAvailableCiphers) {
            if (execute(new ProcessBuilder("openssl", "s_client", "-cipher", cipher, "-servername", host, "-connect", host + ":" + port)).contains("BEGIN CERTIFICATE")) {
                remoteEndpointSupportedCiphers = remoteEndpointSupportedCiphers + cipher + ": YES\n";
            } else {
                remoteEndpointSupportedCiphers = remoteEndpointSupportedCiphers + cipher + ": NO\n";
            }
        }
        return remoteEndpointSupportedCiphers;
    }*/

    private static String execute(ProcessBuilder pb) {
        try {
            Process p = pb.start();
            OutputStream stdin = p.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
            writer.write("\n");
            writer.flush();
            writer.close();
            SequenceInputStream sis = new SequenceInputStream(p.getInputStream(), p.getErrorStream());
            java.util.Scanner s = new java.util.Scanner(sis).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (IOException e) {
            if (log.isInfoEnabled()) {
                log.info("Exception occured {}", e.getMessage());
            }
            return null;
        }
    }
}
