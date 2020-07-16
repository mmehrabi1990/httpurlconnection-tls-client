package main.java.mehrabi.httpurlconnectiontlsclient;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

import static java.net.HttpURLConnection.HTTP_OK;

public class Main {
    static {
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                (hostname, sslSession) -> hostname.equals("localhost"));
    }

    public static void main(String[] args) throws IOException {
        requestHttps();
    }

    private static void requestHttps() throws IOException {
        URL url = new URL("https://localhost:443/IBANSpecEnquiry/IBANSpec");
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            Objects.requireNonNull(sslContext).init(null, null, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setSSLSocketFactory(new SSLSocketFactoryGenerator().generate("C:\\\\Users\\\\m_mehrabi\\\\Desktop\\\\EAP6.4.0\\\\standalone\\\\configuration\\\\client.truststore",
                "secret",
                "C:\\\\Users\\\\m_mehrabi\\\\Desktop\\\\EAP6.4.0\\\\standalone\\\\configuration\\\\client.truststore",
                "secret"));
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/xml");

        OutputStream os = conn.getOutputStream();
//        os.write(Objects.requireNonNull(input).getBytes());
        os.flush();

        if (conn.getResponseCode() != HTTP_OK) {
            throw new RuntimeException("Failed : HTTPS error code : "
                    + conn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }
        conn.disconnect();
    }

}


