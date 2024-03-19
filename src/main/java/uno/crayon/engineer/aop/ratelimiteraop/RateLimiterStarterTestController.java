package uno.crayon.engineer.aop.ratelimiteraop;

//import uno.crayon.engineer.ratelimiter.annotation.DoRateLimiter;
import uno.crayon.engineer2.ratelimiter.annotation.DoRateLimiter;
//import cn.bugstack.middleware.ratelimiter.annotation.DoRateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RateLimiterStarterTestController {

    /**
     * 测试：http://localhost:8081/api/queryUserInfo?userId=aaa
     */
    @DoRateLimiter(permitsPerSecond = 1, returnJson = "{\"code\":\"1111\",\"info\":\"调用方法超过最大次数，限流返回！\"}")
    @RequestMapping(path = "/api/queryUserInfo", method = RequestMethod.GET)
    public UserInfo queryUserInfo(@RequestParam String userId) throws InterruptedException {
        log.info("查询用户信息，userId：{}", userId);
        return new UserInfo("虫虫:" + userId, 19, "天津市东丽区万科赏溪苑14-0000");

    }
}