package io.digisic.bank.config;

import javax.net.ssl.SSLContext;

// --- REPLACED org.apache.http with org.apache.hc ---
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
// --------------------------------------------------

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestClientCertificateConfig {
	
	private static String DIGITAL_CREDIT_KEYSTORE="classpath:keystore/digisic.p12";
	private static String DIGITAL_CREDIT_KEYSTORE_PASS="digisic";
	
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) throws Exception {

        // 1. Setup SSL Context
        SSLContext sslContext = SSLContextBuilder
                .create()
                .loadKeyMaterial(ResourceUtils.getFile(DIGITAL_CREDIT_KEYSTORE), 
                                 DIGITAL_CREDIT_KEYSTORE_PASS.toCharArray(), 
                                 DIGITAL_CREDIT_KEYSTORE_PASS.toCharArray())
                .loadTrustMaterial(ResourceUtils.getFile(DIGITAL_CREDIT_KEYSTORE), 
                                   DIGITAL_CREDIT_KEYSTORE_PASS.toCharArray())
                .build();

        // 2. Create the Socket Factory using the Version 5 Builder
        SSLConnectionSocketFactory csf = SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslContext)
                .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();

        // 3. Create a Connection Manager (This is the NEW step for Version 5)
        HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(csf)
                .build();

        // 4. Build the HttpClient by passing in the Connection Manager
        HttpClient client = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();

        // 5. Build the RestTemplate
        return builder
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(client))
                .build();
    }

}
