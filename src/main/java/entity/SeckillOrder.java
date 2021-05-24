package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SeckillOrder {
    private Integer id;
    private String orderCode;
    private Integer itemId;
    private Integer userId;
    //1 下单未支付
    //2 已支付
    private Integer state;
    private Date createTime;
}
