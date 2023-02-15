package databases.itmo.coursework.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/images/**",
                        "/css/**")
                .addResourceLocations("classpath:/static/images/",
                        "classpath:/static/css/");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/customer/**").hasRole("CUSTOMER")
                        .requestMatchers("/executor/**").hasRole("EXECUTOR")
                        .requestMatchers("/moderator/**").hasRole("MODERATOR")

                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults());
//                .usernameParameter("email")
//                .passwordParameter("password");
        return http.build();

    }

}
