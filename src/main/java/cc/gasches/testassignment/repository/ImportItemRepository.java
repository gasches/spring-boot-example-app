package cc.gasches.testassignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cc.gasches.testassignment.model.ImportItem;

public interface ImportItemRepository extends JpaRepository<ImportItem, Long> {
    // NOOP
}
