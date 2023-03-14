//package com.scu.ds.dfs.dfscoordinator.config;
//
//import com.scu.ds.dfs.dfscoordinator.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
//
//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableWebSecurity
//@EnableJdbcHttpSession
//@Configuration
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private UserService userService;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService((UserDetailsService) userService).passwordEncoder(passwordEncoder());
//    }
//
//    @Override
//    protected void configure(HttpSecurity httpSec) throws Exception {
////
////        httpSec.csrf()
////                .disable()
////                .logout()
////                .clearAuthentication(true)
////                .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout"))
////                .and()
////                .sessionManagement()
////                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
////                .and()
////                .authorizeRequests()
////                .antMatchers("/users/login").permitAll()
////                .antMatchers("/users/signup").permitAll()
////                .antMatchers("/api/api-docs**").permitAll()
////                .antMatchers("/api/swagger**").permitAll()
////                .anyRequest()
////                .authenticated();
//
//       // httpSec.csrf().disable().authorizeHttpRequests().
//    }
//
////
////
////
////    @Bean
////    public InMemoryUserDetailsManager userDetailsService() {
////        UserDetails user1 = User.withUsername("user1")
////                .password(passwordEncoder().encode("user1Pass"))
////                .roles("USER")
////                .build();
////        UserDetails user2 = User.withUsername("user2")
////                .password(passwordEncoder().encode("user2Pass"))
////                .roles("USER")
////                .build();
////        UserDetails admin = User.withUsername("admin")
////                .password(passwordEncoder().encode("adminPass"))
////                .roles("ADMIN")
////                .build();
////        return new InMemoryUserDetailsManager(user1, user2, admin);
////    }
////
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        // http builder configurations for authorize requests and form login
////        http.csrf()
////                .disable()
////                .authorizeHttpRequests().requestMatchers("/upload/*")
////                .permitAll()
////                // .hasRole("ADMIN")
////                .requestMatchers("/login*")
////                .permitAll()
////                .requestMatchers("/logout*")
////                .permitAll()
////                //.anyRequest()
////                //  .authenticated()
////                .and();
////
////        return http.build();
////    }
//
//}
