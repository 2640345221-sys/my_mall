package my_mall.entity.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderDTO {
    private Long addressId;
    private List<Long> cartItemIds;
}
