package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.service.UpdatesListenerService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;
    private final UpdatesListenerService updatesListenerService;

    public TelegramBotUpdatesListener(UpdatesListenerService updatesListenerService) {
        this.updatesListenerService = updatesListenerService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {

        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
                for (Update updateObj : updates) {
                    if (updateObj.message() != null) {
                        if (updateObj.message().text().equals("/start")) {
                            sendMessage(updateObj.message().chat().id(), "Приветствую тебя, " + updateObj.message().chat().firstName());
                        } else if (updateObj.message().text().matches("([0-9.:\\s]{16})(\\s)([\\W+]+)")) {
                            parseMessage(updateObj.message().chat().id(), updateObj.message().text());
                        }
                    }
                }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }


    private void sendMessage(long chatId, String messageText) {
        SendMessage message = new SendMessage(chatId, messageText);
        SendResponse response = telegramBot.execute(message);
    }


    private void parseMessage(Long chatId, String messageText) {
        Pattern pattern = Pattern.compile("([0-9.:\\s]{16})(\\s)([\\W+]+)");
        Matcher matcher = pattern.matcher(messageText);
        if (matcher.matches()) {
            String date = matcher.group(1);
            String item = matcher.group(3);
            updatesListenerService.createNotificationTask(chatId, date, item);
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void run() {
        List<NotificationTask> notifications = updatesListenerService.getCurrentNotifications();
        System.out.println(notifications);

    }
}
