
# 🗺️ Roadmap Genie - AI-Powered Learning Roadmap Generator

## Overview
Roadmap Genie is an intelligent learning roadmap generator built with Spring Boot and Spring AI. Users can enter any technology topic, and the application generates a comprehensive, personalized learning path with curated resources and a visual flowchart.

## 🎯 Problem Statement
Learning a new technology can be overwhelming. Developers often struggle to:
- Find structured learning paths
- Know what to learn and in what order
- Discover quality resources
- Visualize their learning journey

## 💡 Solution
Roadmap Genie solves this by:
- Generating detailed 12-20 step learning roadmaps using AI
- Providing curated resources for each step
- Creating visual flowcharts showing the learning progression
- Organizing content by difficulty (Beginner → Intermediate → Advanced → Expert)

## 🛠️ Tech Stack

### Backend
- **Java 17+**
- **Spring Boot 3.x**
- **Spring AI** (OpenAI Integration)
- **Maven**

### Frontend
- **React 18**
- **Vite**
- **Tailwind CSS**
- **Mermaid.js** (for flowchart visualization)

### APIs
- **OpenAI API** (GPT-4/GPT-3.5) for AI-powered roadmap generation

## ✨ Key Features

1. **AI-Powered Content Generation**
   - Leverages Spring AI and OpenAI to generate intelligent, contextual roadmaps
   - Adapts to any technology topic entered by the user

2. **Comprehensive Learning Paths**
   - 12-20 detailed steps from beginner to expert level
   - Clear progression and dependencies between topics
   - Specific, actionable learning objectives

3. **Curated Resources**
   - 3-5 high-quality resources per step
   - Mix of official documentation, tutorials, courses, and repositories

4. **Visual Flowchart**
   - Interactive Mermaid.js diagrams
   - Color-coded by difficulty level
   - Shows learning dependencies and progression

5. **Clean Architecture**
   - Separation of concerns (roadmap data vs. diagram generation)
   - RESTful API design
   - No database required for POC simplicity

## 🚀 API Endpoints

### 1. Get Roadmap Data
```http
GET /ai/roadmap?topic={topic}
```
Returns structured JSON with learning steps and resources.

**Example Response:**
```json
{
  "topic": "Spring Boot",
  "roadmap": [
    {
      "step": "Step 1: Java Fundamentals & JDK Setup",
      "description": "Master core Java concepts...",
      "resources": [
        "https://docs.oracle.com/javase/tutorial/",
        "https://www.udemy.com/course/java-programming/"
      ]
    }
  ]
}
```

### 2. Get Mermaid Diagram
```http
GET /ai/mermaid?topic={topic}
```
Returns Mermaid flowchart code as plain text.

### 3. Get Complete Roadmap
```http
GET /ai/roadmap-complete?topic={topic}
```
Returns both roadmap data and Mermaid diagram in one response.

## 📦 Installation & Setup

### Prerequisites
- Java 17 or higher
- Node.js 18+ and npm
- OpenAI API Key

### Backend Setup

1. Clone the repository
```bash
git clone https://github.com/anoushka-10/RoadMapGenie.git
cd roadmap-genie-backend
```

2. Configure OpenAI API Key
```properties
# application.properties
spring.ai.openai.api-key=your-api-key-here
spring.ai.openai.chat.options.model=gpt-4
```

3. Run the application
```bash
./mvnw spring-boot:run
```

Backend will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to frontend directory
```bash
cd roadmap-genie-frontend
```

2. Install dependencies
```bash
npm install
```

3. Start development server
```bash
npm run dev
```

Frontend will start on `http://localhost:5173`

## 🎬 Demo

[Link to video demo]

## 🏗️ Architecture

```
┌─────────────┐         ┌──────────────┐         ┌─────────────┐
│   React     │  HTTP   │  Spring Boot │   API   │   OpenAI    │
│  Frontend   │ ──────> │   Backend    │ ──────> │     API     │
│  (Vite)     │ <────── │  (Spring AI) │ <────── │   (GPT-4)   │
└─────────────┘  JSON   └──────────────┘  JSON   └─────────────┘
      │
      │ Renders
      ▼
┌─────────────┐
│  Mermaid.js │
│  Flowchart  │
└─────────────┘
```

## 🔑 Key Technical Decisions

1. **Separate Endpoints for Roadmap & Diagram**
   - Avoids JSON/Mermaid syntax conflicts
   - Cleaner code and easier debugging
   - More flexible for frontend

2. **No Database**
   - Kept simple for POC
   - Each request generates fresh content
   - Easy to deploy and demonstrate

3. **Spring AI Integration**
   - Leverages OpenAI's powerful language models
   - Built-in prompt management
   - Easy to switch AI providers if needed

4. **Mermaid.js for Visualization**
   - Renders beautiful flowcharts from code
   - No need for complex charting libraries
   - Easily customizable

## 🚧 Future Enhancements

- [ ] Add user authentication
- [ ] Save roadmaps to database (PostgreSQL)
- [ ] Export roadmaps as PDF
- [ ] Progress tracking for each step
- [ ] Community-contributed roadmaps
- [ ] Integration with learning platforms (Udemy, Coursera)
- [ ] Mobile app version

## 📝 Lessons Learned

1. **Spring AI Simplification**: Spring AI abstracts away OpenAI API complexity, making integration straightforward
2. **Prompt Engineering**: Detailed, specific prompts produce much better AI responses
3. **Frontend-Backend Separation**: Clean API contracts make development smoother
4. **Mermaid Syntax**: Escaping issues taught me to separate concerns properly


**Built with ❤️ using Spring Boot and Spring AI**
