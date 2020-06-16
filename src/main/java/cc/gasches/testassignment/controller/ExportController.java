package cc.gasches.testassignment.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import cc.gasches.testassignment.service.ExportService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(path = "/export")
@RequiredArgsConstructor
public class ExportController {
    private static final Logger log = LoggerFactory.getLogger(ExportController.class);

    private final ExportService exportService;

    @PostMapping
    public ResponseEntity<Long> exportData() {
        return ResponseEntity.ok(exportService.scheduleDataExport());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<String> status(@PathVariable("id") long id) {
        return exportService.getJobStatus(id).map(Enum::name).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional(readOnly = true)
    @GetMapping(path = "/{id}/file", produces = "text/csv")
    public ResponseEntity<StreamingResponseBody> getFile(@PathVariable("id") long id) {
        return exportService.getFile(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
