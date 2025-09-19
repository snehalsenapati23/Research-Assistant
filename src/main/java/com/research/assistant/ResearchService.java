package com.research.assistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@Slf4j
@Service
public class ResearchService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResearchService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String processRequest(ResearchRequest researchRequest) {

//Build the prompt
        String prompt=processContent(researchRequest);
        //Query the ai model api
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
        );
String response=webClient.post().uri(geminiApiUrl+geminiApiKey).
        bodyValue(requestBody)
        .retrieve().bodyToMono(String.class).block();
        //parse the response
        //return response

        return extractFromText(response);
    }
    public String extractFromText(String response) {
        try{
            GeminiResponse geminiResponse=objectMapper.readValue(response,GeminiResponse.class);

            if (geminiResponse != null &&
                   !geminiResponse.getCandidates().isEmpty()) {
                GeminiResponse.Candidate firstCandidate=geminiResponse.getCandidates().get(0);
                if (firstCandidate.getContent() != null &&
                        firstCandidate.getContent().getParts() != null &&
                        !firstCandidate.getContent().getParts().isEmpty()) {
                    return firstCandidate.getContent().getParts().get(0).getText();
                }
            }
        }catch(Exception e){
            return "Error Parsing: "+e.getMessage();
        }
        return "No Content found";
    }
    public String processContent(ResearchRequest researchRequest) {
        StringBuilder prompt=new StringBuilder();
log.info("Processing research request {}", researchRequest);
        switch (researchRequest.getOperation()){
            case "summarize":
                prompt.append("Summarize the following text in a concise and clear way, in few sentences:\n\n");
                break;
            case "explain":
                prompt.append("Explain the following text in simple terms, as if to a beginner:\n\n");
                break;
            default:
                   throw new IllegalArgumentException("Invalid operation: "+researchRequest.getOperation());
        }
        prompt.append(researchRequest.getContent());
        return prompt.toString();
    }
}
