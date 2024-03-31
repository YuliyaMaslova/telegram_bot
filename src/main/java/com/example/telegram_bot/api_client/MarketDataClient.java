package com.example.telegram_bot.api_client;

import com.example.telegram_bot.model.MoexResponse;
import feign.Headers;
import feign.RequestLine;
import org.springframework.cloud.openfeign.CollectionFormat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "moex", url = "https://iss.moex.com")
public interface MarketDataClient {

    @GetMapping("iss/engines/currency/markets/selt/boards/CETS/securities.json?iss.only=marketdata&marketdata.columns=SECID,LAST&securities=CETS:USD000UTSTOM,CETS:CNYRUB_TOM,CETS:EUR_RUB__TOM")
    @CollectionFormat(feign.CollectionFormat.CSV)
    MoexResponse getMarketData();


}
