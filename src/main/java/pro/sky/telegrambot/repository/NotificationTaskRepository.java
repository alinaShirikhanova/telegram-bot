package pro.sky.telegrambot.repository;

import liquibase.pro.packaged.T;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {
    @Query(value = "SELECT * FROM notification_task WHERE notification_send_time=?1", nativeQuery = true)
    List<NotificationTask> getNotificationsByDate(LocalTime date);
}
