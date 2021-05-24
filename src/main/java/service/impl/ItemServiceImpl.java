package service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import entity.SeckillItem;
import entity.SeckillOrder;
import entity.SeckillUrl;
import mapper.ItemMapper;
import mapper.OrderMapper;
import mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.ItemService;
import util.RedisUtil;

import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    ItemMapper mapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    RedisUtil redis;

    public List<SeckillItem> getAll() {
        return mapper.getAll();
    }

    public SeckillItem selectOne(Integer id) {
        if (ObjectUtil.isEmpty(id) || id <= 0) {
            return null;
        }
        return mapper.selectOneById(id);
    }


    public SeckillUrl getSeckillURL(Integer id) {
        //从redis中获取秒杀对象
        SeckillItem item = (SeckillItem) redis.get(String.valueOf(id));
        //判断对象是否存在，不存在从数据库中获取
        if (ObjectUtil.isEmpty(item)) {
            item = mapper.selectOneById(id);
            redis.set(String.valueOf(id), item);
            redis.set("stock_"+id,item.getNumber());
        }
        //数据库中有就返回正确的SeckillUrl对象
        //据库中没有就返回错误
        if (ObjectUtil.isEmpty(item)) {
            return new SeckillUrl(false, id);
        } else {
            Long startTime = item.getStartTime().getTime();
            Long endTime = item.getEndTime().getTime();
            Long now = new Date().getTime();
            if (startTime > now || endTime < now) {
                return new SeckillUrl(false, id, now, startTime, endTime);
            } else {
                String md5 = getMd5(id);
                return new SeckillUrl(true, md5, id, now, startTime, endTime);
            }
        }
    }

    private final String mixKey = "aka";

    private String getMd5(Integer id) {
        return SecureUtil.md5(id + "," + mixKey);
    }

    public boolean verifySeckillMD5(Integer id, String md5) {
        String sMD5 = getMd5(id);
        if (ObjectUtil.isEmpty(md5) || md5.equals(sMD5)) {
            return false;
        }
        return true;
    }

    public boolean executeSeckill(String mobile, Integer seckillId) {
        String key = mobile + "_" + seckillId;
        Integer mSeckillId = (Integer) redis.get(key);
        if (!ObjectUtil.isEmpty(mSeckillId)) {
            return false;
        } else {
            redis.setex(key, seckillId, 60 * 5);
            // TODO 减库存 lua脚本
            // -1 库存不足
            // -2 不存在
            // 整数是正常操作，减库存成功
            Integer result = redis.stockDecr("stock_" + seckillId);
            if (ObjectUtil.isEmpty(result)) {
                // 脚本执行错误
                return false;
            }
            if (result == -1) {
                // 库存不足
                return false;
            }
            if (result == -2) {
                // key不存在
                return false;
            }
            return true;
        }
    }

    /**
     * 下订单
     */
    @Transactional
    public SeckillOrder createOrder(String mobile, int seckillId) {
        // 数据库更新库存
        mapper.updateStock(seckillId);

        // result-->number
        // 更新SeckillItem的库存

        // lua脚本{
        //      1.获取KEY对应value
        //      2.if判断是否库存大于0
        //      3.更新redis中key的库存 -1
        //      4.string类型转换
        // }
        // 1.先去redis里面查库存
        // 2.判断库存是否可以下单（大于0）
        // 3.商品表，订单表，同时需要操作 事物 保证2个操作的一致
        // 4.减库存，下订单

        // TODO 订单生成逻辑

        String simpleUUID = IdUtil.simpleUUID();

        // 生成秒杀订单并保存
        SeckillOrder order = new SeckillOrder(null, simpleUUID, seckillId, userMapper.getUserByMobile(mobile).getId(), 1, new Date());


        orderMapper.insert(order);


        // 保存到redis中，设置订单的超时时间5分钟
        // key == timeout_uuid
//        redisDao.setex("timeout_" + seckillOrder.getOrderCode(), seckillOrder, 60 * 5);
        redis.setex("timeout_" + order.getOrderCode(), order, 15);

        return order;

    }

    /**
     * 支付
     *
     * @param orderCode
     */
    public void pay(String orderCode) {
        orderMapper.pay(orderCode);
    }

    public SeckillOrder getSeckillOrder(String orderCode) {
        return orderMapper.getSeckillOrder(orderCode);
    }


}
