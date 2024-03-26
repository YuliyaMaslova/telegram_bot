package com.example.telegram_bot.configuration;

import com.example.telegram_bot.bot.ExchangeBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class ExchangeBotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(ExchangeBot exchangeBot) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(exchangeBot);
        return api;

    }
}
