package com.vandev.manage.mapper;

import com.vandev.manage.dto.ScoreDTO;
import com.vandev.manage.pojo.Score;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface ScoreMapper {
    ScoreDTO toDTO(Score score);
    Score toEntity(ScoreDTO scoreDTO);
}
