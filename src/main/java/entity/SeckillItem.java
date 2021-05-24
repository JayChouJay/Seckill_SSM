package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillItem {
    private Integer id;
    private String name;
    private Integer number;
    private BigDecimal price;
    private Date endTime;
    private Date startTime;
    private Date createTime;
}
