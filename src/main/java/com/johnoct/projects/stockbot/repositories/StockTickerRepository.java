package com.johnoct.projects.stockbot.repositories;

import com.johnoct.projects.stockbot.models.StockTicker;
import org.springframework.data.repository.CrudRepository;

public interface StockTickerRepository extends CrudRepository<StockTicker, String> {

    Integer countByUser(String userId);

}
