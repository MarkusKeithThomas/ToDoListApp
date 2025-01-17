package thuong.todolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import thuong.todolist.filter.CustomSecurityFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 1. Cho phép mọi nguồn gốc (chỉ nên dùng cho phát triển)
        config.addAllowedOriginPattern("*");

        // 2. Hoặc chỉ cho phép những URL cụ thể
        // config.setAllowedOrigins(List.of("http://10.0.2.2:8080", "http://192.168.1.100:3000"));

        // 3. Các HTTP method được phép
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 4. Các header được phép
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));

        // 5. Expose các header để client có thể thấy
        config.setExposedHeaders(List.of("Authorization"));

        // 6. Cho phép credentials (cookie, token,...)
        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,CustomSecurityFilter customSecurityFilter) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sử dụng Stateless Session
                .authorizeHttpRequests(request ->{
                    request.requestMatchers("/login").permitAll();
                    request.requestMatchers(HttpMethod.POST,"/register").permitAll();
                    request.requestMatchers("/todolist").authenticated();
                    request.requestMatchers("/todolist/**").authenticated();
                    request.requestMatchers("/todolist/add").permitAll();
                    request.requestMatchers("/image/**").permitAll();
                    request.anyRequest().authenticated();
                })
                .addFilterBefore(customSecurityFilter, UsernamePasswordAuthenticationFilter.class) // Sử dụng filter do Spring quản lý
                .build();
    }
}
