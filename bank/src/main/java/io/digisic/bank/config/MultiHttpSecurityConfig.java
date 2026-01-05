package io.digisic.bank.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import io.digisic.bank.security.JwtTokenFilterConfigurer;
import io.digisic.bank.security.JwtTokenProvider;
import io.digisic.bank.util.Constants;
import io.digisic.bank.util.Patterns;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Replaces EnableGlobalMethodSecurity
public class MultiHttpSecurityConfig {

    private static final String[] PUBLIC = {
            Constants.URI_WEBJARS_RES, 
            Constants.URI_CSS_RES, 
            Constants.URI_SCSS_RES, 
            Constants.URI_FONTS_RES, 
            Constants.URI_JS_RES, 
            Constants.URI_IMAGES_RES, 
            Constants.URI_REGISTER, 
            Constants.URI_ABOUT_RES, 
            Constants.URI_CONTACT_RES, 
            Constants.URI_ERROR_RES,
            Constants.URI_ROOT,
            Constants.URI_SIGNUP,
            Constants.URI_H2_CONSOLE,
            Constants.URI_SWAGGER_UI,
            Constants.URI_SWAGGER_V2,
            Constants.URI_SWAGGER_RES,
            Constants.URI_SWAGGER_CONF,
            Constants.URI_MANAGER_RES,
            Constants.URI_FAVICON_RES,
            Constants.URI_QUERY_DB,
            Constants.URI_QUERY_USER
    };

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // --- API Security Configuration (Order 1) ---
    @Bean
    @Order(1)
public SecurityFilterChain apiFilterChain(HttpSecurity http, JwtTokenProvider jwtTokenProvider) throws Exception {
    http
        .securityMatcher(Constants.URI_API_ALL) 
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            // 1. Specific Public Endpoints
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .requestMatchers(HttpMethod.POST, Constants.URI_API_AUTH).permitAll()
            .requestMatchers(HttpMethod.GET, Constants.URI_API_HEALTHCHECK).permitAll()
            
            // 2. Specific Role Requirements
            .requestMatchers(Constants.URI_API_ALL).hasRole(Patterns.ROLE_API)
            
            // 3. Catch-all (MUST BE LAST)
            .anyRequest().authenticated()
        )
        .exceptionHandling(ex -> ex.accessDeniedPage(Constants.URI_API_AUTH))
        .with(new JwtTokenFilterConfigurer(jwtTokenProvider), Customizer.withDefaults());

    return http.build();
}

 // --- Form Login Security Configuration (Default Order) ---
    @Bean
    public SecurityFilterChain formFilterChain(HttpSecurity http, 
                                              @Value("${io.digisic.max.sessions}") String propMaxSessions) throws Exception {
        
        final Logger LOG = LoggerFactory.getLogger("FormLoginConfig");
        
        // 1. Calculate the value first
        int tempMaxSessions = 1;
        try { 
            tempMaxSessions = Integer.parseInt(propMaxSessions); 
        } catch (Exception e) {
            LOG.warn("Invalid max sessions property, defaulting to 1");
        }
        
        // 2. Assign to a FINAL variable so the lambda can use it
        final int finalMaxSessions = tempMaxSessions;
        
        LOG.info("Max Sessions: " + finalMaxSessions);

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC).permitAll()
                .anyRequest().hasRole(Patterns.ROLE_USER)
            )
            .formLogin(form -> form
                .loginPage(Constants.URI_LOGIN)
                .failureUrl(Constants.URI_LOGIN_ERR)
                .defaultSuccessUrl(Constants.URI_HOME)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher(Constants.URI_LOGOUT))
                .logoutSuccessUrl(Constants.URI_LOGOUT_SUCC)
                .deleteCookies(Constants.COO_JSESSION_ID)
                .permitAll()
            )
            .rememberMe(rm -> rm
                .key(Constants.COO_REMEBER_ME)
                .tokenValiditySeconds(86400)
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .maximumSessions(finalMaxSessions) // Use the final variable here
                .maxSessionsPreventsLogin(true)
            );

        return http.build();
    }
}