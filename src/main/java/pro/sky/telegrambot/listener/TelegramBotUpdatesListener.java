//package pro.sky.telegrambot.listener;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
//import pro.sky.telegrambot.model.NotificationTask;
//import pro.sky.telegrambot.service.UpdatesListenerService;
//
//import javax.annotation.PostConstruct;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@Service
//public class TelegramBotUpdatesListener implements UpdatesListener {
//
//    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
//
//    @Autowired
//    private TelegramBot telegramBot;
//    private final UpdatesListenerService updatesListenerService;
//
//    public TelegramBotUpdatesListener(UpdatesListenerService updatesListenerService) {
//        this.updatesListenerService = updatesListenerService;
//    }
//
//    @PostConstruct
//    public void init() {
//        telegramBot.setUpdatesListener(this);
//    }
//
//    @Override
//    public int process(List<Update> updates) {
//
//        updates.forEach(update -> {
//            logger.info("Processing update: {}", update);
//                for (Update updateObj : updates) {
//                    if (updateObj.message() != null) {
//                        if (updateObj.message().text().equals("/start")) {
//                            sendMessage(updateObj.message().chat().id(), "Приветствую тебя, " + updateObj.message().chat().firstName());
//                            createInlineButtons(updateObj.message().chat().id());
//                        } else if (updateObj.message().text().matches("([0-9.:\\s]{16})(\\s)([\\W+]+)")) {
//                            parseMessage(updateObj.message().chat().id(), updateObj.message().text());
//                        }
//                    }
//                }
//        });
//        return UpdatesListener.CONFIRMED_UPDATES_ALL;
//    }
//
//
//    private void sendMessage(long chatId, String messageText) {
//        SendMessage message = new SendMessage(chatId, messageText);
//        SendResponse response = telegramBot.execute(message);
//    }
//
//
//    private void parseMessage(Long chatId, String messageText) {
//        Pattern pattern = Pattern.compile("([0-9.:\\s]{16})(\\s)([\\W+]+)");
//        Matcher matcher = pattern.matcher(messageText);
//        if (matcher.matches()) {
//            String date = matcher.group(1);
//            String item = matcher.group(3);
//            updatesListenerService.createNotificationTask(chatId, date, item);
//        }
//    }
//
//    @Scheduled(cron = "0 * * * * *")
//    public void run() {
//        List<NotificationTask> notifications = updatesListenerService.getCurrentNotifications();
//        sendMessage(notifications.get(0).getChatId(), notifications.get(0).getMessageText());
//        System.out.println(notifications);
//
//    }
//
//    public void createInlineButtons(Long chatId){
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton("Тык");
//        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton("Тык2");
//        inlineKeyboardButton1.callbackData("Была нажата");
//        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
//        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
//        keyboardButtonsRow1.add(inlineKeyboardButton1);
//        keyboardButtonsRow2.add(inlineKeyboardButton2);
//
//        List<List<InlineKeyboardButton>> rowList= new ArrayList<>();
//        rowList.add(keyboardButtonsRow1);
//        rowList.add(keyboardButtonsRow2);
//
//        inlineKeyboardMarkup.addRow(inlineKeyboardButton1);
//        inlineKeyboardMarkup.addRow(inlineKeyboardButton2);
//        SendMessage message = new SendMessage(chatId, "");
//        message.replyMarkup(inlineKeyboardMarkup);
//        SendResponse response = telegramBot.execute(message);
//    }
//}
