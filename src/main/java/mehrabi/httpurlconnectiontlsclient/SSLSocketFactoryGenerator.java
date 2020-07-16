package main.java.mehrabi.httpurlconnectiontlsclient;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

public class SSLSocketFactoryGenerator {


    public SSLSocketFactory generate(String keystorePath,
                                     String keystorePassword,
                                     String truststorePath,
                                     String truststorePassword)
    {
        try {
            char[] keystorePasswordCharArray = keystorePassword.toCharArray();
            KeyStore keystore = loadKeystore(keystorePath, keystorePasswordCharArray);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keystore, keystorePasswordCharArray);

            char[] truststorePasswordCharArray = truststorePassword.toCharArray();
            KeyStore truststore = loadKeystore(truststorePath, truststorePasswordCharArray);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(truststore);

            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();

        } catch (Exception e) {
            String msg = "Error occurred in creating SSLSocketFactory ";
            throw new RuntimeException(msg, e);
        }
    }

    private KeyStore loadKeystore(String filePath, char[] password) {
        try (InputStream keyStoreData = new FileInputStream(filePath)) {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(keyStoreData, password);
            return keyStore;
        } catch (Exception e) {
            String msg = "Error occurred in loading key store ";
            throw new RuntimeException(msg, e);
        }
    }
}
