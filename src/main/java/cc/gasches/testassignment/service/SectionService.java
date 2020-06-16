package cc.gasches.testassignment.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cc.gasches.testassignment.dto.CreateSectionRequest;
import cc.gasches.testassignment.dto.GeologicalClassDto;
import cc.gasches.testassignment.dto.SectionDto;
import cc.gasches.testassignment.model.GeologicalClass;
import cc.gasches.testassignment.model.Section;
import cc.gasches.testassignment.repository.SectionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionService {
    private static final Logger log = LoggerFactory.getLogger(SectionService.class);

    private final SectionRepository sectionRepository;

    public SectionDto createSection(CreateSectionRequest request) {
        Section section = new Section();
        section.setName(request.getName());
        List<GeologicalClass> geologicalClasses =
                Optional.ofNullable(request.getGeologicalClasses()).orElseGet(Collections::emptyList).stream()
                        .map(geoClassDto -> new GeologicalClass(geoClassDto.getCode(), geoClassDto.getName()))
                        .collect(Collectors.toList());
        section.setGeologicalClasses(geologicalClasses);
        Section storedSection = sectionRepository.save(section);
        List<GeologicalClassDto> geoClassDtos = section.getGeologicalClasses().stream()
                .map(geoClass -> new GeologicalClassDto(geoClass.getName(), geoClass.getCode()))
                .collect(Collectors.toList());
        return new SectionDto(storedSection.getId(), section.getName(), geoClassDtos);
    }

    public List<SectionDto> getAllSections() {
        return sectionRepository.findAll().stream().map(s -> new SectionDto(s.getId(), s.getName(), Collections.emptyList())).collect(
                Collectors.toList());
    }
}
