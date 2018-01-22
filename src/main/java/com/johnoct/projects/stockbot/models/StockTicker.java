package com.johnoct.projects.stockbot.models;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "stocktickers")
public class StockTicker {
    @Id
    String id;
    String user;
    String ticker;
    Date updateDate = new Date();

    public StockTicker(){

    }

    public StockTicker(String user, String ticker){
        this.user = user;
        this.ticker = ticker;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
