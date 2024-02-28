package pro.sky.telegrambot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.telegrambot.configuration.TelegramBotConfiguration;
import pro.sky.telegrambot.model.UserEntity;
import pro.sky.telegrambot.repository.UserRepository;
import pro.sky.telegrambot.utils.Command;

import java.util.ArrayList;
import java.util.List;

import static pro.sky.telegrambot.utils.Buttons.INFO_ABOUT_SHELTER_BUTTON;
import static pro.sky.telegrambot.utils.Commands.*;
import static pro.sky.telegrambot.utils.Messages.*;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramBotConfiguration configuration;
    private final UserRepository userRepository;

    public TelegramBot(TelegramBotConfiguration configuration, UserRepository userRepository) {
        super(configuration.getToken());
        this.configuration = configuration;
        this.userRepository = userRepository;
        createMainMenu();
    }


    @Override
    public void onUpdateReceived(Update update) {
        Long chatId;

        if (update.hasMessage() && update.getMessage().hasText()) {

            chatId = update.getMessage().getChatId();
            String userFirstName = update.getMessage().getChat().getFirstName();
            String text = update.getMessage().getText();
            if (userRepository.findByChatId(chatId).isPresent()){
                sendMessage(chatId, "И снова здравствуйте!");
            }
            else {
                UserEntity userEntity = new UserEntity().setId(1L).setChatId(chatId).setUsername(userFirstName);
                sendMessage(chatId, "Привет привет");
                userRepository.save(userEntity);
            }

//            getCommandsForRegisteredUsers(chatId, text, userFirstName);
//            buttonsForRegistration(chatId, answer);
        }


//        } else if (update.hasCallbackQuery()) {
//
//            chatId = update.getCallbackQuery().getMessage().getChatId();
//            String callbackData = update.getCallbackQuery().getData();
//
//            getResultForKeyBoardButtons(chatId, callbackData);
//        }
    }

    public void getCommandsForRegisteredUsers(Long chatId, String text, String userFirstName) {
        String answer;

        switch (text) {

            case COMMAND_START:
                answer = REACTION_TO_COMMAND_START(userFirstName);
                buttonGetInfoAboutShelter(chatId, answer);
                break;

//            case COMMAND_HELP:
//                answer = REACTION_TO_COMMAND_HELP(userFirstName);
//                buttonCallVolunteer(chatId, answer);
//                break;
//
//            case COMMAND_SETTINGS:
//                answer = REACTION_TO_COMMAND_SETTINGS(userFirstName);
//                getKeyBoardForRegisteredUsers(chatId, answer);
//                break;
//
//            case COMMAND_GET_INFO_ABOUT_SHELTER:
//                answer = INFO_ABOUT_SHELTER;
//                buttonGetInfoAboutProcess(chatId, answer);
//                break;
//
//            case COMMAND_GET_INFO_ABOUT_PROCESS:
//                answer = INFO_ABOUT_PROCESS;
//                buttonGetReportAboutPet(chatId, answer);
//                break;
//
//            case COMMAND_GET_REPORT_ABOUT_PET:
//                answer = REPORT_ABOUT_PET;
//                buttonCallVolunteer(chatId, answer);
//                break;
//
//            case COMMAND_CALL_VOLUNTEER:
//                answer = CALL_VOLUNTEER;
//                reactionToCommand(chatId, answer);
//                break;
//
//            default:
//                answer = DEFAULT_REACTION(userFirstName);
//                buttonCallVolunteer(chatId, answer);
//                break;
        }
    }

    private void buttonGetInfoAboutShelter(Long chatId, String text) {
        SendMessage message = createMessage(chatId, text);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        var buttonGetInfoAboutShelter = new InlineKeyboardButton();

        buttonGetInfoAboutShelter.setText(COMMAND_GET_INFO_ABOUT_SHELTER);
        buttonGetInfoAboutShelter.setCallbackData(INFO_ABOUT_SHELTER_BUTTON);

        row.add(buttonGetInfoAboutShelter);

        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);
        executeMessage(message);
    }

    private void buttonsForRegistration(Long chatId, String text) {
        SendMessage message = createMessage(chatId, text);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        var buttonYes = new InlineKeyboardButton();
        var buttonNo = new InlineKeyboardButton();

        buttonYes.setText("Да");
        buttonYes.setCallbackData("YES_BUTTON");

        buttonNo.setText("Нет");
        buttonNo.setCallbackData("NO_BUTTON");

        row.add(buttonYes);
        row.add(buttonNo);

        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);
        executeMessage(message);
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("ERROR: {}", e.getMessage());
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage sendMessage = createMessage(chatId, text);
        executeMessage(sendMessage);
    }

    private SendMessage createMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        return message;
    }


    private void createMainMenu() {
        List<BotCommand> listOfCommands = new ArrayList<>();

        listOfCommands.add(new BotCommand(Command.COMMAND_START.commandText, Command.COMMAND_START.description));
        listOfCommands.add(new BotCommand(Command.COMMAND_START.commandText, Command.COMMAND_START.description));
        listOfCommands.add(new BotCommand(Command.COMMAND_START.commandText, Command.COMMAND_START.description));
//        listOfCommands.add(new BotCommand(COMMAND_HELP, DESCRIPTION_COMMAND_HELP));
//        listOfCommands.add(new BotCommand(COMMAND_SETTINGS, DESCRIPTION_COMMAND_SETTINGS));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("ERROR: setting bot`s command list {}", e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return configuration.getName();
    }
}
