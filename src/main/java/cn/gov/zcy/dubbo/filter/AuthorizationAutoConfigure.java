package cn.gov.zcy.dubbo.filter;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luming
 */
@Configuration
@EnableConfigurationProperties(AuthorizationProperties.class)
public class AuthorizationAutoConfigure {

	@Bean
	public AuthorizationProperties authorizationProperties(AuthorizationProperties authorizationProperties) {
		AuthorizationFilter.setAuthorizationProperties(authorizationProperties);
		return authorizationProperties;
	}
}
