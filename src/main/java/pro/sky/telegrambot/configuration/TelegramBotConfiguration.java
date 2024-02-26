package pro.sky.telegrambot.configuration;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;



@PropertySource("application.yml")
@Data
@Configuration
public class TelegramBotConfiguration {

    @Value("${telegram.bot.name}")
    private String name;

    @Value("${telegram.bot.token}")
    private String token;
}