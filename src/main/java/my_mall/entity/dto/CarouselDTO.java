package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CarouselDTO {
    private Long id;
    private Integer rank;
    private String url;
    private String redirectUrl;
}
