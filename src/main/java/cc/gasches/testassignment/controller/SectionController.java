package cc.gasches.testassignment.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.gasches.testassignment.dto.CreateSectionRequest;
import cc.gasches.testassignment.dto.SectionDto;
import cc.gasches.testassignment.service.SectionService;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping(path = "/sections", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class SectionController {
    private static final Logger log = LoggerFactory.getLogger(SectionController.class);

    private final SectionService sectionService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SectionDto> createSection(@RequestBody CreateSectionRequest request) {
        return ResponseEntity.ok(sectionService.createSection(request));
    }

    @GetMapping
    public ResponseEntity<List<SectionDto>> getAllSections() {
        return ResponseEntity.ok(sectionService.getAllSections());
    }

//    @GetMapping
    public ResponseEntity<?> getSection() {
        return ResponseEntity.ok("OK");
    }

//    @PutMapping
    public ResponseEntity<?> updateSection() {
        return ResponseEntity.ok("OK");
    }

//    @DeleteMapping
    public ResponseEntity<?> deleteSection() {
        return ResponseEntity.ok("OK");
    }
}
