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
                        // -------------------------- code này dành cho mấy cái api không cần token

                        // All
                        .requestMatchers("/api/service-package/post-service-package").permitAll()
                        .requestMatchers("/api/service-package/put-service-package").permitAll()
                        .requestMatchers("/api/service-package/delete-service-package/**").permitAll()
                        // All
                                //khoi
                                .requestMatchers("/api/account/getAll").permitAll()
                                .requestMatchers("/api/account/add-account-staff").permitAll()
                                //khoi
                        // vu
                        .requestMatchers(
                                "/api/account/account-by-id/{username}",
                                "/api/account/toggleDelete/{id}",
                                "/api/account/get-info-staff",
                                "api/feedback/**",
                                "api/service-hotel/getAll",
                                "/api/status/**",
                                "/api/image/**",
                                "api/hotel/**",
                                "/api/status-room/getAll",
                                "/api/floor/getAll",
                                "/api/account/getAll",
                                "/api/booking-room/getAll",
                                "/api/type-room/get-list-room",
                                "/api/type-room/detail-type-room",
                                "/api/room/list-room-filter",
                                "/api/room/details",
                                "/api/account/register",
                                "/api/account/getTokenGG",
                                "/api/account/loginToken",
                                "/api/account/sendEmail",
                                "/api/discount/getAll",
                                "/api/account/updateAccount",
                                "/api/account/updatePassword",
                                "/api/room/getCountRoom",
                                "/api/booking/sendBooking",
                                "/api/booking/confirmBooking",
                                "/api/type-room/find-type-room",
                                "/api/service-package/post-service-package",
                                "/api/service-package/put-service-package",
                                "/api/service-package/delete-service-package/**",
                                "/api/type-room/getAll",
                                "api/type-room/add",
                                "api/type-room/update",
                                "api/type-room/delete/",
                                "api/type-room/top3",
                                "/api/type-room/**",
                                "/api/service-package/getAll",
                                "/api/service-room/getAll",
                                "/api/type-room-service/getAll",
                                "/api/room/FloorById/{id}",
                                "api/type-room-amenities-type-room/**",
                                "/api/booking/accountId/{id}",
                                "/api/account/{id}",
                                "/api/type-room/getAll",
                                "api/type-room/add",
                                "api/type-room/update",
                                "api/type-room/delete/**",
                                "api/type-room/top3",
                                "/api/type-room/find-by-id",
                                "/api/type-room/find-amenities-type-rom/**",
                                "api/room/getAll",
                                "api/room/post-room",
                                "api/room/put-room",
                                "api/amenities-type-room/getAll",
                                "api/room/getById/**"
                                )
                        .permitAll()

                                // nghia
                                .requestMatchers("/api/account/sendEmail").permitAll()
                                .requestMatchers("/api/discount/getAll").permitAll()
                                .requestMatchers("/api/account/getAll").permitAll()
                                .requestMatchers("/api/booking/sendBooking").permitAll()
                                .requestMatchers("/api/account/updateAccount").permitAll()
                                .requestMatchers("/api/account/updatePassword").permitAll()
                                .requestMatchers("/api/room/getCountRoom").permitAll()
                                .requestMatchers("/api/booking/confirmBooking").permitAll()
                                .requestMatchers("/api/type-room/find-type-room").permitAll()
                                // nghia

                                //son
                                .requestMatchers("/api/amenities-type-room/**").permitAll()
                                //son

                                //---------------------------api cần token có phân quyền Customer  ( khách hàng )
                                //nghia

                               // .requestMatchers("/api/booking/sendBooking").hasAnyAuthority("Customer")
                                //vu
                                .requestMatchers("api/discount/**").hasAnyAuthority("Customer")
                                //vu

                                //--------------------------- api cần token có phân quyền Staff  ( nhân viên )
                                .requestMatchers("/api/hotel/login").hasAnyAuthority("Staff", "HotelOwner")
                                .requestMatchers("/api/hotel/getAll").hasAnyAuthority("Staff", "HotelOwner")




                        //--------------------------- api cần token có phân quyền HotelOwner  (chủ  khách sạn )
                        .requestMatchers("/api/account/login").hasAuthority("HotelOwner")
                        //vu
                        .requestMatchers(
                                "api/discount/**")
                        .hasAnyAuthority("Customer")
                        // Các endpoint yêu cầu quyền "Staff" hoặc "HotelOwner"
                        .requestMatchers(
                                "/api/hotel/login",
                                "/api/hotel/getAll")
                        .hasAnyAuthority("Staff", "HotelOwner")

                        // Các endpoint yêu cầu quyền "HotelOwner"
                        .requestMatchers(
                                "/api/account/login",
                                "api/discount/**",
                                "/api/account/update-account-staff/{id}",
                                "/api/account/delete-account-staff/{id}",
                                "api/hotel/update-hotel/{id}",
                                "api/overview/room-types/get-all",
                                "api/overview/room-types/get-list-room",
                                "api/overview/room-types/get-by-id",
                                "api/overview/room-types/booking-history",
                                "api/overview/room-types/bed-type-options",
                                "/api/reservations/getAll",
                                "/api/reservations/selectBookingById",
                                "/api/reservations/statusBooking",
                                "/api/amenities-type-room/add",
                                "/api/amenities-type-room/update",
                                "/api/amenities-type-room/delete/",
                                "/api/service-hotel/post-data-service-hotel",
                                "/api/service-hotel/update-data-service-hotel",
                                "/api/service-hotel/delete-data-service-hotel/**",
                                "/api/service-room/update-service-room/**",
                                "/api/service-room/add-service-room",
                                "/api/service-room/delete-service-room/**",
                                "/api/type-room-service/update/**",
                                "/api/type-room-service/create",
                                "/api/type-room-service/delete/**")
                        .hasAuthority("HotelOwner")

                        // khoi
                        .requestMatchers("/api/service-room/update-service-room/**").hasAnyAuthority("HotelOwner")
                        .requestMatchers("/api/service-room/add-service-room").hasAnyAuthority("HotelOwner")
                        .requestMatchers("/api/service-room/delete-service-room/**").hasAnyAuthority("HotelOwner")
                        .requestMatchers("/api/type-room-service/update/**").hasAnyAuthority("HotelOwner")
                        .requestMatchers("/api/type-room-service/create").hasAnyAuthority("HotelOwner")
                        .requestMatchers("/api/type-room-service/delete/**").hasAnyAuthority("HotelOwner")
                        .requestMatchers("/api/booking-room/account/{id}").hasAnyAuthority("HotelOwner")
                        .requestMatchers("/api/service-hotel/post-data-service-hotel").hasAnyAuthority("HotelOwner")
                        .requestMatchers("/api/service-hotel/update-data-service-hotel").hasAnyAuthority("HotelOwner")
                        .requestMatchers("/api/service-hotel/delete-data-service-hotel/**")
                        .hasAnyAuthority("HotelOwner")
                        .requestMatchers("/api/booking-infomation/booking-room").hasAnyAuthority("HotelOwner")
                        .requestMatchers("/api/booking-room-service-room/service").hasAnyAuthority("HotelOwner")
                        .requestMatchers("/api/booking-room/room").hasAnyAuthority("Staff", "HotelOwner")
                        .requestMatchers("/api/status-room/get-status-excluding/**").permitAll()
                        .requestMatchers("/api/room/update-active").hasAnyAuthority("Staff", "HotelOwner")
                        .requestMatchers("/api/room").permitAll()
                // khoi
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
