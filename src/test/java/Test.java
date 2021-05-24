import entity.SeckillOrder;
import mapper.OrderMapper;
import mapper.UserMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-*.xml"})
public class Test {
    @Autowired
    UserMapper mapper;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    OrderMapper orderMapper;

    public static void main(String[] args) {
        System.out.println(new Test().mapper.getUserById(1));
//        new SqlSessionFactoryBuilder().build;
    }

    @org.junit.Test
    public void t1() {
//        System.out.println(mapper.addUser("18258511614","202cb962ac59075b964b07152d234b70"));

//        redisTemplate.opsForValue().set("1", "2");
//        redisTemplate.opsForValue().get("1");

//        SeckillOrder order=new SeckillOrder(null,99,99,99,new Date());
//        orderMapper.insert(order);
    }

}
