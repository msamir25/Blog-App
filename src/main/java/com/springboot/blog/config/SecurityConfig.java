package com.springboot.blog.config;


import com.springboot.blog.security.CustomUserDetailService;
import com.springboot.blog.security.JwtAuthenticationEntryPoint;
import com.springboot.blog.security.JwtAuthenticationFilter;
import jakarta.servlet.FilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


        @Autowired
        private CustomUserDetailService userDetailService;

        @Autowired
        private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter(){
                return  new JwtAuthenticationFilter();
        }
        @Bean
        PasswordEncoder passwordEncoder(){
                return new BCryptPasswordEncoder();
        }


        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                        //.csrf(Customizer.withDefaults())
                        .csrf(AbstractHttpConfigurer::disable)
                        .exceptionHandling( httpSecurityExceptionHandlingConfigurer ->
                                httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                        .authorizeHttpRequests(authorize ->
                                authorize
                                         .requestMatchers(HttpMethod.GET , "/api/v1/posts").hasRole("USER")
                                         .requestMatchers(HttpMethod.GET , "/api/v1/posts/**").permitAll()
                                         .requestMatchers(HttpMethod.POST , "/api//v1/posts").hasRole("ADMIN")
                                         .requestMatchers(HttpMethod.PUT , "/api/v1/posts/**").hasRole("ADMIN")
                                         .requestMatchers(HttpMethod.DELETE , "/api/v1/posts/**").hasRole("ADMIN")
                                         .requestMatchers("/api/v1/auth/**").permitAll()
                                        //.requestMatchers("/api/**").permitAll()
                                         .requestMatchers("/v3/api-docs/**").permitAll()
                                        .requestMatchers("/swagger-ui/**").permitAll()
                                        .requestMatchers("/swagger-resources/**").permitAll()
                                        .requestMatchers("/swagger-ui.html").permitAll()
                                        .requestMatchers("/webjars/**").permitAll()


                                .anyRequest().authenticated()
                        )
                        .sessionManagement((session) -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                http.addFilterBefore(jwtAuthenticationFilter() , UsernamePasswordAuthenticationFilter.class);
                return http.build();

        }



/*        @Bean
        public AuthenticationProvider configure() {
                DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
                authenticationProvider.setUserDetailsService(userDetailService);
                authenticationProvider.setPasswordEncoder(passwordEncoder());
                return authenticationProvider;
        }*/

       @Bean
        public AuthenticationManager authenticationManager(
                AuthenticationConfiguration authenticationConfiguration) throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }
/*        @Bean
        public InMemoryUserDetailsManager userDetailsService(){
                UserDetails mohamed = User.builder()
                        .username("mohamed")
                        .password(passwordEncoder()
                                .encode("password")).
                        roles("USER").build();

                UserDetails admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder()
                                .encode("admin"))
                        .roles("ADMIN").build();

                return new InMemoryUserDetailsManager(mohamed , admin);
        }*/

}
