package uno.crayon.engineer.aop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 */
@RestController
public class AopTestController {

    @GetMapping("/aop")
    @MyFirstAop()
    public String test() {
        return "HELLO";
    }
}
