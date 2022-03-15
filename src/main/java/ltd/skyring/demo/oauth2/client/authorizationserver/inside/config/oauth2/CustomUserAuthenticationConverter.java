package ltd.skyring.demo.oauth2.client.authorizationserver.inside.config.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Copyright (C), 2017-2020,jASON
 * @Author: Tan xiang tian
 * @Date: 2020/10/31 10:22
 * @FileName: CustomUserAuthenticationConverter
 * @Description: config.converter 对返回的令牌进行数据填充
 * @Version: Eliza .1
 */
@Component
public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        LinkedHashMap response = new LinkedHashMap();
        String name = authentication.getName();
        response.put("user_name", name);

        Object principal = authentication.getPrincipal();

        fullInByJwt(response,"test");

        return response;
    }

    private LinkedHashMap fullInByJwt(LinkedHashMap source,Object data){
        LinkedHashMap response=source;

        response.put("o",data);

        return response;
    }


}