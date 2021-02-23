package pl.com.khryniewicki.heroesbattle.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoders;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    String issuerUri;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();

        http.cors()
                .and()
                .authorizeRequests()
                .antMatchers("/websocket-example/**","/game/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/download/**", "/api/option/**")
                .hasAuthority("SCOPE_user")
                .anyRequest()
                .authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt(jwtConfigurer -> jwtConfigurer.decoder(JwtDecoders.fromIssuerLocation(issuerUri)));
    }

}
