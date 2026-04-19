package my_mall.service;

import my_mall.entity.dto.IndexConfigDTO;
import my_mall.entity.dto.IndexPageDTO;
import my_mall.entity.po.IndexConfig;
import my_mall.result.PageResult;

import java.util.List;

public interface IndexConfigService {
    PageResult getPage(IndexPageDTO indexPageDTO);

    IndexConfig getById(Long id);

    void delete(List<Long> ids);

    void update(IndexConfigDTO indexConfigDTO);

    void insert(IndexConfigDTO indexConfigDTO);
}
