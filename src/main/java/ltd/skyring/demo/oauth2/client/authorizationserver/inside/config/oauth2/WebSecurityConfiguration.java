package ltd.skyring.demo.oauth2.client.authorizationserver.inside.config.oauth2;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * @Copyright (C), 2017-2020,jASON
 * @Author: Tan xiang tian
 * @Date: 2020/10/29 16:13
 * @FileName: SecurityConfiguration
 * @Description: config
 * @Version: Eliza .1
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    //采用bcrypt对密码进行编码
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers(
//                        //测试接口路径
//                        "/test**/**"
//
//                        //制作者信息
//                        , "/actuator/info"
//
//                        //工作路径
//                        , "/oauth/**"
////                        , "/login/**"
//                )
//                .permitAll()
//                .anyRequest()
//                .authenticated();
//        // @formatter:on
//    }

}