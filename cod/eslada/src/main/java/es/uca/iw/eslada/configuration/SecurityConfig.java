package es.uca.iw.eslada.configuration;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.requestMatchers(new AntPathRequestMatcher("/**")).permitAll());
//        http.authorizeHttpRequests(auth -> {
//            auth.requestMatchers(new AntPathRequestMatcher("/register")).permitAll();
//            auth.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll(); //TODO: Delete access to console
//        });
        super.configure(http);

    }
}