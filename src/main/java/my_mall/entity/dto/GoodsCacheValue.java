package my_mall.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import my_mall.entity.po.Goods;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//逻辑过期缓存包装：缓存里存 {过期时间, 真实数据}，过期后先返回旧数据再异步刷新，防止热点 key 击穿
public class GoodsCacheValue {
    private long expireAt;
    private List<Goods> data;

    public boolean isExpired() {
        return System.currentTimeMillis() > expireAt;
    }
}
