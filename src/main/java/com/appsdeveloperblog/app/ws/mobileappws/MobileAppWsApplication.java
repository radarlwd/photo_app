package com.appsdeveloperblog.app.ws.mobileappws;

import com.appsdeveloperblog.app.ws.mobileappws.security.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@SpringBootApplication
public class MobileAppWsApplication extends SpringBootServletInitializer {

	// deploy to Apache Tomcat
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MobileAppWsApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(MobileAppWsApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SpringApplicationContext springApplicationContext() { return new SpringApplicationContext(); }

	@Bean(name="AppProperties")
	public AppProperties getAppProperties(){ return new AppProperties(); }

	// expose Authorization for client application
	@Bean
	CorsConfigurationSource corsConfigurationSource() {

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		source.registerCorsConfiguration("/**", config.applyPermitDefaultValues());
		//allow Authorization to be exposed
		config.setExposedHeaders(Arrays.asList("Authorization"));

		return source;
	}

}
