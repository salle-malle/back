package com.shinhan.pda_midterm_project.domain.disclosure.repository;

import com.shinhan.pda_midterm_project.domain.disclosure.model.Disclosure;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DisclosureRepository extends JpaRepository<Disclosure, Long> {

    @Query("""
                SELECT d
                FROM Disclosure d
                JOIN d.stock s
                WHERE s.stockId IN (
                    SELECT ms.stock.stockId
                    FROM MemberStock ms
                    WHERE ms.member.id = :memberId
                )
                ORDER BY d.disclosureDate DESC
            """)
    List<Disclosure> getMyCurrentDisclosure(@Param("memberId") Long memberId, Pageable pageable);

    @Query("""
                SELECT d
                FROM Disclosure d
                JOIN d.stock s
                WHERE s.stockId IN (
                    SELECT ms.stock.stockId
                    FROM MemberStock ms
                    WHERE ms.member.id = :memberId
                )
                ORDER BY d.disclosureDate DESC
            """)
    List<Disclosure> getMyDisclosures(@Param("memberId") Long memberId);
}
