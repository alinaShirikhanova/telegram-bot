package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UpdatesListenerService {
    private final NotificationTaskRepository notificationTaskRepository;

    public UpdatesListenerService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    public void createNotificationTask(Long chatId, String data, String messageText){
        NotificationTask notificationTask = new NotificationTask(chatId, messageText, LocalDateTime.parse(data, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        notificationTaskRepository.save(notificationTask);
    }

    public List<NotificationTask> getCurrentNotifications() {
        LocalTime dateTime = LocalTime.now();
        return notificationTaskRepository.getNotificationsByDate(dateTime);
    }
}
