package com.step.planB_planner.mapper;

import com.step.planB_planner.model.dto.request.DeadlineRequestDto;
import com.step.planB_planner.model.dto.response.DeadlineResponseDto;
import com.step.planB_planner.model.entity.Deadline;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Daria Pevets
 **/
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DeadlineMapper {

    Deadline mapDeadlineRequestDtoToDeadline(DeadlineRequestDto deadlineRequestDto);

    DeadlineResponseDto mapDeadlineToDeadlineResponseDto(Deadline deadline);
}
