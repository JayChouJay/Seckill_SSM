package service;

import entity.User;

public interface UserService {
    User getUser(int id);
    User getUser(String mobile);
    int regist(String mobile,String pass);

    String login(String mobile, String pass);
}
