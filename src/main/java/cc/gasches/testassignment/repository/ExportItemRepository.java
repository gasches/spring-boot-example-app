package cc.gasches.testassignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cc.gasches.testassignment.model.ExportItem;

public interface ExportItemRepository extends JpaRepository<ExportItem, Long> {

    long countByStatus(ExportItem.Status status);
}
