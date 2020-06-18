package cc.gasches.geodata.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cc.gasches.geodata.dto.CreateSectionRequest;
import cc.gasches.geodata.dto.GeologicalClassDto;
import cc.gasches.geodata.dto.SectionDto;
import cc.gasches.geodata.dto.UpdateSectionRequest;
import cc.gasches.geodata.mapper.SectionMapper;
import cc.gasches.geodata.model.Section;
import cc.gasches.geodata.repository.SectionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionService {
    private static final Logger log = LoggerFactory.getLogger(SectionService.class);

    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;

    @Transactional
    public SectionDto createSection(CreateSectionRequest request) {
        Section section = sectionRepository.save(sectionMapper.requestToSection(request));
        return sectionMapper.sectionToDto(section);
    }

    @Transactional(readOnly = true)
    public List<SectionDto> getAllSections() {
        return sectionMapper.sectionsToDtoList(sectionRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<SectionDto> findAllSectionsByGeoClassCode(String code) {
        return sectionMapper.sectionsToDtoList(sectionRepository.findByGeologicalClasses_Code(code));
    }

    @Transactional(readOnly = true)
    public Optional<SectionDto> getSectionById(long id) {
        return sectionRepository.findById(id).map(sectionMapper::sectionToDto);
    }

    @Transactional
    public Optional<SectionDto> updateSection(long id, UpdateSectionRequest request) {
        Optional<Section> sectionOpt = sectionRepository.findById(id);
        if (sectionOpt.isEmpty()) {
            return Optional.empty();
        }
        Section section = sectionOpt.get();
        if (!StringUtils.isBlank(request.getName())) {
            section.setName(request.getName());
        }
        if (request.getGeologicalClasses() != null) {
            section.clearGeologicalClasses();
            for (GeologicalClassDto dto : request.getGeologicalClasses()) {
                section.addGeologicalClass(sectionMapper.dtoToGeologicalClass(dto));
            }
        }
        return Optional.of(sectionMapper.sectionToDto(sectionRepository.save(section)));
    }

    @Transactional
    public boolean deleteSection(long id) {
        if (sectionRepository.existsById(id)) {
            sectionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
