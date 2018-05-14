import com.wxq.example.model.User;
import com.wxq.example.service.UserService;
import com.wxq.example.service.impl.UserServiceImpl;
import com.wxq.example.util.BeanUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Administrator on 2018/5/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)//表示整合JUnit4进行测试
@ContextConfiguration(locations={"classpath:spring/applicationContext-main.xml"})//加载spring配置文件
public class TestBean {
    @Test
    public void testBeanByClass() {
        UserService userService = BeanUtil.getBean(UserService.class);
        User user = userService.getUserById(1);
        System.out.println(user.getName());
    }

    @Test
    public void testBeanByName() {
        UserService userService = (UserService) BeanUtil.getBean("userServiceImpl");
        User user = userService.getUserById(1);
        System.out.println(user.getName());
    }
}
