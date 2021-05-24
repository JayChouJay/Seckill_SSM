package mapper;

import entity.SeckillItem;

import java.util.List;

public interface ItemMapper {
    List<SeckillItem> getAll();

    SeckillItem selectOneById(Integer id);

    void updateStock(int seckillId);

    void stockAdd(int id);
}
