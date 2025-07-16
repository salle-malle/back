package com.shinhan.pda_midterm_project.domain.disclosure;

import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import com.shinhan.pda_midterm_project.domain.stock.repository.StockRepository;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DisclosureReader implements ItemReader<Stock> {
    private final StockRepository stockRepository;
    private Iterator<Stock> tickerIterator;

    @Override
    public Stock read() {
        if (tickerIterator == null) {
            tickerIterator = stockRepository.findAll()
                    .stream()
                    .iterator();
        }

        return tickerIterator.hasNext() ? tickerIterator.next() : null;
    }
}
