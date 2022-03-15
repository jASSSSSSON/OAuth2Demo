package ltd.skyring.demo.oauth2.client.resourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * @Author: jASON
 * @Date: 2022/03/14 16:42
 * @OriginFile: OAuth2Demo$ResourceServerApplication
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class OAuth2Demo$ResourceServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OAuth2Demo$ResourceServerApplication.class, args);
    }
}
