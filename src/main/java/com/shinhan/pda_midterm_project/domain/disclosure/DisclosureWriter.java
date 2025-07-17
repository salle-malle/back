package com.shinhan.pda_midterm_project.domain.disclosure;

import com.shinhan.pda_midterm_project.domain.disclosure.model.Disclosure;
import com.shinhan.pda_midterm_project.domain.disclosure.repository.DisclosureRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DisclosureWriter implements ItemWriter<Disclosure> {

    private final DisclosureRepository disclosureRepository;

    @Override
    @Transactional
    public void write(Chunk<? extends Disclosure> chunk) {
        List<? extends Disclosure> items = chunk.getItems();
        items.forEach(i -> System.out.println(" - " + i));
        if (!items.isEmpty()) {
            disclosureRepository.saveAll(items);
            disclosureRepository.flush();
        }
    }
}
