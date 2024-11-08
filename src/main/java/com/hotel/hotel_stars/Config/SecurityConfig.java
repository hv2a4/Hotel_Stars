package com.hotel.hotel_stars.Config;

import com.hotel.hotel_stars.filter.JwtAuthFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Lazy
    @Autowired
    private JwtAuthFilter authFilter;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    // User Creation
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserInfoService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of("*")); // Thay đổi miền nếu cần
                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                    return configuration;
                }))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/account/login").authenticated()
                                //--------------------------  code này dành cho mấy cái api không cần token
                                // vu
                                .requestMatchers(
                                        "/api/hotel/getAll",
                                        "/api/account/account-by-id/{username}",
                                        "/api/account/toggleDelete/{id}",
                                        "/api/account/get-info-staff",
                                        "/api/booking/**",
                                        "api/feedback/**",
                                        "api/service-hotel/**",
                                        "/api/status/**",
                                        "/api/image/**"
                                )
                                .permitAll()
                                // vu

                                // nghia
                                .requestMatchers("/api/account/register").permitAll()
                                .requestMatchers("/api/account/getTokenGG").permitAll()
                                .requestMatchers("/api/account/loginToken").permitAll()
                                .requestMatchers("/api/account/sendEmail").permitAll()
                                .requestMatchers("/api/discount/getAll").permitAll()
                                .requestMatchers("/api/account/updateAccount").permitAll()
                                .requestMatchers("/api/account/updatePassword").permitAll()
                                .requestMatchers("/api/room/getCountRoom").permitAll()
                                .requestMatchers("/api/booking/sendBooking").permitAll()
                                .requestMatchers("/api/booking/confirmBooking").permitAll()
                                // nghia

                                //son
                                .requestMatchers("/api/amenities-type-room/**").permitAll()
                                .requestMatchers("/api/type-room/**").permitAll()
                                //son

                                //---------------------------api cần token có phân quyền Customer  ( khách hàng )
                                //vu
                                .requestMatchers("api/discount/**").hasAnyAuthority("Customer")
                                //vu

                                //--------------------------- api cần token có phân quyền Staff  ( nhân viên )
                                .requestMatchers("/api/hotel/login").hasAnyAuthority("Staff", "HotelOwner")
                                .requestMatchers("/api/hotel/getAll").hasAnyAuthority("Staff", "HotelOwner")


                                //--------------------------- api cần token có phân quyền HotelOwner  (chủ  khách sạn )
                                .requestMatchers("/api/account/login").hasAuthority("HotelOwner")
                                //vu
                                .requestMatchers("api/overview/room-types/get-all","api/overview/room-types/get-by-id","api/overview/room-types/get-list-room").hasAuthority("HotelOwner")
                                //vu
                                //vu
                                .requestMatchers(
                                        "api/discount/**",
                                        "api/service-package/**",
                                        "api/room/**",
                                        "/api/account/add-account-staff",
                                        "/api/account/update-account-staff/{id}",
                                        "/api/account/delete-account-staff/{id}",
                                        "api/hotel/update-hotel/{id}")
                                .hasAnyAuthority("HotelOwner")
                        //vu
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Password Encoding
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
