package com.example.stock.facade;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.StockService;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockStockFacade {
    private RedisLockRepository redisLockRepository;
    private final StockService stockService;

    public LettuceLockStockFacade(StockService stockService, RedisLockRepository redisLockRepository) {
        this.stockService = stockService;
        this.redisLockRepository = redisLockRepository;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while(!redisLockRepository.lock(id)){
            Thread.sleep(100);
        }
        try{
            stockService.decrease_V2(id, quantity);
        }finally {
            redisLockRepository.unlock(id);
        }
    }
}
