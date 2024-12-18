package com.hotel.hotel_stars.Config;

import com.hotel.hotel_stars.filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;

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
                    configuration.setAllowedOriginPatterns(List.of("http://localhost:*")); // Mẫu

                    configuration.setAllowedMethods(
                            Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                    configuration.setAllowCredentials(true);
                    return configuration;
                }))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/account/login").authenticated()
                                .requestMatchers("/api/account/changepassword").authenticated()
                                // -------------------------- code này dành cho mấy cái api không cần
                                // token

                                                // All
                                                .requestMatchers("/api/service-package/post-service-package")
                                                .permitAll()
                                                .requestMatchers("/api/service-package/put-service-package").permitAll()
                                                .requestMatchers("/api/service-package/delete-service-package/**")
                                                .permitAll()
                                                .requestMatchers("/vnpay-payment").permitAll()
                                                .requestMatchers("/api/booking/downloadPdf").permitAll()
                                                .requestMatchers("/api/booking/booking-history-account").permitAll()
                                                // All
                                                // khoi
                                                .requestMatchers("/api/discount/get-discount-account").permitAll()
                                                .requestMatchers("/api/account/add-account-staff").permitAll()
                                                // khoi
                                                // vu
                                                //tuong
                                                .requestMatchers("/api/discount/get-discount-by-date", "/api/discount/get-discount-by-account").permitAll()
                                                .requestMatchers("/api/discount-accounts/add").hasAnyAuthority("Customer", "HotelOwner")
                                                 .requestMatchers("/api/messages/getAll", "/api/messages/add", "/api/messages/find-by-id/**",
                                                 "/api/feedback/getAllDC", "/api/feedback/getAllDPH", "/api/messages/send-email-feedback", "/api/discount/delete-discount/**",
                                                 "/api/discount/get-by-id/**", "/api/discount/update-discount", "/api/discount/hide-discount/**", "/api/discount/show-discount/**")
                                                 .hasAnyAuthority("HotelOwner")
                                                //tuong
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
                                                                "/api/booking/delete-booking",
                                                                "/api/floor/getAll",
                                                                "/api/type-room/find-type-room",
                                                                "/api/account/getAll",
                                                                "/api/feedback/postFeedBack",
                                                                "/api/booking-room/getAll",
                                                                "/api/type-room/get-list-room",
                                                                "/api/type-room/find-all-type-room",
                                                                "/api/type-room/detail-type-room",
                                                                "/api/room/list-room-filter",
                                                                "/api/room/details",
                                                                "/api/account/register",
                                                                "/api/account/getTokenGG",
                                                                "/api/account/loginToken",
                                                                "/api/account/sendEmail",
                                                                "/api/account/updateAccount",
                                                                "/api/account/updatePassword",
                                                                "/api/room/getCountRoom",
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
                                                                "api/type-room/add",
                                                                "api/type-room/update",
                                                                "api/type-room/delete/**",
                                                                "api/type-room/top3",
                                                                "/api/type-room/find-by-id",
                                                                "/api/type-room/find-amenities-type-rom/**",
                                                                "api/room/getAll",
                                                                "api/room/post-room",
                                                                "api/room/put-room",
                                                                "api/room/list-room",
                                                                "api/amenities-type-room/getAll",
                                                                "api/room/getById/**",
                                                                "/api/image/getAll",
                                                                "/api/service-hotel/getAll",
                                                                "/api/discount-accounts/getAll",
                                                                "/api/booking/room-usage"
                                                                )
                                                .permitAll()

                                                // nghia
                                                .requestMatchers("/api/account/sendEmail").permitAll()
                                                .requestMatchers("/api/discount/getAll").permitAll()
                                                .requestMatchers("/api/discount/getAllDiscountTR").permitAll()
                                                .requestMatchers("/api/account/getAll").permitAll()
                                                .requestMatchers("/api/booking/sendBooking").permitAll()
                                                .requestMatchers("/api/account/updateAccount").permitAll()
                                                .requestMatchers("/api/account/updatePassword").permitAll()
                                                .requestMatchers("/api/room/getCountRoom").permitAll()
                                                .requestMatchers("/api/booking/confirmBooking").permitAll()
                                                .requestMatchers("/api/type-room/find-type-room",
                                                                "/api/amenitiesHotel/getAll",
                                                                "/api/list-controller/get-staff",
                                                                "/api/feedback/get-all-use"
                                                                )
                                                .permitAll()
                                                // nghia

                                                // son
                                                .requestMatchers("/api/amenities-type-room/**").permitAll()
                                                // son

                                                // ---------------------------api cần token có phân quyền Customer (
                                                // khách hàng )
                                                // nghia

                                                // .requestMatchers("/api/booking/sendBooking").hasAnyAuthority("Customer")
                                                // vu
                                                // .requestMatchers("api/discount/**").hasAnyAuthority("Customer")
                                                // tuong cmt
                                                // vu

                                                // --------------------------- api cần token có phân quyền Staff ( nhân
                                                // viên )
                                                .requestMatchers("/api/hotel/login")
                                                .hasAnyAuthority("Staff", "HotelOwner")
                                                .requestMatchers("/api/hotel/getAll", "/api/amenitiesHotel/getById/**",
                                                                "/api/amenitiesHotel/update",
                                                                "/api/amenitiesHotel/delete/**",
                                                                "/api/amenitiesHotel/add",
                                                                "/api/discount/post-discount")
                                                .hasAnyAuthority("Staff", "HotelOwner")
                                                // --------------------------- api cần token có phân quyền HotelOwner
                                                // (chủ khách sạn )
                                                .requestMatchers("/api/account/login").hasAuthority("HotelOwner")
                                                // vu
                                                .requestMatchers(
                                                                "/api/booking/sendBooking")
                                                .hasAnyAuthority("Customer")
                                                // Các endpoint yêu cầu quyền "Staff" hoặc "HotelOwner"
                                                .requestMatchers(
                                                                "/api/booking/sendBooking")
                                                .hasAnyAuthority("Customer")

                                                .requestMatchers(
                                                                "/api/hotel/login",
                                                                "/api/hotel/getAll")
                                                .hasAnyAuthority("Staff", "HotelOwner")

                                                .requestMatchers(
                                                                "/api/account/login",
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
                                                                "/api/type-room-service/delete/**",
                                                                // tuong
                                                                "/api/floor/add",
                                                                "/api/floor/update",
                                                                "/api/floor/delete/**",
                                                                "/api/room/delete/**"
                                                // tuong

                                                )
                                                .hasAuthority("HotelOwner")

                                                // khoi
                                                .requestMatchers(
                                                	    "/api/service-room/update-service-room/**",
                                                	    "/api/service-room/add-service-room",
                                                	    "/api/service-room/delete-service-room/**",
                                                	    "/api/type-room-service/update/**",
                                                	    "/api/type-room-service/create",
                                                	    "/api/type-room-service/delete/**",
                                                	    "/api/booking-room/account/{id}",
                                                	    "/api/service-hotel/post-data-service-hotel",
                                                	    "/api/service-hotel/update-data-service-hotel",
                                                	    "/api/service-hotel/delete-data-service-hotel/**"
                                                	).hasAnyAuthority("HotelOwner")
                                                	.requestMatchers(
                                                	    "/api/booking-room/room",
                                                	    "/api/room/update-active",
                                                	    "/api/invoice/add"
                                                	).hasAnyAuthority("Staff", "HotelOwner")
                                                	.requestMatchers(
                                                	    "/api/status-room/get-status-excluding/**",
                                                	    "/api/room",
                                                	    "/api/booking/booking-offline",
                                                	    "/api/booking",
                                                	    "/api/booking/update-status/**",
                                                	    "/api/booking/update-checkIn/**",
                                                	    "/api/booking-infomation/booking-room",
                                                	    "/api/booking-room/list-booking-room",
                                                	    "/api/customer-info/add",
                                                	    "/api/customer-info/update/**",
                                                	    "/api/booking-infomation",
                                                	    "/api/booking/getById/**",
                                                	    "/api/booking-room/getById/**",
                                                	    "/api/booking-room-service-room/booking-room-id",
                                                	    "/api/booking-room-service-room/service",
                                                	    "/api/service-room/booking-room",
                                                	    "/api/booking-room-service-room/**",
                                                	    "/api/booking/downloadPdf",
                                                	    "/api/booking/cancel-booking/**",
                                                	    "/api/booking/booking-by-room/**"
                                                	).permitAll()

                                // khoi
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
                                        "/api/account/updateAccount",
                                        "/api/account/updatePassword",
                                        "/api/room/getCountRoom",
                                        "/api/booking/confirmBooking",
                                        "/api/type-room/find-type-room",
                                        "/api/service-package/post-service-package",
                                        "/api/service-package/put-service-package",
                                        "/api/service-package/delete-service-package/**",
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
                                        "api/type-room/add",
                                        "api/type-room/update",
                                        "api/type-room/delete/**",
                                        "api/type-room/top3",
                                        "/api/type-room/find-by-id",
                                        "/api/type-room/find-amenities-type-rom/**",
                                        "api/room/getAll",
                                        "api/room/post-room",
                                        "api/room/put-room",
                                        "api/room/list-room",
                                        "api/amenities-type-room/getAll",
                                        "api/room/getById/**",
                                        "/api/image/getAll",
                                        "/api/service-hotel/getAll",
                                        "/api/discount-accounts/getAll",
                                        "/api/room/list-room-id","/api/room/room-occupancy"
                                )
                                .permitAll()

                                // nghia
                                .requestMatchers("/api/account/sendEmail").permitAll()
                                .requestMatchers("/api/discount/getAll").permitAll()
                                .requestMatchers("/api/discount/getAllDiscountTR").permitAll()
                                .requestMatchers("/api/account/getAll").permitAll()
                                .requestMatchers("/api/booking/sendBooking").permitAll()
                                .requestMatchers("/api/account/updateAccount").permitAll()
                                .requestMatchers("/api/account/updatePassword").permitAll()
                                .requestMatchers("/api/room/getCountRoom").permitAll()
                                .requestMatchers("/api/booking/confirmBooking").permitAll()
                                .requestMatchers("/api/type-room/find-type-room",
                                        "/api/amenitiesHotel/getAll",
                                        "/api/room/top-5-type-room",
                                        "/api/account/updateAccountCustomer"
                                        )
                                .permitAll()
                                // nghia

                                // son
                                .requestMatchers("/api/amenities-type-room/**").permitAll()
                                // son

                                // ---------------------------api cần token có phân quyền Customer (
                                // khách hàng )
                                // nghia

                                // .requestMatchers("/api/booking/sendBooking").hasAnyAuthority("Customer")
                                // vu
                                // .requestMatchers("api/discount/**").hasAnyAuthority("Customer")
                                // tuong cmt
                                // vu

                                // --------------------------- api cần token có phân quyền Staff ( nhân
                                // viên )
                                .requestMatchers("/api/hotel/login")
                                .hasAnyAuthority("Staff", "HotelOwner")
                                .requestMatchers("/api/hotel/getAll", "/api/amenitiesHotel/getById/**",
                                        "/api/amenitiesHotel/update",
                                        "/api/amenitiesHotel/delete/**",
                                        "/api/amenitiesHotel/add",
                                        "/api/discount/post-discount","/api/discount/get-by-id/**","/api/discount/update-discount")
                                .hasAnyAuthority("Staff", "HotelOwner")
                                // --------------------------- api cần token có phân quyền HotelOwner
                                // (chủ khách sạn )
                                .requestMatchers("/api/account/login").hasAuthority("HotelOwner")
                                // vu
                                .requestMatchers(
                                        "/api/booking/sendBooking")
                                .hasAnyAuthority("Customer")
                                // Các endpoint yêu cầu quyền "Staff" hoặc "HotelOwner"
                                .requestMatchers(
                                        "/api/booking/sendBooking")
                                .hasAnyAuthority("Customer")

                                .requestMatchers(
                                        "/api/hotel/login",
                                        "/api/hotel/getAll")
                                .hasAnyAuthority("Staff", "HotelOwner")

                                .requestMatchers(
                                        "/api/account/login",
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
                                        "/api/type-room-service/delete/**",
                                        // tuong
                                        "/api/floor/add",
                                        "/api/floor/update",
                                        "/api/floor/delete/**",
                                        "/api/room/delete/**"
                                        // tuong

                                )
                                .hasAuthority("HotelOwner")

                                // khoi
                                .requestMatchers("/api/service-room/update-service-room/**")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/service-room/add-service-room")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/service-room/delete-service-room/**")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/type-room-service/update/**")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/type-room-service/create")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/type-room-service/delete/**")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/booking-room/account/{id}")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/service-hotel/post-data-service-hotel")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/service-hotel/update-data-service-hotel")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/service-hotel/delete-data-service-hotel/**")
                                .hasAnyAuthority("HotelOwner")
                                // .requestMatchers("/api/booking-infomation/booking-room").hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/booking-room-service-room/service")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/booking-room/room")
                                .hasAnyAuthority("Staff", "HotelOwner")
                                .requestMatchers("/api/status-room/get-status-excluding/**").permitAll()
                                .requestMatchers("/api/room/update-active")
                                .hasAnyAuthority("Staff", "HotelOwner")
                                .requestMatchers("/api/room").permitAll()
                                .requestMatchers("/api/booking/booking-offline").permitAll()
                                .requestMatchers("/api/booking").permitAll()
                                .requestMatchers("/api/booking/update-status/**").permitAll()
                                .requestMatchers("/api/booking/update-checkIn/**").permitAll()
                                .requestMatchers("/api/booking-infomation/booking-room").permitAll()
                                .requestMatchers("/api/booking-room/list-booking-room").permitAll()
                                .requestMatchers("/api/customer-info/add").permitAll()
                                // khoi
                                .requestMatchers("/api/service-room/update-service-room/**")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/service-room/add-service-room")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/service-room/delete-service-room/**")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/type-room-service/update/**")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/type-room-service/create")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/type-room-service/delete/**")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/booking-room/account/{id}")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/service-hotel/post-data-service-hotel")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/service-hotel/update-data-service-hotel")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/service-hotel/delete-data-service-hotel/**")
                                .hasAnyAuthority("HotelOwner")
                                // .requestMatchers("/api/booking-infomation/booking-room").hasAnyAuthority("HotelOwner")
                                // .requestMatchers("/api/booking-room-service-room/service").hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/booking-room/room")
                                .hasAnyAuthority("Staff", "HotelOwner")
                                .requestMatchers("/api/status-room/get-status-excluding/**").permitAll()
                                .requestMatchers("/api/room/update-active")
                                .hasAnyAuthority("Staff", "HotelOwner")
                                .requestMatchers("/api/room").permitAll()
                                .requestMatchers("/api/booking/booking-offline").permitAll()
                                .requestMatchers("/api/booking").permitAll()
                                .requestMatchers("/api/booking/update-status/**").permitAll()
                                .requestMatchers("/api/booking/update-checkIn/**").permitAll()
                                .requestMatchers("/api/booking-infomation/booking-room").permitAll()
                                .requestMatchers("/api/booking-room/list-booking-room").permitAll()
                                .requestMatchers("/api/customer-info/add").permitAll()
                                .requestMatchers("/api/customer-info/update/**").permitAll()
                                .requestMatchers("/api/booking-infomation").permitAll()
                                .requestMatchers("/api/booking/getById/**").permitAll()
                                .requestMatchers("/api/booking-room/getById/**").permitAll()
                                .requestMatchers("/api/booking-room-service-room/booking-room-id")
                                .permitAll()
                                .requestMatchers("/api/booking-room-service-room/service").permitAll()
                                // khoi
                                .requestMatchers("/api/service-room/update-service-room/**")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/service-room/add-service-room")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/service-room/delete-service-room/**")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/type-room-service/update/**")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/type-room-service/create")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/type-room-service/delete/**")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/booking-room/account/{id}")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/service-hotel/post-data-service-hotel")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/service-hotel/update-data-service-hotel")
                                .hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/service-hotel/delete-data-service-hotel/**")
                                .hasAnyAuthority("HotelOwner")
                                // .requestMatchers("/api/booking-infomation/booking-room").hasAnyAuthority("HotelOwner")
                                // .requestMatchers("/api/booking-room-service-room/service").hasAnyAuthority("HotelOwner")
                                .requestMatchers("/api/booking-room/room")
                                .hasAnyAuthority("Staff", "HotelOwner")
                                .requestMatchers("/api/status-room/get-status-excluding/**").permitAll()
                                .requestMatchers("/api/room/update-active")
                                .hasAnyAuthority("Staff", "HotelOwner")
                                .requestMatchers("/api/room").permitAll()
                                .requestMatchers("/api/booking/booking-offline").permitAll()
                                .requestMatchers("/api/booking").permitAll()
                                .requestMatchers("/api/booking/reservation").permitAll()
                                .requestMatchers("/api/invoice/statistics").permitAll()
                                .requestMatchers("/api/invoice/statistics2").permitAll()
                                .requestMatchers("/api/invoice/revenue2").permitAll()
                                .requestMatchers("/api/invoice/getAll-statistics").permitAll()
                                .requestMatchers("/api/invoice/maintenance").permitAll()
                                .requestMatchers("/api/invoice/getAll-reservation").permitAll()
                                .requestMatchers("/api/account/deleteAccount/**").permitAll()
                                .requestMatchers("/api/booking/by-start-date-with-invoice").permitAll()
                                .requestMatchers("/api/booking/update-status/**").permitAll()
                                .requestMatchers("/api/booking/update-checkIn/**").permitAll()
                                .requestMatchers("/api/booking-infomation/booking-room").permitAll()
                                .requestMatchers("/api/booking/booking-history-account").permitAll()
                                .requestMatchers("/api/booking-room/list-booking-room").permitAll()
                                .requestMatchers("/api/customer-info/add").permitAll()
                                .requestMatchers("/api/customer-info/update/**").permitAll()
                                .requestMatchers("/api/booking-infomation").permitAll()
                                .requestMatchers("/api/booking/getById/**").permitAll()
                                .requestMatchers("/api/booking-room/getById/**").permitAll()
                                .requestMatchers("/api/booking-room-service-room/booking-room-id")
                                .permitAll()
                                .requestMatchers("/api/booking-room-service-room/service").permitAll()
                                .requestMatchers("/api/service-room/booking-room").permitAll()
                                .requestMatchers("/api/booking-room-service-room/**").permitAll()
                                .requestMatchers("/api/booking-room-service-room/post-service").permitAll()
                                .requestMatchers("/api/booking-room/getByRoom/**").permitAll()
                                .requestMatchers("/api/booking/booking-maintenance").permitAll()
                                .requestMatchers("/api/discount/discount-name-booking").permitAll()
                        // khoi
                        // vu

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
