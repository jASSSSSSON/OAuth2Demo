package ltd.skyring.demo.oauth2.client.resourceserver.outside.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: jASON
 * @Date: 2022/03/15 10:13
 * @OriginFile: TestResourceApi
 */
@RestController
@RequestMapping("testResourceApi")
public class TestResourceApi {

    @PostMapping("testApi1")
    public String testApi1(){
        return "get it!";
    }

    @PostMapping("testApi2")
    public String testApi2(){
        return "get it!";
    }
}
