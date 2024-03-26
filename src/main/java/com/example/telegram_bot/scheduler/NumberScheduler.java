package com.example.telegram_bot.scheduler;

import com.example.telegram_bot.bot.ExchangeBot;
import com.example.telegram_bot.service.NumberService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class NumberScheduler {

    private final ExchangeBot exchangeBot;

    public NumberScheduler(ExchangeBot exchangeBot) {
        this.exchangeBot = exchangeBot;
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void sendRandomNumber() {
        int randomNumber = NumberService.generateRandomNumber();
        exchangeBot.sendMessage(-123456789L, "Random number: " + randomNumber);
    }
}
