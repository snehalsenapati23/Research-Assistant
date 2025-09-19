package com.research.assistant;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/research")
@CrossOrigin("*")
public class ResearchController {
    private ResearchService researchService;

    @PostMapping("/process")
    public ResponseEntity<String> processContent(@RequestBody ResearchRequest researchRequest) {

        log.info("Processing research request {}", researchRequest);
        String result=researchService.processRequest(researchRequest);

        return ResponseEntity.ok(result);
    }
}
