package controller;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.ItemService;
import service.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/seckill")
@SessionAttributes("mobile")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;

    @PostMapping("/login")
    public String login(String mobile, String pass,Model model) {
        if (mobile != null && !"".equals(mobile) && pass != null && !"".equals(pass)) {
            String loginResult = userService.login(mobile, pass);
            if (loginResult.equals("登录成功")) {
                model.addAttribute("mobile", mobile);
                return "forward:/seckill/seckill_list";
            } else {
                model.addAttribute("msg", loginResult);
            }
        } else {
            model.addAttribute("msg", "用户名和密码不能为空");
        }
        return "../../login";
    }

    @PostMapping("/regist")
    public String regist(String mobile, String pass, Model model) {
        //确保手机号合法
        if (StrUtil.isEmpty(mobile) || mobile.length() != 11) {
            model.addAttribute("msg", "电话号码格式不正确！");
        }
        else if (StrUtil.isEmpty(pass)) {
            //确保密码不为空
            model.addAttribute("msg", "密码为空请重新输入！");
        } else if (userService.getUser(mobile) != null) {
            model.addAttribute("msg", "手机号码已被注册！");
        } else {
            if (userService.regist(mobile, pass) != 0) {
                model.addAttribute("mobile", mobile);
                return "forward:/seckill/seckill_list";
            } else {
                model.addAttribute("msg", "系统故障请重试！");
            }
        }
        return "../../regist";
    }

    @RequestMapping("/seckill_list")
    public String toListPage(Model model) {
        model.addAttribute("items", itemService.getAll());
        return "seckill_list";
    }
    
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("mobile");
        return "../../login";
    }
//    @RequestMapping("/getUser/{id}")
//    @ResponseBody
//    public User test2(@PathVariable int id){
//        return userService.getUser(id);
//    }
}
