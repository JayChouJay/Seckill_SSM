package service.impl;

import cn.hutool.core.util.ObjectUtil;

import entity.SeckillOrder;
import mapper.ItemMapper;
import mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import util.RedisUtil;

import java.util.List;

@Component
@EnableScheduling
public class SeckillOrderTask {


    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    RedisUtil redis;

    @Scheduled(fixedRate = 1000 * 1)
    @Transactional
    public void runsecend() {
        System.out.println("********SeckillOrderTask job is ok******");

        // 获取数据库中所有未支付的订单
        List<SeckillOrder> list = orderMapper.getAllNotPaid();

        if (ObjectUtil.isEmpty(list) || list.size() == 0) {
            // 没有未支付订单，什么都不做
            return;
        }
        for (SeckillOrder sOrder : list) {
            // 根据返回结果，去reids中查询是否订单超时
            Object existOrder = redis.get("timeout_" + sOrder.getOrderCode());

            if (ObjectUtil.isEmpty(existOrder)) {
                // 说明订单超时，不可以在继续支付，更改数据库订单状态
                orderMapper.updateTimeout(sOrder);
                String key="stock_" + sOrder.getItemId();
                System.out.println(key);
                // redis库存+1
                redis.stockAdd(key);
                // mysql库存+1
                itemMapper.stockAdd(sOrder.getItemId());


            }
        }
    }

}
