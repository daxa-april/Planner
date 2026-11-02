package com.step.planB_planner.service.impl;

import com.step.planB_planner.exception.IllegalTimeArgument;
import com.step.planB_planner.exception.TaskException;
import com.step.planB_planner.mapper.DeadlineMapper;
import com.step.planB_planner.model.constant.AppStatus;
import com.step.planB_planner.model.constant.TaskStatus;
import com.step.planB_planner.model.dto.request.DeadlineRequestDto;
import com.step.planB_planner.model.dto.response.DeadlineResponseDto;
import com.step.planB_planner.model.entity.Deadline;
import com.step.planB_planner.repository.DeadlineRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Daria Pevets
 **/
@ExtendWith(MockitoExtension.class)
class DeadlineServiceImplTest {

    @Mock
    private DeadlineRepository deadlineRepository;

    @Mock
    private DeadlineMapper deadlineMapper;

    @InjectMocks
    private DeadlineServiceImpl deadlineService;

    @Nested
    class AddDeadlineTests {

        @Test
        void addDeadline() {
            DeadlineRequestDto request = new DeadlineRequestDto(
                    "Task", "Desc", LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(2));
            Deadline deadline = new Deadline();
            DeadlineResponseDto responseDto = new DeadlineResponseDto(
                    1L,
                    "Название",
                    "Описание",
                    LocalDateTime.now(),
                    LocalDateTime.now().plusHours(1),
                    TaskStatus.NOT_STARTED
            );

            when(deadlineRepository.getDeadlineByName(request.name())).thenReturn(Optional.empty());
            when(deadlineRepository.getDeadlineByWhenGot(request.whenGot())).thenReturn(Optional.empty());
            when(deadlineMapper.mapDeadlineRequestDtoToDeadline(request)).thenReturn(deadline);
            when(deadlineMapper.mapDeadlineToDeadlineResponseDto(deadline)).thenReturn(responseDto);

            DeadlineResponseDto result = deadlineService.addDeadline(request);
            assertNotNull(result);
            assertEquals(TaskStatus.NOT_STARTED, deadline.getStatus());
            verify(deadlineRepository, times(1)).save(deadline);
        }

        @Test
        void addDeadlineEndBeforeNow() {

            DeadlineRequestDto request = new DeadlineRequestDto(
                    "Task", "Desc", LocalDateTime.now(),
                    LocalDateTime.now().minusDays(1));
            IllegalTimeArgument exception = assertThrows(IllegalTimeArgument.class,
                    () -> deadlineService.addDeadline(request));
            assertEquals("Я понимаю, что задача должна быть выполнена еще вчера, но будь реалистом!", exception.getMessage());
        }

        @Test
        void addDeadlineWhenGotAfterEnd() {

            DeadlineRequestDto request = new DeadlineRequestDto(
                    "Task", "Desc", LocalDateTime.now().plusDays(3),
                    LocalDateTime.now().plusDays(2));
            IllegalTimeArgument exception = assertThrows(IllegalTimeArgument.class,
                    () -> deadlineService.addDeadline(request));
            assertEquals("Совсем сдурел?", exception.getMessage());
        }

        @Test
        void addDeadlineNameExists() {

            DeadlineRequestDto request = new DeadlineRequestDto(
                    "Duplicate", "Desc", LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(2));
            when(deadlineRepository.getDeadlineByName(request.name())).
                    thenReturn(Optional.of(new Deadline()));
            TaskException exception = assertThrows(TaskException.class,
                    () -> deadlineService.addDeadline(request));
            assertEquals("А ты очень забывчив(а) однако!", exception.getMessage());
        }

        @Test
        void addDeadlineWhenGotExists() {

            DeadlineRequestDto request = new DeadlineRequestDto(
                    "Task", "Desc", LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(2));
            when(deadlineRepository.getDeadlineByName(request.name())).
                    thenReturn(Optional.empty());
            when(deadlineRepository.getDeadlineByWhenGot(request.whenGot())).
                    thenReturn(Optional.of(new Deadline()));

            TaskException exception = assertThrows(TaskException.class,
                    () -> deadlineService.addDeadline(request));
            assertEquals("Может таблеточки попить?", exception.getMessage());
        }
    }

    @Nested
    class UpdateDeadlineTests {

        @Test
        void updateDeadlineSuccess() {
            // Arrange
            Long id = 1L;
            DeadlineRequestDto request = new DeadlineRequestDto(
                    "New Name", "New Desc",
                    LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3));
            Deadline existingDeadline = new Deadline();
            DeadlineResponseDto responseDto = new DeadlineResponseDto(
                    1L,
                    "Название",
                    "Описание",
                    LocalDateTime.now().plusHours(1),
                    LocalDateTime.now().plusHours(2),
                    TaskStatus.NOT_STARTED
            );

            when(deadlineRepository.getDeadlineById(id)).thenReturn(Optional.of(existingDeadline));
            when(deadlineMapper.mapDeadlineToDeadlineResponseDto(existingDeadline)).
                    thenReturn(responseDto);

            DeadlineResponseDto result = deadlineService.updateDeadline(id, request);

            assertNotNull(result);
            assertEquals("New Name", existingDeadline.getName());
            assertEquals("New Desc", existingDeadline.getDescription());
            assertEquals(request.theEnd(), existingDeadline.getTheEnd());
            verify(deadlineRepository, times(1)).save(existingDeadline);
        }

        @Test
        void updateDeadlineEndBeforeNow() {

            Long id = 1L;
            DeadlineRequestDto request = new DeadlineRequestDto(
                    "Name", "Desc", LocalDateTime.now(),
                    LocalDateTime.now().minusDays(1));
            when(deadlineRepository.getDeadlineById(id)).thenReturn(Optional.of(new Deadline()));

            IllegalTimeArgument exception = assertThrows(IllegalTimeArgument.class,
                    () -> deadlineService.updateDeadline(id, request));
            assertEquals("Не пытайся обмануть меня, человек!", exception.getMessage());
        }

        @Test
        void updateDeadlineNotFound() {

            Long id = 1L;
            DeadlineRequestDto request = new DeadlineRequestDto(
                    "Name", "Desc", LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1));
            when(deadlineRepository.getDeadlineById(id)).thenReturn(Optional.empty());

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> deadlineService.updateDeadline(id, request));
            assertEquals("Specified theEnd does not exist!", exception.getMessage());
        }
    }

    @Nested
    class UpdateStatusAndDeleteDeadlineTests {

        @Test
        void updateDeadlineStatusSuccess() {

            Long id = 1L;
            TaskStatus newStatus = TaskStatus.IN_PROGRESS;
            Deadline deadline = new Deadline();
            when(deadlineRepository.existsById(id)).thenReturn(true);
            when(deadlineRepository.getDeadlineById(id)).thenReturn(Optional.of(deadline));

            TaskStatus result = deadlineService.updateDeadlineStatus(id, newStatus);

            assertEquals(newStatus, result);
            assertEquals(newStatus, deadline.getStatus());
            verify(deadlineRepository, times(1)).save(deadline);
        }

        @Test
        void updateDeadlineStatusNotFound() {

            Long id = 1L;
            when(deadlineRepository.existsById(id)).thenReturn(false);
            assertThrows(IllegalArgumentException.class,
                    () -> deadlineService.updateDeadlineStatus(id, TaskStatus.NOT_STARTED));
        }

        @Test
        void deleteDeadlineSuccess() {

            Long id = 1L;
            when(deadlineRepository.getDeadlineById(id)).thenReturn(Optional.of(new Deadline()));

            AppStatus result = deadlineService.deleteDeadline(id);

            assertEquals(AppStatus.SUCCESS, result);
            verify(deadlineRepository, times(1)).deleteById(id);
        }

        @Test
        void deleteDeadlineNotFound() {

            Long id = 1L;
            when(deadlineRepository.getDeadlineById(id)).thenReturn(Optional.empty());
            assertThrows(IllegalArgumentException.class, () -> deadlineService.deleteDeadline(id));
        }
    }

    @Nested
    public class DateRangeTests {

        @Test
        void getDeadlinesByRange() {
            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = LocalDateTime.now().plusDays(1);
            int page = 1;
            int pageSize = 10;
            Pageable pageable = PageRequest.of(page, pageSize, Sort.by("time").descending());

            List<Deadline> mockList = Collections.singletonList(new Deadline());

            Page<Deadline> mockPage = new PageImpl<>(mockList, pageable, mockList.size());

            List<DeadlineResponseDto> mockResponseList = Collections.singletonList(new DeadlineResponseDto(
                    1L,
                    "Название",
                    "Описание",
                    LocalDateTime.now().plusHours(1),
                    LocalDateTime.now().plusHours(2),
                    TaskStatus.NOT_STARTED
            ));

            when(deadlineRepository.getDeadlineByTheEndBetween(start, end, pageable))
                    .thenReturn(mockPage);
            when(deadlineMapper.mapDeadlineListToDeadlineResponseDtoList(any()))
                    .thenReturn(mockResponseList);

            Page<DeadlineResponseDto> result = deadlineService.getDeadlinesByRange(start, end, page, pageSize);
            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertEquals(1, result.getContent().size());
            assertEquals(mockResponseList.get(0).name(), result.getContent().get(0).name());
        }
    }

    @Test
    void getDeadlinesByRangeStartAfterEnd() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now();
        int page = 1;
        int pageSize = 10;
        assertThrows(IllegalArgumentException.class,
                () -> deadlineService.getDeadlinesByRange(start, end, page, pageSize));
    }

    @Test
    void getDeadlinesByDaySuccess() {
        LocalDate day = LocalDate.now();
        LocalDateTime startOfDay = day.atStartOfDay();
        LocalDateTime endOfDay = day.atTime(23, 59, 59);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("time").descending());

        List<Deadline> mockList = Collections.singletonList(new Deadline());
        Page<Deadline> mockPage = new PageImpl<>(mockList, pageable, mockList.size());
        List<DeadlineResponseDto> mockResponseList = Collections.singletonList(new DeadlineResponseDto(
                1L,
                "Название",
                "Описание",
                LocalDateTime.now(),
                LocalDateTime.now(),
                TaskStatus.NOT_STARTED
        ));

        when(deadlineRepository.getDeadlineByTheEndBetween(eq(startOfDay), eq(endOfDay), any(Pageable.class)))
                .thenReturn(mockPage);

        when(deadlineMapper.mapDeadlineListToDeadlineResponseDtoList(any()))
                .thenReturn(mockResponseList);

        Page<DeadlineResponseDto> result = deadlineService.getDeadlinesByDay(day, pageable.getPageNumber(), pageable.getPageSize());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(deadlineRepository, times(1)).getDeadlineByTheEndBetween(
                eq(startOfDay),
                eq(endOfDay),
                any(Pageable.class)
        );
    }
}
