package mapper;

import entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;

public interface UserMapper {
    User getUserById(int id);
    User getUserByMobile(String mobile);
    int addUser(@Param("mobile") String mobile, @Param("password") String password);

}
