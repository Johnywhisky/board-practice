package net.scit.spring7.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http.authorizeHttpRequests((auth) -> auth.requestMatchers(
				"/",
				"/user/regist",
				"/user/idDupCheck",
				"/user/login",
				"/board/list",
				"/board/detail/{seqNo}",
				"/board/download",
				"/reply/list",
				"/css/**",
				"/images/**",
				"/js/**"
			).permitAll()
			.requestMatchers("/admin").hasRole("ADMIN")
			.requestMatchers("/user/pwdcheck").hasRole("USER")
			.requestMatchers("/user/mypage").hasRole("USER")
			.anyRequest().authenticated()
		);
		http.formLogin((auth)-> auth
			.loginPage("/user/login")
			.loginProcessingUrl("/user/login")
			.usernameParameter("userId")
			.passwordParameter("userPwd")
			.defaultSuccessUrl("/")
			.failureUrl("/user/login?error=true")
			.permitAll()
		);
		http.logout((auth) -> auth
			.logoutUrl("/user/logout")
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true)
		);
		http.csrf((auth) -> auth.disable());

		return http.build();
	}

	@Bean
	BCryptPasswordEncoder pwdEncoder() {

		return new BCryptPasswordEncoder();
	}
}
