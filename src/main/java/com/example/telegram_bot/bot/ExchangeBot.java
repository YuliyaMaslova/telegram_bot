package com.example.telegram_bot.bot;

import com.example.telegram_bot.service.NumberService;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ExchangeBot extends TelegramLongPollingBot {
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeBot.class);
    private static final String START = "/start";
    private static final String INFO = "/get_info";

    private final NumberService numberService;

    public ExchangeBot(@Value("${bot.token}") String botToken, NumberService numberService) {
        super(botToken);
        this.numberService = numberService;
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getFirstName();

            switch (messageText) {
                case START -> startCommand(chatId, userName);
                case INFO -> sendRandomNumber(chatId);
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
               
               Хотите узнать Ваше счастливое число?
               
               Для этого воспользуйтесь командой:
               /get_info
               """;
        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    private void sendRandomNumber(long chatId)  {
        int randomNumber = numberService.generateRandomNumber();
        String message = "Ваше счастливое число: " + randomNumber;
        sendMessage(chatId, message);
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
