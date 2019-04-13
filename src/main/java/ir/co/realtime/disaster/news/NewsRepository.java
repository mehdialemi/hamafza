package ir.co.realtime.disaster.news;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface NewsRepository extends JpaRepository<Report, Long> {

    @Query(value = "SELECT * FROM Report AS r WHERE r.createdAt > :start AND r.confirmed = true", nativeQuery = true)
    List<Report> findRecents(@Param("start") Instant start);

    @Query(value = "SELECT * FROM Report AS r WHERE r.warningThreshold > :threshold LIMIT :limit", nativeQuery = true)
    List<Report> findWarnings(@Param("threshold") Integer threshold, @Param("limit") Integer limit);

    @Query(value = "SELECT * FROM Report AS r WHERE r.source = :source",
            countQuery = "SELECT count(*) FROM Report AS r WHERE r.source = :source",
            nativeQuery = true)
    Page<Report> reportBySource(@Param("source") Report.Source source, Pageable pageable);

}
