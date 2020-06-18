package cc.gasches.geodata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cc.gasches.geodata.model.ImportItem;

public interface ImportItemRepository extends JpaRepository<ImportItem, Long> {
    // NOOP
}
