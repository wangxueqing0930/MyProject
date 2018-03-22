import com.wxq.example.dao.UserMapper;
import com.wxq.example.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)//表示整合JUnit4进行测试
@ContextConfiguration(locations={"classpath:spring/applicationContext-main.xml"})//加载spring配置文件
public class TestUser {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void test01(){
        User user = new User();
        user.setName("1111111");
        user.setSex((byte)1);
        String timeStr = "2018-03-15 00:00:00";
        SimpleDateFormat fomat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date time= fomat.parse(timeStr);
            user.setTime(time);
        }catch(ParseException e){
            e.printStackTrace();
        }
        User user1 = new User();
        user1.setName("222222222");
        user1.setSex((byte)0);
        List<User> list = new ArrayList<>();
        list.add(user);
        list.add(user1);
        userMapper.batchInsert(list);
        String id = list.get(0).getId()+"";
        System.out.println(id);
    }
}
