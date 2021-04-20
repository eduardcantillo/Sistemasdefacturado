package com.bolsadeideas.spring.horario.datajpa.app;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import com.bolsadeideas.spring.horario.datajpa.app.auth.handler.LoginSuccesHandler;
import com.bolsadeideas.spring.horario.datajpa.app.service.JpaUserDetailsService;

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private LoginSuccesHandler succesHandler;
	

	@Autowired
	private JpaUserDetailsService user;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/","/css/**","/js/**","/api/clientes/**","/locale","/images/**","/listar").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin().permitAll()
		.successHandler(succesHandler)
		.loginPage("/login").permitAll()
		.and()
		.logout().permitAll();
	}



	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
		 builder.userDetailsService(user).passwordEncoder(encoder);
		/*
		
		/*PasswordEncoder encoder=this.encoder;
		//UserBuilder user=User.builder().passwordEncoder(password->encoder.encode(password));
		UserBuilder user=User.builder().passwordEncoder(encoder::encode);
		builder.inMemoryAuthentication().withUser(user.username("admin").password("12345").roles("ADMIN","USER"))
		.withUser(user.username("eduard").password("12345").roles("USER"));*/
		
	}
	
}
