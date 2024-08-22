package com.tinqinacademy.bff.rest.config;

        import com.tinqinacademy.comments.api.contracts.RestApiRoutesComments;
        import com.tinqinacademy.hotel.api.contracts.RestApiRoutes;
        import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
        import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
        import io.swagger.v3.oas.annotations.security.SecurityScheme;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.http.HttpMethod;
        import org.springframework.security.authentication.AuthenticationManager;
        import org.springframework.security.config.Customizer;
        import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
        import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
        import org.springframework.security.config.annotation.web.builders.HttpSecurity;
        import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
        import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
        import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
        import org.springframework.security.config.http.SessionCreationPolicy;
        import org.springframework.security.web.SecurityFilterChain;
        import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@SecurityScheme(
        name = "bearerAuth",
        scheme = "bearer",
        bearerFormat = "JWT",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig ( JWTAuthenticationFilter jwtAuthenticationFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, RestApiRoutes.BOOK_ROOM).hasAnyAuthority("ADMIN", "USER")
                         .requestMatchers(HttpMethod.DELETE, RestApiRoutesComments.ADMIN_DELETE).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, RestApiRoutesComments.ADMIN_EDIT).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, RestApiRoutes.DELETE_ROOM). hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, RestApiRoutes.REMOVE_BOOKING).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, RestApiRoutes.PARTIAL_UPDATE_ROOM).hasAuthority("ADMIN")

                        .anyRequest().permitAll()

                ).httpBasic(Customizer.withDefaults())
                .sessionManagement(s ->
                        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}