package com.example.telegram_bot.bot;

import com.example.telegram_bot.api_client.MarketDataClient;
import com.example.telegram_bot.model.MoexResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExchangeBot extends TelegramLongPollingBot {
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeBot.class);
    private static final String START = "/start";
    private static final String MARKET = "/market_info";


    private final MarketDataClient marketDataClient;

    public ExchangeBot(@Value("${bot.token}") String botToken,
                       MarketDataClient marketDataClient) {
        super(botToken);

        this.marketDataClient = marketDataClient;
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getFirstName();

            switch (messageText) {
                case START -> startCommand(chatId, userName);
                case MARKET -> sendMarketData(chatId);
                default -> unknownCommand(chatId);

            }
        }
    }


    @Override
    public String getBotUsername() {
        return "maslova_exchange_bot";
    }

    private void startCommand(Long chatId, String userName) {
        var text = """
                Добро пожаловвать в бот, %s!
                               
                У вас есть возможность узнать текущий курс валют на Московской бирже.
                               
                Для этого воспользуйтесь командой:
                /market_info
                """;
        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    public void sendMarketData(long chatId) {

        try {
            MoexResponse marketData = marketDataClient.getMarketData();
            processMarketData(chatId, marketData);
        } catch (Exception e) {
            LOG.error("Ошибка при получении и обработке данных рынка", e);
        }
    }


    public void processMarketData(long chatId, MoexResponse marketData) {
            Map<String, Double> rates = new HashMap<>();
            for (List<Object> rateInfo : marketData.marketdata().data()) {
                rates.put((String) rateInfo.get(0), (Double) rateInfo.get(1));
            }

            var info = String.format("""
                            Курсы валют на москвоской бирже:
                            %-13s: %s
                            %-13s: %s
                            %-13s: %s
                            """,
                    "Доллар (USD)", rates.get("USD000UTSTOM"),
                    "Евро (EUR)", rates.get("EUR_RUB__TOM"),
                    "Юань (CNY)", rates.get("CNYRUB_TOM")
            );
            sendMessage(chatId, info);
        }


    private void unknownCommand(Long chatId) {
        var text = "Не удалось распознать команду!";
        sendMessage(chatId, text);
    }

    public void sendMessage(long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Ошибка отправки сообщения", e);
        }
    }


}
