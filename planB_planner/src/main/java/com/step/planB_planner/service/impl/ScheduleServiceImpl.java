package com.step.planB_planner.service.impl;

import com.step.planB_planner.model.entity.Task;
import com.step.planB_planner.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Daria Pevets
 **/
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl {

    private final TaskRepository taskRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void checkUpcomingTasks() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime windowStart = now.plusMinutes(4);
        LocalDateTime windowEnd = now.plusMinutes(5);

        List <Task> upcomingTasks = taskRepository.findTasksForNotification(windowStart, windowEnd);

        for (Task task : upcomingTasks) {
           notificationService.sendNotification(task.getName(), task.getDescription());
           task.setNotified(true);
        }
    }
}
