package com.step.planB_planner.service.impl;

import com.step.planB_planner.constant.AppStatus;
import com.step.planB_planner.constant.TaskStatus;
import com.step.planB_planner.exception.IllegalTimeArgument;
import com.step.planB_planner.exception.TaskException;
import com.step.planB_planner.mapper.DeadlineMapper;
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
            DeadlineRequestDto request = new DeadlineRequestDto("Task", "Desc", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
            Deadline deadline = new Deadline();
            DeadlineResponseDto responseDto = new DeadlineResponseDto(
                    1L,                        // id
                    "Название",                 // name
                    "Описание",                 // description
                    LocalDateTime.now(),       // whenGot
                    LocalDateTime.now().plusHours(1),       // theEnd
                    TaskStatus.notStarted      // status
            );

            when(deadlineRepository.getDeadlineByName(request.name())).thenReturn(Optional.empty());
            when(deadlineRepository.getDeadlineByWhenGot(request.whenGot())).thenReturn(Optional.empty());
            when(deadlineMapper.mapDeadlineRequestDtoToDeadline(request)).thenReturn(deadline);
            when(deadlineMapper.mapDeadlineToDeadlineResponseDto(deadline)).thenReturn(responseDto);

            DeadlineResponseDto result = deadlineService.addDeadline(request);

            // Assert
            assertNotNull(result);
            assertEquals(TaskStatus.notStarted, deadline.getStatus());
            verify(deadlineRepository, times(1)).save(deadline);
        }

        @Test
        void addDeadline_EndBeforeNow_ThrowsIllegalTimeArgument() {
            // Arrange
            DeadlineRequestDto request = new DeadlineRequestDto("Task", "Desc", LocalDateTime.now(), LocalDateTime.now().minusDays(1));

            // Act & Assert
            IllegalTimeArgument exception = assertThrows(IllegalTimeArgument.class, () -> deadlineService.addDeadline(request));
            assertEquals("Я понимаю, что задача должна быть выполнена еще вчера, но будь реалистом!", exception.getMessage());
        }

        @Test
        void addDeadline_WhenGotAfterEnd_ThrowsIllegalTimeArgument() {
            // Arrange
            DeadlineRequestDto request = new DeadlineRequestDto("Task", "Desc", LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(2));

            // Act & Assert
            IllegalTimeArgument exception = assertThrows(IllegalTimeArgument.class, () -> deadlineService.addDeadline(request));
            assertEquals("Совсем сдурел?", exception.getMessage());
        }

        @Test
        void addDeadline_NameExists_ThrowsTaskException() {
            // Arrange
            DeadlineRequestDto request = new DeadlineRequestDto("Duplicate", "Desc", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
            when(deadlineRepository.getDeadlineByName(request.name())).thenReturn(Optional.of(new Deadline()));

            // Act & Assert
            TaskException exception = assertThrows(TaskException.class, () -> deadlineService.addDeadline(request));
            assertEquals("А ты очень забывчив(а) однако!", exception.getMessage());
        }

        @Test
        void addDeadline_WhenGotExists_ThrowsTaskException() {
            // Arrange
            DeadlineRequestDto request = new DeadlineRequestDto("Task", "Desc", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
            when(deadlineRepository.getDeadlineByName(request.name())).thenReturn(Optional.empty());
            when(deadlineRepository.getDeadlineByWhenGot(request.whenGot())).thenReturn(Optional.of(new Deadline()));

            // Act & Assert
            TaskException exception = assertThrows(TaskException.class, () -> deadlineService.addDeadline(request));
            assertEquals("Может таблеточки попить?", exception.getMessage());
        }
    }

    @Nested
    class UpdateDeadlineTests {

        @Test
        void updateDeadline_Success() {
            // Arrange
            Long id = 1L;
            DeadlineRequestDto request = new DeadlineRequestDto("New Name", "New Desc", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3));
            Deadline existingDeadline = new Deadline();
            DeadlineResponseDto responseDto = new DeadlineResponseDto(
                    1L,                        // id
                    "Название",                 // name
                    "Описание",                 // description
                    LocalDateTime.now().plusHours(1),       // whenGot
                    LocalDateTime.now().plusHours(2),       // theEnd
                    TaskStatus.notStarted      // status
            );

            when(deadlineRepository.getDeadlineById(id)).thenReturn(Optional.of(existingDeadline));
            when(deadlineMapper.mapDeadlineToDeadlineResponseDto(existingDeadline)).thenReturn(responseDto);

            // Act
            DeadlineResponseDto result = deadlineService.updateDeadline(id, request);

            // Assert
            assertNotNull(result);
            assertEquals("New Name", existingDeadline.getName());
            assertEquals("New Desc", existingDeadline.getDescription());
            assertEquals(request.theEnd(), existingDeadline.getTheEnd());
            verify(deadlineRepository, times(1)).save(existingDeadline);
        }

        @Test
        void updateDeadline_EndBeforeNow_ThrowsIllegalTimeArgument() {
            // Arrange
            Long id = 1L;
            DeadlineRequestDto request = new DeadlineRequestDto("Name", "Desc", LocalDateTime.now(), LocalDateTime.now().minusDays(1));
            when(deadlineRepository.getDeadlineById(id)).thenReturn(Optional.of(new Deadline()));

            // Act & Assert
            IllegalTimeArgument exception = assertThrows(IllegalTimeArgument.class, () -> deadlineService.updateDeadline(id, request));
            assertEquals("Не пытайся обмануть меня, человек!", exception.getMessage());
        }

        @Test
        void updateDeadline_NotFound_ThrowsIllegalArgumentException() {
            // Arrange
            Long id = 1L;
            DeadlineRequestDto request = new DeadlineRequestDto("Name", "Desc", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
            when(deadlineRepository.getDeadlineById(id)).thenReturn(Optional.empty());

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> deadlineService.updateDeadline(id, request));
            assertEquals("Specified theEnd does not exist!", exception.getMessage());
        }
    }

    @Nested
    class UpdateStatusAndDeleteDeadlineTests {

        @Test
        void updateDeadlineStatus_Success() {
            // Arrange
            Long id = 1L;
            TaskStatus newStatus = TaskStatus.inProgress; // Пример статуса, замените на ваш реальный
            Deadline deadline = new Deadline();
            when(deadlineRepository.existsById(id)).thenReturn(true);
            when(deadlineRepository.getDeadlineById(id)).thenReturn(Optional.of(deadline));

            // Act
            TaskStatus result = deadlineService.updateDeadlineStatus(id, newStatus);

            // Assert
            assertEquals(newStatus, result);
            assertEquals(newStatus, deadline.getStatus());
            verify(deadlineRepository, times(1)).save(deadline);
        }

        @Test
        void updateDeadlineStatus_NotFound_ThrowsIllegalArgumentException() {
            // Arrange
            Long id = 1L;
            when(deadlineRepository.existsById(id)).thenReturn(false);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> deadlineService.updateDeadlineStatus(id, TaskStatus.notStarted));
        }

        @Test
        void deleteDeadline_Success() {
            // Arrange
            Long id = 1L;
            when(deadlineRepository.getDeadlineById(id)).thenReturn(Optional.of(new Deadline()));

            // Act
            AppStatus result = deadlineService.deleteDeadline(id);

            // Assert
            assertEquals(AppStatus.success, result);
            verify(deadlineRepository, times(1)).deleteById(id);
        }

        @Test
        void deleteDeadline_NotFound_ThrowsIllegalArgumentException() {
            // Arrange
            Long id = 1L;
            when(deadlineRepository.getDeadlineById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> deadlineService.deleteDeadline(id));
        }
    }

    @Nested
    public class DateRangeTests {

        @Test
        void getDeadlinesByRange() {
            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = LocalDateTime.now().plusDays(1);
            List<Deadline> mockList = Collections.singletonList(new Deadline());
            List<DeadlineResponseDto> mockResponseList = Collections.singletonList(new DeadlineResponseDto(
                    1L,                        // id
                    "Название",                 // name
                    "Описание",                 // description
                    LocalDateTime.now().plusHours(1),       // whenGot
                    LocalDateTime.now().plusHours(2),       // theEnd
                    TaskStatus.notStarted      // status
            ));

            when(deadlineRepository.getDeadlineByTheEndBetween(start, end)).thenReturn(Optional.of(mockList.get(0)));
            when(deadlineMapper.mapDeadlineListToDeadlineResponseDtoList(any())).thenReturn(mockResponseList);

            List<DeadlineResponseDto> result = deadlineService.getDeadlinesByRange(start, end);
            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Test
    void getDeadlinesByRange_StartAfterEnd_ThrowsIllegalArgumentException() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> deadlineService.getDeadlinesByRange(start, end));
    }

    @Test
    void getDeadlinesByDay_Success() {
        // Arrange
        LocalDate day = LocalDate.now();
        when(deadlineRepository.getDeadlineByTheEndBetween(any(), any())).thenReturn(Optional.of(new Deadline()));
        // Act
        List<DeadlineResponseDto> result = deadlineService.getDeadlinesByDay(day);
        // Assert
        assertNotNull(result);
        verify(deadlineRepository, times(1)).getDeadlineByTheEndBetween(
                eq(day.atStartOfDay()),
                eq(day.atStartOfDay().withHour(23).withMinute(59).withSecond(58))
        );
    }
}
