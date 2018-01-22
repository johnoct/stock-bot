package com.johnoct.projects.stockbot.bot;

import com.johnoct.projects.stockbot.models.StockTicker;
import com.johnoct.projects.stockbot.repositories.StockTickerRepository;

import java.util.regex.Matcher;
import me.ramswaroop.jbot.core.slack.Bot;
import me.ramswaroop.jbot.core.slack.Controller;
import me.ramswaroop.jbot.core.slack.EventType;
import me.ramswaroop.jbot.core.slack.models.Event;
import me.ramswaroop.jbot.core.slack.models.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import com.google.gson.*;
import java.net.*;
import java.io.*;

@Component
public class SlackBot extends Bot{

    @Autowired
    StockTickerRepository stockRepo;

    private static final Logger logger = LoggerFactory.getLogger(SlackBot.class);

    @Value("${slackBotToken}")
    private String slackToken;

    @Override
    public String getSlackToken() {
        return slackToken;
    }

    @Override
    public Bot getSlackBot() {
        return this;
    }

    @Controller(events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE }, pattern = (".*"))
    public void onReceiveDM(WebSocketSession session, Event event, Matcher matcher) {
        // Make a REST CALL with stock ticker
        URL url = null;
        HttpURLConnection request;
        String ticker = matcher.group(0).substring(13);
        String currentdate = "";
        String closeVal = "";
        try {
            url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol="+ ticker +"&interval=1min&apikey=5WSJC21KABPZCMZR");
            request  = (HttpURLConnection)url.openConnection();
            request.setDoOutput(true);
            request.setRequestMethod("GET");

            request.connect();
            JsonParser jp = new JsonParser();
            JsonElement element = jp.parse(new InputStreamReader(request.getInputStream()));
            JsonObject obj = element.getAsJsonObject();

            if (request.getResponseCode() == HttpURLConnection.HTTP_OK){
                JsonObject lastRefreshed = obj.getAsJsonObject("Meta Data");
                currentdate = lastRefreshed.get("3. Last Refreshed").getAsString();
                lastRefreshed = obj.getAsJsonObject("Time Series (1min)");
                lastRefreshed = lastRefreshed.getAsJsonObject(currentdate);
                closeVal = lastRefreshed.get("4. close").getAsString();
            }

        } catch (IOException e){
            e.printStackTrace();
        }

        reply(session, event, new Message("Hi, Here is the current price: " + closeVal));
    }

    @Controller(events = EventType.MESSAGE, pattern = "fadge|shoot|bic")
    public void onReceiveMessage(WebSocketSession session, Event event, Matcher matcher) {
        if(!matcher.group(0).isEmpty()) {
            StockTicker stockticker = new StockTicker(event.getUserId(),matcher.group(0));
            stockRepo.save(stockticker);
            Integer countBadWords = stockRepo.countByUser(event.getUserId());
            if(countBadWords >= 5) {
                reply(session, event, new Message("Enough! You have said too many bad words. \nThe admin will kick you from this channel."));
            } else {
                reply(session, event, new Message("Be careful, you swore "+countBadWords+" times"));
            }
        }
    }

}
