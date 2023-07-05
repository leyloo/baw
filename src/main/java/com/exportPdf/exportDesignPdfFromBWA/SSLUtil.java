package com.exportPdf.exportDesignPdfFromBWA;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Disables and enables certificate and host-name checking in
 * HttpsURLConnection, the default JVM implementation of the HTTPS/TLS protocol.
 * Has no effect on implementations such as Apache Http Client, Ok Http.
*/
public class SSLUtil {

    private static final HostnameVerifier jvmHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();

    private static final HostnameVerifier trivialHostnameVerifier = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession sslSession) {
            return true;
        }
    };

    private static final TrustManager[] UNQUESTIONING_TRUST_MANAGER = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    } };

    public static void turnOffSslChecking() throws NoSuchAlgorithmException, KeyManagementException {
        HttpsURLConnection.setDefaultHostnameVerifier(trivialHostnameVerifier);
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, UNQUESTIONING_TRUST_MANAGER, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    public static void turnOnSslChecking() throws KeyManagementException, NoSuchAlgorithmException {
        HttpsURLConnection.setDefaultHostnameVerifier(jvmHostnameVerifier);
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, null, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    private SSLUtil() {
        throw new UnsupportedOperationException("Do not instantiate libraries.");
    }
}
