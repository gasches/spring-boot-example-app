package cc.gasches.geodata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cc.gasches.geodata.model.ExportItem;

public interface ExportItemRepository extends JpaRepository<ExportItem, Long> {

    long countByStatus(ExportItem.Status status);
}
