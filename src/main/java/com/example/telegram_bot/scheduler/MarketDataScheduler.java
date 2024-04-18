package com.example.telegram_bot.scheduler;

import com.example.telegram_bot.api_client.MarketDataClient;
import com.example.telegram_bot.bot.ExchangeBot;
import com.example.telegram_bot.model.MoexResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MarketDataScheduler {
    private final ExchangeBot exchangeBot;
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeBot.class);

    public MarketDataScheduler(ExchangeBot exchangeBot) {
        this.exchangeBot = exchangeBot;
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void sendMarketData(long chatId) {
        try {
            exchangeBot.sendMarketData(chatId);
        } catch (Exception e) {
            LOG.error("Ошибка при получении и обработке данных рынка", e);
        }
    }
}
