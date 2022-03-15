package ltd.skyring.demo.oauth2.client.resourceserver.inside.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * @Copyright (C), 2017-2020,jASON
 * @Author: Tan xiang tian
 * @Date: 2020/10/30 15:41
 * @FileName: ResourceConfiguration
 * @Description: config.security
 * @Version: Eliza .1
 */

@Configuration
@EnableResourceServer
public class ResourceConfiguration extends ResourceServerConfigurerAdapter {

    private static final String SOURCE_ID = "resource01";

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * jwt令牌转换器
     *
     * @return jwt令牌转换器
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 测试用,资源服务使用相同的字符达到一个对称加密的效果,生产时候使用RSA非对称加密方式
        converter.setVerifierKey(getPubKey());
        DefaultAccessTokenConverter accessTokenConverter = (DefaultAccessTokenConverter) converter.getAccessTokenConverter();

        return converter;
    }

    /**
     * 获取公钥
     *
     * @return 公钥字符串
     */
    public static String getPubKey() {
        Resource resource = new ClassPathResource("public_key.txt");
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader br = new BufferedReader(inputStreamReader);
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException ioe) {
            return null;
        }
    }
    /**
     * 令牌发行库
     *
     * @param jwtAccessTokenConverter jwt令牌转换器
     * @return 令牌发行库
     */
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Override
    @CrossOrigin
    public void configure(ResourceServerSecurityConfigurer resources) throws IOException {
        resources.resourceId(SOURCE_ID).stateless(true);
        resources.tokenServices(defaultTokenServices());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        // 我们这里放开/order/*的请求，以/order/*开头的请求不用认证
        http
                .authorizeRequests()
                .antMatchers(
                        //自定义路径
                        "/order/*"
                        //工作路径
                        , "/login/**"
                        ,"/testResourceApi/testApi2"
                        //制作者信息
                        , "/actuator/info"
                        //swagger
                        , "/v2/api-docs"
                        , "/webjars/**"
                        , "/swagger-resources"
                        , "/swagger-resources/configuration/ui"
                        , "/swagger-resources/configuration/security"
                        , "/swagger-ui.html"
                        //特殊通道
                        , "/oauth/**"

                )
                .permitAll()
                .anyRequest()
                .authenticated();
        // @formatter:on
    }

    /**
     * 创建一个默认的资源服务token
     */
    @Bean
    public ResourceServerTokenServices defaultTokenServices() throws IOException {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        // 使用自定义的Token转换器
        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter);
        // 使用自定义的tokenStore
        defaultTokenServices.setTokenStore(tokenStore);
        return defaultTokenServices;
    }
}