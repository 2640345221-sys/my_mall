package my_mall.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.config.RabbitMQConfig;
import my_mall.entity.dto.SeckillMessage;
import my_mall.entity.dto.SeckillOrderDTO;
import my_mall.enums.SeckillResultEnum;
import my_mall.service.SeckillService;
import my_mall.utils.TLUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SeckillServiceImpl implements SeckillService {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public SeckillResultEnum seckillWork(SeckillOrderDTO seckillOrderDTO) {
        Long userId = TLUtils.getUserId();
        SeckillMessage message = new SeckillMessage();
        message.setUserId(userId);
        message.setSeckillGoodsId(seckillOrderDTO.getSeckillGoodsId());
        message.setAddressId(seckillOrderDTO.getAddressId());
        message.setCount(seckillOrderDTO.getCount());
        rabbitTemplate.convertAndSend(RabbitMQConfig.SECKILL_REQUEST_QUEUE, message);
        log.info("秒杀请求已入队: userId={}, seckillGoodsId={}", userId, seckillOrderDTO.getSeckillGoodsId());
        return SeckillResultEnum.SUCCESS;
    }
}
