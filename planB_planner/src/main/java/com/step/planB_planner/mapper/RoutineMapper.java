package com.step.planB_planner.mapper;

import com.step.planB_planner.model.dto.request.RoutineRequestDto;
import com.step.planB_planner.model.dto.response.RoutineResponseDto;
import com.step.planB_planner.model.entity.Routine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Daria Pevets
 **/
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RoutineMapper {

    Routine mapRoutineRequestDtoToRoutine(RoutineRequestDto routineRequestDto);

    RoutineResponseDto mapRoutineToRoutineResponseDto(Routine routine);
}
