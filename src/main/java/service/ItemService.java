package service;

import entity.SeckillItem;
import entity.SeckillOrder;
import entity.SeckillUrl;

import java.util.List;

public interface ItemService {
    List<SeckillItem> getAll();

    SeckillItem selectOne(Integer id);

    SeckillUrl getSeckillURL(Integer id);

    boolean verifySeckillMD5(Integer id, String md5);

    boolean executeSeckill(String mobile, Integer seckillId);

    SeckillOrder createOrder(String mobile, int seckillId);

    void pay(String orderCode);

    SeckillOrder getSeckillOrder(String orderCode);
}
