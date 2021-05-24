package service.impl;

import cn.hutool.crypto.SecureUtil;
import entity.User;
import mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper mapper;

    public User getUser(int id) {
        return mapper.getUserById(id);
    }

    public User getUser(String mobile) {
        return mapper.getUserByMobile(mobile);
    }

    public int regist(String mobile, String pass) {
        pass=SecureUtil.md5(pass);
        System.out.println(mobile+"----"+pass);
        return mapper.addUser(mobile, pass);
    }

    public String login(String mobile, String pass) {
        User user = getUser(mobile);
        if (user != null) {
            if (SecureUtil.md5(pass).equals(user.getPassword())){
                return "登录成功";
            }else {
                return "用户名或密码不正确";
            }
        }else {
            return "号码未注册";
        }
    }
}
