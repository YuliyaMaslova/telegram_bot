package com.example.telegram_bot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MoexResponse(
        MarketData marketdata
) {
}
