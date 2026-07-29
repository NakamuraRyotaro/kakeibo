package dev.nakamura.kakeibo_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Cookie形式じゃないので、無効
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // サーバー側でセッションを持たない
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health").permitAll() // ALBはJWTを持たないので認証不要
                .anyRequest().authenticated() // 上記以外は全て認証が必要
            )
            .oauth2ResourceServer(o -> o.jwt(Customizer.withDefaults())); // jwt検証の有効化
        return http.build();
    }
}