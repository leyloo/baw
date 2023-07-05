package com.exportPdf.exportDesignPdfFromBWA;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class ExportDesignPdfFromBwaApplication {
	private static final Logger logger = LoggerFactory.getLogger(ExportDesignPdfFromBwaApplication.class);
	
	
	@Value("${server.port}")
	private Integer port;

	@Bean(name = "threadPoolTaskExecutor")
	public Executor threadPoolTaskExecutor() {
		return new ThreadPoolTaskExecutor();
	}

	public static void main(String[] args) throws UnknownHostException {
		SpringApplication app = new SpringApplication(ExportDesignPdfFromBwaApplication.class);
		Environment env = app.run(args).getEnvironment();

		String protocol = "http";
		if (Objects.nonNull(env.getProperty("server.ssl.key-store")))
			protocol = "https";
		logger.info(
				"\n----------------------------------------------------------\n\t"
						+ "Application '{}' is running! Access URLs:\n\t" + "Local: \t\t{}://localhost:{}{}\n\t"
						+ "External: \t{}://{}:{}{}\n\t"
						+ ("Swagger Link: \t{}://{}:{}{}/swagger-ui/index.html?configUrl={}/v3/api-docs/swagger-config \n")
						+ "----------------------------------------------------------",
				env.getProperty("spring.application.name"), protocol, 
				env.getProperty("server.port"), env.getProperty("server.servlet.context-path"), protocol,
				InetAddress.getLocalHost().getHostAddress(), env.getProperty("server.port"), env.getProperty("server.servlet.context-path"), protocol, 
				InetAddress.getLocalHost().getHostAddress(), env.getProperty("server.port"), env.getProperty("server.servlet.context-path"), env.getProperty("server.servlet.context-path"));
	}
	

}
