package my_mall.entity.annotation;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OperationLogPO {
    private String module;
    private String type;
    private String description;
    private Boolean recordParams;
    private Boolean recordResult;
    private Long executionTime;
    private String errorMessage;
    private String params;
    private String result;
    private Long userId;
    private LocalDateTime createTime;
}
