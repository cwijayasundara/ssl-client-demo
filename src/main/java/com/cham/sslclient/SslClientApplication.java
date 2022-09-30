package com.cham.sslclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@SpringBootApplication
@Slf4j
public class SslClientApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SslClientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		final String password = "Chaminda";
		SSLContext sslContext = SSLContextBuilder
				.create()
				.loadTrustMaterial(ResourceUtils.getFile("classpath:DemoSSLSpringApp.p12"), password.toCharArray())
				.build();
		CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(client);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		String url = "https://localhost:8443/api/hello";
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, String.class);
		log.info("Result = " + response.getBody());
	}
}
