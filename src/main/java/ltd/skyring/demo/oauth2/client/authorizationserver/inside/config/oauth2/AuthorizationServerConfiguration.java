package ltd.skyring.demo.oauth2.client.authorizationserver.inside.config.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.annotation.Resource;
import java.security.KeyPair;

/**
 * @Copyright (C), 2017-2020,jASON
 * @Author: Tan xiang tian
 * @Date: 2020/10/29 16:06
 * @FileName: AuthorizationServerConfiguration
 * @Description: config
 * @Version: Eliza .1
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired
    private TokenStore tokenStore;


    @Resource(name = "keyProp")
    private KeyProperties keyProperties;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Bean
    public UserDetailsService userDetailsServiceBean() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager.createUser(User.withUsername("admin").password(new BCryptPasswordEncoder().encode("admin")).roles("admin").build());
        manager.createUser(User.withUsername("123").password(new BCryptPasswordEncoder().encode("123")).roles("user").build());

        return manager;
    }

    /**
     * ??????????????????
     *
     * @return ???????????????????????????
     */
    @Bean("keyProp")
    public KeyProperties keyProperties() {
        return new KeyProperties();
    }

    /**
     * ???????????????
     *
     * @param jwtAccessTokenConverter jwt???????????????
     * @return ???????????????
     */
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    /**
     * ?????????????????????
     *
     * @param clients ?????????????????????
     * @throws Exception ????????????
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.jdbc(this.dataSource).clients(this.clientDetails());
//        ?????????
        clients
                .inMemory()//?????????
                .withClient("myapp")//?????????id
                .resourceIds("resource01")
                .authorizedGrantTypes("password", "refresh_token")//????????????
                .scopes("all")//??????
                .authorities("ADMIN")
                .secret(passwordEncoder.encode("test"))//?????????code
                .accessTokenValiditySeconds(6666)
                .refreshTokenValiditySeconds(7777);
    }

    /**
     * jwt???????????????
     *
     * @param customUserAuthenticationConverter ????????????????????????
     * @return jwt???????????????
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(CustomUserAuthenticationConverter customUserAuthenticationConverter) {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        // ?????????,??????????????????????????????????????????????????????????????????,??????????????????RSA?????????????????????
        KeyPair keyPair = new KeyStoreKeyFactory
                (keyProperties.getKeyStore().getLocation(), keyProperties.getKeyStore().getSecret().toCharArray())
                .getKeyPair(keyProperties.getKeyStore().getAlias(), keyProperties.getKeyStore().getPassword().toCharArray());
        converter.setKeyPair(keyPair);

        DefaultAccessTokenConverter accessTokenConverter = (DefaultAccessTokenConverter) converter.getAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(customUserAuthenticationConverter);

        return converter;
    }

    /**
     * ??????????????????
     *
     * @param endpoints ????????????
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsServiceBean())
                .accessTokenConverter(jwtAccessTokenConverter)
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }


    /**
     * ??????????????????????????????
     *
     * @param oauthServer ?????????????????????
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                //??????????????????
                .allowFormAuthenticationForClients()
                //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                .passwordEncoder(new BCryptPasswordEncoder())
                .tokenKeyAccess("permitAll()")
                //??????token??????????????????????????????http basic??????
                .checkTokenAccess("isAuthenticated()");
    }
}