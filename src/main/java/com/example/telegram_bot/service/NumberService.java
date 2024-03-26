package com.example.telegram_bot.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class NumberService {
    private static final Random random = new Random();

    public static int generateRandomNumber() {
        return random.nextInt(100) + 1;
    }
}
