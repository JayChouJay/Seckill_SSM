package controller;

import cn.hutool.core.util.ObjectUtil;
import entity.ResponseResult;
import entity.SeckillItem;
import entity.SeckillOrder;
import entity.SeckillUrl;
import exception.SeckillException;
import mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import service.ItemService;

import java.util.Date;

@Controller
@RequestMapping("/seckill")
public class ItemController {
    @Autowired
    ItemService itemService;

    @GetMapping("/detail/{id}")
    public ModelAndView detail(@PathVariable Integer id) {
        ModelAndView mv = new ModelAndView();
        SeckillItem item = itemService.selectOne(id);
        if (ObjectUtil.isEmpty(item)) {
            throw new SeckillException("商品不存在！");
        }
        mv.addObject("item", item);
        mv.setViewName("detail");
        return mv;
    }

    @GetMapping("/now")
    @ResponseBody
    public ResponseResult now() {
        return new ResponseResult(true, new Date().getTime(), "系统当前时间");
    }

    @PostMapping("/getURL/{id}")
    @ResponseBody
    public ResponseResult<SeckillUrl> getURL(@PathVariable Integer id) {
        ResponseResult<SeckillUrl> result = new ResponseResult<SeckillUrl>();
        SeckillUrl url = itemService.getSeckillURL(id);
        result.setData(url);
        result.setStatus(true);
        result.setMessage("ok");
        return result;
    }

    @GetMapping("/execute/{seckillId}/{md5}")
    public String executeSeckill(@PathVariable Integer seckillId, @PathVariable String md5,@SessionAttribute String mobile) {
        System.out.println(mobile);
        if (itemService.verifySeckillMD5(seckillId, md5)) {
            throw new SeckillException("请求的URL,MD5验证不通过");
        }
        boolean success = itemService.executeSeckill(mobile, seckillId);
        // 减库存，redis做并发减库存操作
        // 下订单
        if (!success) {
//            result.setSuccess(false);
//            result.setMessage("order fail");
//            return result;

            throw new SeckillException("下单失败");
        }
        SeckillOrder seckillOrder = itemService.createOrder(mobile, seckillId);
        return "redirect:/seckill/orderPay?orderCode=" + seckillOrder.getOrderCode();
    }
    @PostMapping("/pay")
    @ResponseBody
    public String pay(String orderCode) {

        // 超时订单不能支付
        SeckillOrder seckillOrder = itemService.getSeckillOrder(orderCode);

        if (seckillOrder.getState() == 4) {
            return "order expire";
        }

        // 重复支付，需要判断，如果已经支付成功，不要在跳转支付宝或微信


        // 支付验证
        itemService.pay(orderCode);

        return "pay ok";
    }

    @RequestMapping("/orderPay")
    public String orderPay(String orderCode, Model model) {

        SeckillOrder seckillOrder = itemService.getSeckillOrder(orderCode);
        System.out.println("====================="+seckillOrder);
        model.addAttribute("seckillOrder", seckillOrder);
        model.addAttribute("item", itemService.selectOne(seckillOrder.getItemId()));
        // 每次请求订单，时间固定的
        model.addAttribute("orderTime", seckillOrder.getCreateTime());

        return "order";
    }
}
