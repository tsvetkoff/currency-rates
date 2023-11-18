package tsvetkoff.currencyrates.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))
                .httpBasic(withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(antMatcher(HttpMethod.GET, "/actuator/**")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.GET, "/swagger-ui/**")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.GET, "/swagger-ui.html")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.GET, "/api-docs/**")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.GET, "/v3/api-docs/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/api/rates")).hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                        .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/api/rates/current")).hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                        .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST, "/api/rates/update")).hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        String findUserQuery = """
                    SELECT username, password, enabled
                    FROM public.users
                    WHERE username = ?
                """;
        String findRolesQuery = """
                    SELECT username, authority
                    FROM public.authorities
                    WHERE username = ?
                """;

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setDataSource(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(findUserQuery);
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(findRolesQuery);
        return jdbcUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public enum Role {
        USER,
        ADMIN
    }

}
