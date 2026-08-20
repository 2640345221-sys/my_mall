package my_mall.entity.vo;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
//用户端秒杀商品列表 VO：秒杀活动商品 + 商品基础信息
public class SeckillGoodsVO {
    private Long id;
    private Long goodsId;
    private String goodsName;
    private String coverImg;
    private Integer originalPrice;
    private Integer seckillPrice;
    private Integer stockCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
}
