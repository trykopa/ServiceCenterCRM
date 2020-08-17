package com.googe.ssadm.sc.mappers;

import com.googe.ssadm.sc.dto.NewsDTO;
import com.googe.ssadm.sc.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface NewsMapper {
    @Mapping(source = "createdAt", target = "created")

    NewsDTO toNewsDTO(News news);
    News toNews(NewsDTO newsDTO);
}
