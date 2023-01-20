package br.com.lucapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

public class GoogleAuthConfig {
	
    public GoogleAuthorizationCodeFlowConfig googleConfig() {
		return new GoogleAuthorizationCodeFlowConfig();
	}
	
}