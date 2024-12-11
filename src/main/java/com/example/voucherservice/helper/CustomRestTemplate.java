package com.example.voucherservice.helper;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class CustomRestTemplate {

    public static RestTemplate createRestTemplate() throws NoSuchAlgorithmException, KeyManagementException {
        // Create a trust manager that trusts all certificates
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        // Configure the SSL context to use the trust manager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        // Create a custom SSL socket factory with the configured SSL context
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext,
                NoopHostnameVerifier.INSTANCE);

        // Create an Apache HttpClient with the custom SSL socket factory
        HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslSocketFactory).build();

        // Create an HttpComponentsClientHttpRequestFactory with the Apache HttpClient
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        // Create the RestTemplate using the custom request factory
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        return restTemplate;
    }

	/*
	 * public static void main(String[] args) throws NoSuchAlgorithmException,
	 * KeyManagementException { RestTemplate restTemplate = createRestTemplate();
	 * 
	 * // Use the custom RestTemplate to make requests //
	 * restTemplate.getForObject(...) or restTemplate.exchange(...) }
	 */
}