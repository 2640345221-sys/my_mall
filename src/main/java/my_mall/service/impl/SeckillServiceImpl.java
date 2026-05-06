package my_mall.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.dto.SeckillOrderDTO;
import my_mall.service.CommonService;
import my_mall.service.SeckillService;
import my_mall.utils.TLUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j

public class SeckillServiceImpl implements SeckillService {
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private CommonService commonService;
    public Integer seckillWork(SeckillOrderDTO seckillOrderDTO) {
        Long userId= TLUtils.getUserId();
        Long seckillGoodsId= seckillOrderDTO.getSeckillGoodsId();
        String  stockKey="seckill:stock:"+seckillGoodsId;
        String userKey="seckill:user:"+seckillGoodsId+":"+userId;

        log.info(stockKey);
        log.info(String.valueOf(seckillGoodsId));
        log.info(String.valueOf(userId));
        log.info(userKey);

        String stockStr = redisTemplate.opsForValue().get(stockKey);
        if(stockStr == null){
            return -1;
        }
        Integer stockValue = Integer.parseInt(stockStr);
        if(stockValue <= 0){
            return -1;
        }
        if(Boolean.TRUE.equals(redisTemplate.hasKey(userKey))){
            return -2;
        }

        RLock lock =redissonClient.getLock("seckill:lock:"+seckillGoodsId);
        boolean locked=false;
        try{
            locked=lock.tryLock(0,10, TimeUnit.SECONDS);
            if(!locked){
                return -3;
            }
            String currentStockStr = redisTemplate.opsForValue().get(stockKey);
            if(currentStockStr == null){
                return -1;
            }
            Integer currentStock = Integer.parseInt(currentStockStr);
            if(currentStock <= 0){
                return -1;
            }
            if(Boolean.TRUE.equals(redisTemplate.hasKey(userKey))){
                return -2;
            }

            Long newStock=redisTemplate.opsForValue().decrement(stockKey,seckillOrderDTO.getCount());
            if(newStock<0){
                redisTemplate.opsForValue().increment(stockKey,seckillOrderDTO.getCount());
                return -1;
            }
            redisTemplate.opsForValue().set(userKey,"1", Duration.ofHours(1));
            commonService.createOrderAndReduceDbStock(seckillOrderDTO);
            return 1;
        }catch (Exception e){
            log.error("秒杀处理异常",e);
            redisTemplate.opsForValue().increment(stockKey);
            redisTemplate.delete(userKey);
            return -3;
        }finally {
            if(locked&&lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }
}
