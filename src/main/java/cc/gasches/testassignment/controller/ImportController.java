package cc.gasches.testassignment.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cc.gasches.testassignment.service.ImportService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(path = "/import")
@RequiredArgsConstructor
public class ImportController {
    private static final Logger log = LoggerFactory.getLogger(ImportController.class);

    private final ImportService importService;

    @PostMapping
    public ResponseEntity<Long> importData(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(importService.uploadAsync(file));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<String> status(@PathVariable("id") long id) {
        return importService.getJobStatus(id).map(Enum::name).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
