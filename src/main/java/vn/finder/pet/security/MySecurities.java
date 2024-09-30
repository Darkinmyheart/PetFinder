package vn.finder.pet.security;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vn.finder.pet.entity.Users;
import vn.finder.pet.oauth2User.CustomOAuth2User;
import vn.finder.pet.oauth2User.CustomOAuth2UserService;
import vn.finder.pet.service.UsersService;

import javax.sql.DataSource;
import java.security.SecureRandom;
import java.sql.Date;
import java.time.LocalDate;

@Configuration
public class MySecurities {

    private DataSource dataSource;
    private CustomOAuth2UserService customOAuth2UserService;
    private UsersService usersService;
    private HttpSession session;
    @Autowired
    public MySecurities(CustomOAuth2UserService customOAuth2UserService, DataSource dataSource, UsersService usersService, HttpSession session) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.dataSource = dataSource;
        this.usersService = usersService;
        this.session = session;
    }

    @Bean
    @Primary
    public JdbcUserDetailsManager jdbcUserDetailsManager() {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(config ->
                        config.requestMatchers("/account/signUp-shelter").hasRole("USER")
                                .requestMatchers("/pet-list/add-favorites").hasAnyAuthority("OAUTH2_USER", "ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN")
                                .requestMatchers("/pet-list/remove-favorites").hasAnyAuthority("OAUTH2_USER", "ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN")
                                .requestMatchers("/account/**").hasAnyAuthority("OAUTH2_USER", "ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN")
                                .requestMatchers("/manager/**").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN")
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/**").permitAll())
                .formLogin(login -> {
                    login.loginPage("/sign-in").loginProcessingUrl("/authenticateTheUser").defaultSuccessUrl("/index").permitAll();
                })
                .oauth2Login(oauth -> {
                    oauth.loginPage("/login")
                            .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService))
                            .successHandler((request, response, authentication) -> {
                                CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
                                String email = oAuth2User.getAttribute("email");

                                if(this.usersService.findById(email).isEmpty()) {
                                    String given_name  = oAuth2User.getAttribute("given_name");
                                    String family_name  = oAuth2User.getAttribute("family_name");

                                    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                                    StringBuilder password = new StringBuilder(10);
                                    SecureRandom random = new SecureRandom();
                                    for (int i = 0; i < 10; i++) {
                                        int randomIndex = random.nextInt(characters.length());
                                        password.append(characters.charAt(randomIndex));
                                    }

                                    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                                    String enCode = bCryptPasswordEncoder.encode(password);
                                    Users users = new Users(email, "{bcrypt}"+enCode, true, family_name, given_name, null, Date.valueOf(LocalDate.now()));
                                    this.session.setAttribute("userLogin", users);
                                    response.sendRedirect("/sign-in");
                                } else {
                                    // Cập nhật thông tin người dùng trong session
                                    Users existingUser = this.usersService.findById(email).orElse(null);
                                    if (existingUser != null) {
                                        this.session.setAttribute("userLogin", existingUser);
                                    }
                                    response.sendRedirect("/index");
                                }
                            });
                })
                .logout(
                        logout -> logout.permitAll()
                );

        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}
