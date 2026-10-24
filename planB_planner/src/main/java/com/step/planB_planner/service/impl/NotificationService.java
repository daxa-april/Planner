package com.step.planB_planner.service.impl;

import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Daria Pevets
 **/
@Service
public class NotificationService {

    public void sendNotification(String title, String message) {

        String safeTitle = title.replace("'", "''");
        String safeMessage = message.replace("'", "''");

        // PowerShell скрипт, использующий встроенный Windows Notification XML
        String powershellCmd = String.format(
                "[Windows.UI.Notifications.ToastNotificationManager, Windows.UI.Notifications, ContentType = WindowsRuntime] > $null; " +
                        "$template = [Windows.UI.Notifications.ToastNotificationManager]::GetTemplateContent([Windows.UI.Notifications.ToastTemplateType]::ToastText02); " +
                        "$toastXml = [xml]$template.GetXml(); " +
                        "$toastXml.GetElementsByTagName('text').Item(0).AppendChild($toastXml.CreateTextNode('%s')) > $null; " +
                        "$toastXml.GetElementsByTagName('text').Item(1).AppendChild($toastXml.CreateTextNode('%s')) > $null; " +
                        "$toast = [Windows.UI.Notifications.ToastNotification]::new($toastXml); " +
                        "[Windows.UI.Notifications.ToastNotificationManager]::CreateToastNotifier('Spring Boot App').Show($toast);",
                safeTitle, safeMessage
        );

        // Запуск процесса в фоновом режиме, чтобы не блокировать основной поток Spring Boot
        new Thread(() -> {
            try {
                ProcessBuilder pb = new ProcessBuilder(
                        "powershell.exe",
                        "-NoProfile",
                        "-ExecutionPolicy", "Bypass",
                        "-Command", powershellCmd
                );
                pb.start();
            } catch (IOException e) {
                throw new RuntimeException("Не удалось отправить уведомление.");
                //@ToDo: add to exception handler
            }
        }).start();
    }
}
