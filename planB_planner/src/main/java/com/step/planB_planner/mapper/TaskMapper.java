package com.step.planB_planner.mapper;

import com.step.planB_planner.model.dto.request.TaskRequestDto;
import com.step.planB_planner.model.dto.response.TaskResponseDto;
import com.step.planB_planner.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Optional;

/**
 * @author Daria Pevets
 **/
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TaskMapper {

    Task mapTaskRequestDtoToTask(TaskRequestDto taskRequestDto);

    TaskResponseDto mapTaskToTaskResponseDto(Task task);

    List<TaskResponseDto> mapTaskListToTaskResponseDtoList(List<Task> tasks);

}
