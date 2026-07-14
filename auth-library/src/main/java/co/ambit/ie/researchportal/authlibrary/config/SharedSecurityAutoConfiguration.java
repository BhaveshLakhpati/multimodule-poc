package co.ambit.ie.researchportal.authlibrary.config;

import co.ambit.ie.researchportal.authlibrary.filter.JwtCookieAuthFilter;
import co.ambit.ie.researchportal.authlibrary.filter.RequestLoggingFilter;
import co.ambit.ie.researchportal.authlibrary.properties.CustomSecurityProperties;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.util.ObjectUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(CustomSecurityProperties.class)
@Slf4j
public class SharedSecurityAutoConfiguration {
    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity,
                                            final CustomSecurityProperties securityProperties,
                                            final Tracer tracer
//                                            , final JwtDecoder jwtDecoder
    ) {
        log.info("excluded-paths: {}", securityProperties);

        httpSecurity.csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                // TODO: fix before PROD
                .cors(corsCustomizer -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(List.of("*"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setExposedHeaders(List.of("X-Trace-Id"));

                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", corsConfiguration);

                    corsCustomizer.configurationSource(source);
                })
                .authorizeHttpRequests(auth -> {
                    if (!ObjectUtils.isEmpty(securityProperties.getExcludedPaths())) {
                        auth.requestMatchers(
                                securityProperties.getExcludedPaths().toArray(new String[0])
                        ).permitAll();
                    }

                    auth.anyRequest().authenticated();
                })
                .sessionManagement(sessionCustomizer -> sessionCustomizer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(FormLoginConfigurer::disable)
                .addFilterBefore(new RequestLoggingFilter(tracer),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtCookieAuthFilter(securityProperties),
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
