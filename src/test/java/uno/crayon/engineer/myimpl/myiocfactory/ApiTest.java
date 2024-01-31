package uno.crayon.engineer.myimpl.myiocfactory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApiTest {
    @Test
    public void test_BeanFactory() {
        //1.创建bean工厂(同时完成了加载资源、创建注册单例bean注册器的操作)
        BeanFactory beanFactory = new BeanFactory();

        //2.第一次获取bean（通过反射创建bean，缓存bean）
        MyPhoneBean myPhoneBean = (MyPhoneBean) beanFactory.getBean("myPhoneBean");
        myPhoneBean.queryPhoneInfo();

        //3.第二次获取bean（从缓存中获取bean）
        MyPhoneBean myPhoneBean2 = (MyPhoneBean) beanFactory.getBean("myPhoneBean");
        myPhoneBean2.queryPhoneInfo();
    }
}
