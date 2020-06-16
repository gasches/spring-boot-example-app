package cc.gasches.testassignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cc.gasches.testassignment.model.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
    // NOOP
}
