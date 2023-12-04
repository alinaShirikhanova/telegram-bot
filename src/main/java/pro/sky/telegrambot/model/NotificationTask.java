package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "notification_task")
@Table(name = "notification_task")
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "chat_id")
    Long chatId;
    @Column(name = "message_text")
    String messageText;
    @Column(name = "notification_send_time")
    LocalDateTime notificationSendTime;

    public NotificationTask(Long chatId, String messageText, LocalDateTime notificationSendTime) {
        this.chatId = chatId;
        this.messageText = messageText;
        this.notificationSendTime = notificationSendTime;
    }

    public NotificationTask() {
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", messageText='" + messageText + '\'' +
                ", notificationSendTime=" + notificationSendTime +
                '}';
    }
}
