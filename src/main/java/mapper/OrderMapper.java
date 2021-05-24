package mapper;

import entity.SeckillItem;
import entity.SeckillOrder;

import java.util.List;

public interface OrderMapper {
   void insert(SeckillOrder order);

   List<SeckillOrder> getAllNotPaid();

   void updateTimeout(SeckillOrder seckillOrder);

   void pay(String orderCode);

   SeckillOrder getSeckillOrder(String orderCode);
}
