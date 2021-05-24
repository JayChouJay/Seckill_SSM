package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillUrl {
    private boolean enable;
    private String md5;
    private Integer seckillId;
    private Long now;
    private Long start;
    private Long end;
    public SeckillUrl(boolean enable, Integer seckillId) {
        this.enable = enable;
        this.seckillId = seckillId;
    }

    public SeckillUrl(boolean enable, Integer seckillId, Long now, Long start, Long end) {
        this.enable = enable;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }
}
