package com.research.assistant;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/research")
@CrossOrigin("*")
public class ResearchController {
    private ResearchService researchService;

    @PostMapping("/process")
    public ResponseEntity<String> processContent(@RequestBody ResearchRequest researchRequest) {
        String result=researchService.processRequest(researchRequest);

        return ResponseEntity.ok(result);
    }
}
