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
     * 读取密钥仓库
     *
     * @return 配置文件内密钥仓库
     */
    @Bean("keyProp")
    public KeyProperties keyProperties() {
        return new KeyProperties();
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

    /**
     * 客户端服务配置
     *
     * @param clients 客户端配置实体
     * @throws Exception 异常抛出
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.jdbc(this.dataSource).clients(this.clientDetails());
//        测试用
        clients
                .inMemory()//内存中
                .withClient("myapp")//客户端id
                .resourceIds("resource01")
                .authorizedGrantTypes("password", "refresh_token")//认证方式
                .scopes("all")//范围
                .authorities("ADMIN")
                .secret(passwordEncoder.encode("test"))//客户端code
                .accessTokenValiditySeconds(6666)
                .refreshTokenValiditySeconds(7777);
    }

    /**
     * jwt令牌转换器
     *
     * @param customUserAuthenticationConverter 自定义认证转换器
     * @return jwt令牌转换器
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(CustomUserAuthenticationConverter customUserAuthenticationConverter) {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        // 测试用,资源服务使用相同的字符达到一个对称加密的效果,生产时候使用RSA非对称加密方式
        KeyPair keyPair = new KeyStoreKeyFactory
                (keyProperties.getKeyStore().getLocation(), keyProperties.getKeyStore().getSecret().toCharArray())
                .getKeyPair(keyProperties.getKeyStore().getAlias(), keyProperties.getKeyStore().getPassword().toCharArray());
        converter.setKeyPair(keyPair);

        DefaultAccessTokenConverter accessTokenConverter = (DefaultAccessTokenConverter) converter.getAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(customUserAuthenticationConverter);

        return converter;
    }

    /**
     * 授权端点配置
     *
     * @param endpoints 端点实体
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
     * 授权服务器的安全配置
     *
     * @param oauthServer 认证服务器实体
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                //允许表单登录
                .allowFormAuthenticationForClients()
                //外部密码进来时的加密方式，需与数据库内的密码加密方式相同，且必须提供一种加密方式
                .passwordEncoder(new BCryptPasswordEncoder())
                .tokenKeyAccess("permitAll()")
                //校验token需要认证通过，可采用http basic认证
                .checkTokenAccess("isAuthenticated()");
    }
}