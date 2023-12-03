package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UpdatesListenerService {
    private final NotificationTaskRepository notificationTaskRepository;

    public UpdatesListenerService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    public void createNotificationTask(Long chatId, String data, String messageText){
        NotificationTask notificationTask = new NotificationTask(chatId, messageText, LocalDateTime.parse("01.01.2022 20:00", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        notificationTaskRepository.save(notificationTask);
    }
}
