package com.research.assistant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@Service
public class ResearchService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    private final WebClient webClient;

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
    }
    public String processContent(ResearchRequest researchRequest) {
        StringBuilder prompt=new StringBuilder();

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
