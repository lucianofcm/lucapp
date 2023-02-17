package br.com.lucapp;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan({"br.com.lucapp"})
@EntityScan("br.com.lucapp")
@EnableWebMvc
public class LucappApplication {

	public static void main(String[] args) {
		SpringApplication.run(LucappApplication.class, args);
	}
	
	 @Bean
	 public MultipartConfigElement multipartConfigElement() {
	     return new MultipartConfigElement("");
	 }

	 @Bean
	 public MultipartResolver multipartResolver() {
		 System.setProperty("org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH", "true");
	     org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
	     multipartResolver.setMaxUploadSize(1000000);
	     return multipartResolver;
	 }

}
