package com.step.planB_planner.service.impl;

import com.step.planB_planner.exception.IllegalTimeArgument;
import com.step.planB_planner.exception.TaskException;
import com.step.planB_planner.mapper.TaskMapper;
import com.step.planB_planner.model.constant.AppStatus;
import com.step.planB_planner.model.constant.TaskStatus;
import com.step.planB_planner.model.dto.request.TaskRequestDto;
import com.step.planB_planner.model.dto.response.TaskResponseDto;
import com.step.planB_planner.model.entity.Task;
import com.step.planB_planner.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author Daria Pevets
 **/
@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Nested
    class CreateTaskTests {

        @Test
        void createTask() {
            TaskRequestDto requestDto = new TaskRequestDto(
                    "name",
                    "desc",
                    LocalDateTime.now().plusDays(1)
            );

            Task task = new Task();

            TaskResponseDto responseDto = new TaskResponseDto(
                    1L,
                    "name",
                    "desc",
                    LocalDateTime.now().plusDays(1),
                    TaskStatus.NOT_STARTED
            );

            when(taskRepository.findTaskByName(requestDto.name())).thenReturn(Optional.empty());
            when(taskRepository.findTaskByTime(requestDto.time())).thenReturn(Optional.empty());
            when(taskMapper.mapTaskRequestDtoToTask(requestDto)).thenReturn(task);
            when(taskMapper.mapTaskToTaskResponseDto(task)).thenReturn(responseDto);

            TaskResponseDto result = taskService.createTask(requestDto);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(responseDto, result);
            Assertions.assertEquals(TaskStatus.NOT_STARTED, result.status());
            verify(taskRepository, times(1)).save(task);
        }

        @Test
        void createTaskNameExists() {

            TaskRequestDto requestDto = new TaskRequestDto(
                    "name",
                    "desc",
                    LocalDateTime.now().plusDays(1)
            );

            when(taskRepository.findTaskByName(requestDto.name())).thenReturn(Optional.of(new Task()));

            TaskException exception = Assertions.assertThrows(TaskException.class,
                    () -> taskService.createTask(requestDto));
            Assertions.assertEquals("Task with name " + requestDto.name() + " already exists", exception.getMessage());

        }

        @Test
        void createTaskTimeExists() {

            TaskRequestDto requestDto = new TaskRequestDto(
                    "name",
                    "desc",
                    LocalDateTime.now().plusDays(1)
            );

            when(taskRepository.findTaskByTime(requestDto.time())).thenReturn(Optional.of(new Task()));

            TaskException exception = Assertions.assertThrows(TaskException.class,
                    () -> taskService.createTask(requestDto));
            Assertions.assertEquals("Task with time " + requestDto.time() + " already exists", exception.getMessage());

        }

        @Test
        void createTaskStartBeforeNow() {

            TaskRequestDto requestDto = new TaskRequestDto(
                    "name",
                    "desc",
                    LocalDateTime.now().minusDays(1)
            );

            IllegalTimeArgument exception = Assertions.assertThrows(IllegalTimeArgument.class,
                    () -> taskService.createTask(requestDto));

            Assertions.assertEquals("Еще вчера задачки к сожалению не делаются...(", exception.getMessage());
        }
    }

    @Nested
    class UpdateTaskTests {

        @Test
        void updateTask() {

            Long taskId = 1L;
            TaskRequestDto requestDto = new TaskRequestDto(
                    "name",
                    "desc",
                    LocalDateTime.now().plusDays(1)
            );

            Task task = new Task();

            TaskResponseDto responseDto = new TaskResponseDto(
                    1L,
                    "name",
                    "desc",
                    LocalDateTime.now().plusDays(1),
                    TaskStatus.NOT_STARTED
            );

            when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(task));
            when(taskMapper.mapTaskToTaskResponseDto(task)).thenReturn(responseDto);

            TaskResponseDto result = taskService.updateTask(taskId, requestDto);

            Assertions.assertNotNull(result);
            Assertions.assertEquals("name", task.getName());
            Assertions.assertEquals("desc", task.getDescription());

            verify(taskRepository, times(1)).save(task);
        }

        @Test
        void updateTaskNotFound() {

            Long taskId = 1L;

            TaskRequestDto requestDto = new TaskRequestDto(
                    "name",
                    "desc",
                    LocalDateTime.now().plusDays(1)
            );

            when(taskRepository.findTaskById(taskId)).thenReturn(Optional.empty());
            TaskException exception = Assertions.assertThrows(TaskException.class,
                    () -> taskService.updateTask(taskId, requestDto));

            Assertions.assertEquals("Task with id " + taskId + " does not exist", exception.getMessage());

        }

        @Test
        void updateTaskStartBeforeNow() {
            Long taskId = 1L;

            TaskRequestDto requestDto = new TaskRequestDto(
                    "name",
                    "desc",
                    LocalDateTime.now().minusDays(1)
            );

            Task task = new Task();

            when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(task));

            Assertions.assertThrows(IllegalTimeArgument.class,
                    () -> taskService.updateTask(taskId, requestDto));
        }

        @Test
        void changeTaskStatus() {

            Long taskId = 1L;
            TaskStatus newStatus = TaskStatus.IN_PROGRESS;

            Task task = new Task();
            task.setStatus(TaskStatus.NOT_STARTED);

            when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(task));
            TaskStatus result = taskService.changeTaskStatus(taskId, newStatus);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(newStatus, result);
        }

        @Test
        void changeTaskStatusNotFound() {

            Long taskId = 1L;

            when(taskRepository.findTaskById(taskId)).thenReturn(Optional.empty());

            assertThrows(TaskException.class,
                    () -> taskService.changeTaskStatus(taskId, TaskStatus.NOT_STARTED));

        }
    }

    @Nested
    class DeleteTaskTests {

        @Test
        void deleteTask() {

            Long taskId = 1L;
            Task task = new Task();
            when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(task));

            AppStatus result = taskService.deleteTask(taskId);

            Assertions.assertEquals(AppStatus.SUCCESS, result);
            verify(taskRepository, times(1)).deleteById(taskId);
        }

        @Test
        void deleteTaskNotFound() {
            Long taskId = 1L;

            when(taskRepository.findTaskById(taskId)).thenReturn(Optional.empty());
            assertThrows(TaskException.class,
                    () -> taskService.deleteTask(taskId));
        }
    }

    @Nested
    class GetTaskTests {

        @Test
        void getTasksByRange() {

            LocalDateTime start = LocalDateTime.now().plusDays(1);
            LocalDateTime end = LocalDateTime.now().plusDays(2);
            int page = 0;
            int pageSize = 10;

            Pageable pageable = PageRequest.of(page, pageSize, Sort.by("time").descending());
            List<Task> mockList = Collections.singletonList(new Task());
            Page<Task> mockPage = new PageImpl<>(mockList, pageable, mockList.size());

            TaskResponseDto responseDto = new TaskResponseDto(
                    1L,
                    "name",
                    "desc",
                    LocalDateTime.now().plusDays(1),
                    TaskStatus.NOT_STARTED
            );

            when(taskRepository.findTaskByTimeBetween(start, end, pageable)).thenReturn(mockPage);
            when(taskMapper.mapTaskToTaskResponseDto(any(Task.class))).thenReturn(responseDto);

            Page<TaskResponseDto> result = taskService.getTasksByRange(start, end, page, pageSize);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(1, result.getTotalElements());
            Assertions.assertEquals(1, result.getContent().size());
        }
    }
}