package cc.gasches.testassignment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cc.gasches.testassignment.model.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findByGeologicalClasses_Code(String code);
}
