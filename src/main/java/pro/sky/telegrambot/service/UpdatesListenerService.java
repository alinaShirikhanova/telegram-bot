package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.sql.Timestamp;
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
        System.out.println(notificationTask);
        notificationTaskRepository.save(notificationTask);
    }

    public List<NotificationTask> getCurrentNotifications() {
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDateTime dateTime2 = LocalDateTime.of(dateTime.getYear(),
                dateTime.getMonth(),
                dateTime.getDayOfMonth(),
                dateTime.getHour(),
                dateTime.getMinute(),
                0,
                0);

        return notificationTaskRepository.getNotificationsByDate(dateTime2);
    }
}
