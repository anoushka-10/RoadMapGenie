package com.roadmap.genie.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class ChatController {

    private final OpenAiChatModel chatModel;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public ChatController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * Returns detailed roadmap steps as clean JSON (no Mermaid diagram)
     */
    @GetMapping("/ai/roadmap")
    public Map<String, Object> generateRoadmap(
            @RequestParam(value = "topic", defaultValue = "Spring Boot") String topic) throws Exception {

        String template = """
            Generate a COMPREHENSIVE and DETAILED structured JSON roadmap for learning {topic}.
            
            Requirements:
            - Include 12-20 steps covering the entire learning journey from absolute beginner to advanced/expert level
            - Each step should be very specific with clear, actionable learning objectives
            - Include beginner fundamentals, intermediate concepts, advanced topics, and real-world projects
            - For each step, provide 3-5 high-quality, specific resources (official docs, tutorials, courses, GitHub repos)
            - Resources should be real, well-known sources in the developer community
            - Steps should follow a logical progression that builds on previous knowledge
            
            Categories to cover (adjust based on topic):
            1. Prerequisites & Setup (environment, tools, basic concepts)
            2. Core Fundamentals (syntax, basic features, key concepts)
            3. Intermediate Concepts (design patterns, best practices, common libraries)
            4. Advanced Topics (performance, security, architecture, scalability)
            5. Real-World Projects (hands-on practice, portfolio building)
            6. Production & Deployment (CI/CD, monitoring, debugging)
            7. Community & Career (contributing, interviewing, staying updated)
            
            Return ONLY valid JSON with this exact structure (no markdown, no code blocks):
            {
              "topic": "{topic}",
              "roadmap": [
                {
                  "step": "Step 1: Setting Up Your Development Environment",
                  "description": "Detailed description of what to learn, why it matters, and what you'll achieve. Be specific about tools, versions, and key concepts.",
                  "resources": [
                    "https://example.com/official-installation-guide",
                    "https://example.com/video-tutorial",
                    "https://example.com/setup-best-practices"
                  ]
                }
              ]
            }
            
            Make each step detailed and actionable. Don't be generic - be specific to {topic}.
            """;

        template = template.replace("{topic}", topic);
        Prompt prompt = new Prompt(template);

        String aiResponse = chatModel.call(prompt)
                .getResult()
                .getOutput()
                .getText()
                .trim();

        // Extract JSON from response
        int start = aiResponse.indexOf("{");
        int end = aiResponse.lastIndexOf("}") + 1;
        if (start == -1 || end == -1) {
            throw new Exception("No valid JSON found in AI response");
        }

        String jsonOnly = aiResponse.substring(start, end);
        return mapper.readValue(jsonOnly, Map.class);
    }

    /**
     * Returns detailed Mermaid diagram as plain text
     */
    @GetMapping(value = "/ai/mermaid", produces = MediaType.TEXT_PLAIN_VALUE)
    public String generateMermaidDiagram(
            @RequestParam(value = "topic", defaultValue = "Spring Boot") String topic) throws Exception {

        String template = """
            Create a DETAILED and COMPREHENSIVE Mermaid flowchart for a {topic} learning roadmap.
            
            Requirements:
            - Include 12-20 nodes representing specific learning milestones
            - Use descriptive, specific node labels (not generic like "Step 1")
            - Organize into clear subgraphs for different learning phases:
              * Beginner: Prerequisites, Setup, Fundamentals
              * Intermediate: Core Concepts, Common Patterns, Libraries/Frameworks
              * Advanced: Architecture, Performance, Security, Best Practices
              * Expert: Real Projects, Production, Advanced Patterns
            - Show clear progression with arrows indicating dependencies
            - Use different colors for different difficulty levels
            - Make node labels concise but descriptive (e.g., "REST APIs & HTTP", "Database Integration", "Authentication & Security")
            
            Style Guide:
            - Beginner nodes: Light blue (#e3f2fd, stroke:#1976d2)
            - Intermediate nodes: Light green (#e8f5e9, stroke:#388e3c)
            - Advanced nodes: Light orange (#fff3e0, stroke:#f57c00)
            - Expert/Project nodes: Light purple (#f3e5f5, stroke:#7b1fa2)
            
            Return ONLY the raw Mermaid code, no markdown code blocks, no explanations.
            
            Example structure (adapt to {topic}):
            graph TD
                subgraph Beginner["🎯 Beginner Level"]
                    A[Environment Setup]
                    B[Basic Syntax & Types]
                    C[Control Flow & Functions]
                    D[Data Structures]
                end
                
                subgraph Intermediate["⚡ Intermediate Level"]
                    E[Object-Oriented Programming]
                    F[Error Handling]
                    G[File I/O & Modules]
                    H[Popular Libraries]
                    I[Testing Basics]
                end
                
                subgraph Advanced["🚀 Advanced Level"]
                    J[Design Patterns]
                    K[Performance Optimization]
                    L[Security Best Practices]
                    M[Database Integration]
                    N[API Development]
                end
                
                subgraph Expert["💎 Expert & Projects"]
                    O[Build Real Project]
                    P[Deployment & CI/CD]
                    Q[Monitoring & Logging]
                    R[Contributing to Open Source]
                end
                
                A --> B --> C --> D
                D --> E --> F
                F --> G --> H --> I
                I --> J --> K
                K --> L --> M --> N
                N --> O --> P
                P --> Q --> R
                
                style A fill:#e3f2fd,stroke:#1976d2,stroke-width:2px
                style B fill:#e3f2fd,stroke:#1976d2,stroke-width:2px
                style C fill:#e3f2fd,stroke:#1976d2,stroke-width:2px
                style D fill:#e3f2fd,stroke:#1976d2,stroke-width:2px
                
                style E fill:#e8f5e9,stroke:#388e3c,stroke-width:2px
                style F fill:#e8f5e9,stroke:#388e3c,stroke-width:2px
                style G fill:#e8f5e9,stroke:#388e3c,stroke-width:2px
                style H fill:#e8f5e9,stroke:#388e3c,stroke-width:2px
                style I fill:#e8f5e9,stroke:#388e3c,stroke-width:2px
                
                style J fill:#fff3e0,stroke:#f57c00,stroke-width:2px
                style K fill:#fff3e0,stroke:#f57c00,stroke-width:2px
                style L fill:#fff3e0,stroke:#f57c00,stroke-width:2px
                style M fill:#fff3e0,stroke:#f57c00,stroke-width:2px
                style N fill:#fff3e0,stroke:#f57c00,stroke-width:2px
                
                style O fill:#f3e5f5,stroke:#7b1fa2,stroke-width:2px
                style P fill:#f3e5f5,stroke:#7b1fa2,stroke-width:2px
                style Q fill:#f3e5f5,stroke:#7b1fa2,stroke-width:2px
                style R fill:#f3e5f5,stroke:#7b1fa2,stroke-width:2px
            
            Create a similar detailed structure specifically for {topic}. Be SPECIFIC and DETAILED with node names.
            """;

        template = template.replace("{topic}", topic);
        Prompt prompt = new Prompt(template);

        String mermaidCode = chatModel.call(prompt)
                .getResult()
                .getOutput()
                .getText()
                .trim();

        // Clean up if AI adds markdown code blocks
        mermaidCode = mermaidCode.replace("```mermaid", "")
                                 .replace("```", "")
                                 .trim();

        return mermaidCode;
    }

    /**
     * Combined endpoint that returns both roadmap and Mermaid diagram
     */
    @GetMapping("/ai/roadmap-complete")
    public Map<String, Object> generateCompleteRoadmap(
            @RequestParam(value = "topic", defaultValue = "Spring Boot") String topic) throws Exception {

        // Get roadmap data
        Map<String, Object> roadmapData = generateRoadmap(topic);
        
        // Get Mermaid diagram
        String mermaidDiagram = generateMermaidDiagram(topic);
        
        // Combine them
        Map<String, Object> response = new HashMap<>();
        response.put("topic", roadmapData.get("topic"));
        response.put("roadmap", roadmapData.get("roadmap"));
        response.put("mermaid", mermaidDiagram);
        
        return response;
    }
}