package cc.gasches.geodata.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import lombok.RequiredArgsConstructor;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${app.bcrypt-strength}")
    private final int bcryptStrength;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(bcryptStrength);
    }

    @Bean
    protected HttpFirewall strictFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowedHttpMethods(
                Arrays.asList(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.DELETE.name(),
                        HttpMethod.PUT.name()));
        return firewall;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/actuator/*");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
                .csrf().disable()
                .formLogin().disable()
                .cors().and()
                .httpBasic().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/sections/**").permitAll()
                .antMatchers("/export/**").authenticated()
                .antMatchers("/import/**").authenticated()
                .antMatchers("/users/**").authenticated()
                .anyRequest().denyAll();
        //@formatter::on
    }
}
