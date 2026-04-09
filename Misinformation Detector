# ⚡ Veritas.AI — Misinformation & Propaganda Detection System

> An AI-powered web application that analyzes text for misinformation, propaganda techniques, and credibility — built with Java Spring Boot and vanilla HTML/CSS/JS.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green?style=flat-square&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)
![Status](https://img.shields.io/badge/Status-Active-brightgreen?style=flat-square)

---

## 📌 Project Overview

Veritas.AI is a web application that analyzes textual content — such as news articles, social media posts, or online statements — to identify misleading information and manipulative propaganda techniques.

Users submit text through the frontend interface. The system processes the text through an NLP pipeline built in Java, evaluates its credibility, detects emotional manipulation, and returns a structured result including a credibility score and detected manipulation flags. All results are saved in a MySQL database.

---

## 👥 Team

| Member | Role | Responsibilities |
|--------|------|-----------------|
| **Srivardhini** | Backend + AI + Database | Spring Boot REST APIs, TextPreprocessorService, PropagandaDetectorService, AnalysisOrchestratorService, MySQL schema, JPA integration |
| **Thanushree M** | Frontend + Integration | SentimentAnalysisService, CredibilityScoreService, UI design (HTML/CSS/JS), API integration, end-to-end testing |

**Institution:** Coimbatore Institute of Technology — Department of Computer Science and Engineering

---

## 🛠️ Tech Stack

| Layer | Technology | Purpose |
|-------|-----------|---------|
| Backend | Java 17 + Spring Boot 3.2 | REST API, business logic, NLP services |
| NLP Processing | Custom Java lexicon engine | Sentiment analysis, propaganda detection |
| Database ORM | Spring Data JPA + Hibernate | Entity mapping, database queries |
| Database | MySQL 8 | Persistent storage of analysis results |
| Build Tool | Maven (mvnw) | Dependency management |
| Frontend | HTML / CSS / JavaScript | User interface and API integration |
| API Testing | Postman / Browser | Endpoint testing and validation |

---

## 🏗️ Project Structure

```
misinformation-detector/
├── mvnw / mvnw.cmd                          ← Maven wrapper (run without installing Maven)
├── pom.xml                                  ← Dependencies
├── frontend/
│   └── index.html                           ← Complete frontend UI
└── src/
    ├── main/
    │   ├── java/com/misinfo/misinformationdetector/
    │   │   ├── MisinformationdetectorApplication.java  ← Entry point
    │   │   ├── controller/
    │   │   │   └── AnalysisController.java             ← REST endpoints
    │   │   ├── dto/
    │   │   │   ├── AnalysisRequest.java                ← Incoming JSON
    │   │   │   └── AnalysisResponse.java               ← Outgoing JSON
    │   │   ├── model/
    │   │   │   └── AnalysisResult.java                 ← JPA Entity (MySQL table)
    │   │   ├── repository/
    │   │   │   └── AnalysisResultRepository.java       ← Database queries
    │   │   └── service/
    │   │       ├── AnalysisOrchestratorService.java    ← Pipeline coordinator
    │   │       ├── TextPreprocessorService.java        ← Clean + tokenize text
    │   │       ├── PropagandaDetectorService.java      ← Detect manipulation techniques
    │   │       ├── SentimentAnalysisService.java       ← POSITIVE/NEGATIVE/NEUTRAL
    │   │       └── CredibilityScoreService.java        ← Score 0–100 + classification
    │   └── resources/
    │       └── application.properties                  ← DB config
    └── test/
        └── MisinformationdetectorApplicationTests.java
```

---

## ⚙️ Setup & Installation

### Prerequisites

| Tool | Version | Download |
|------|---------|----------|
| Java JDK | 17 or higher | [adoptium.net](https://adoptium.net) |
| MySQL | 8.0 | [mysql.com](https://dev.mysql.com/downloads/) |
| Git | Any | [git-scm.com](https://git-scm.com) |

---

### Step 1 — Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/misinformation-detector.git
cd misinformation-detector
```

### Step 2 — Set Up MySQL Database

Open MySQL Workbench or MySQL command line and run:

```sql
CREATE DATABASE misinfo_db;
```

> Tables are created **automatically** by Hibernate when you first run the app.

### Step 3 — Configure Your Database Password

Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD_HERE
```

If your MySQL has no password, leave it blank:
```properties
spring.datasource.password=
```

### Step 4 — Run the Backend

```bash
# Windows
.\mvnw spring-boot:run

# Mac / Linux
./mvnw spring-boot:run
```

Wait for this message:
```
Misinformation Detector is running!
Backend: http://localhost:8080
```

### Step 5 — Open the Frontend

Open `frontend/index.html` in your browser.

The status indicator in the top-right will show **"backend online"** in green when everything is connected. ✅

---

## 🔌 API Endpoints

Base URL: `http://localhost:8080/api`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/analyze` | Analyze text for misinformation |
| `GET` | `/history` | Get all past analyses |
| `GET` | `/history/{id}` | Get single result by ID |
| `DELETE` | `/history/{id}` | Delete a result |
| `GET` | `/hello` | Health check |

### Sample Request

```bash
curl -X POST http://localhost:8080/api/analyze \
  -H "Content-Type: application/json" \
  -d '{"text": "SHOCKING truth they dont want you to know! Deep state cover-up!!!"}'
```

### Sample Response

```json
{
  "inputText": "SHOCKING truth they dont want you to know!...",
  "credibilityScore": 25.0,
  "classification": "PROPAGANDA",
  "sentiment": "NEGATIVE",
  "propagandaFlags": ["conspiracy_indicators", "false_urgency", "loaded_language"],
  "summary": "Credibility Score: 25.0/100. Strong propaganda indicators detected."
}
```

---

## 📊 Credibility Score System

| Score | Classification | Meaning |
|-------|---------------|---------|
| 80 – 100 | 🟢 CREDIBLE | Appears trustworthy |
| 60 – 79 | 🟡 SUSPICIOUS | Some concerning patterns |
| 40 – 59 | 🟠 MISLEADING | Likely distorted information |
| 0 – 39 | 🔴 PROPAGANDA | Strong manipulation detected |

---

## 🚩 Detected Propaganda Techniques

The system detects **7 manipulation techniques**:

1. **Fear Appeal** — language designed to cause panic or fear
2. **Loaded Language** — emotionally charged or biased words
3. **False Urgency** — pressure to act immediately without thinking
4. **Bandwagon Effect** — claiming "everyone believes this"
5. **Name Calling** — personal attacks instead of arguments
6. **Exaggeration** — extreme claims like "best ever" or "100%"
7. **Conspiracy Indicators** — phrases like "deep state", "cover-up", "they don't want you to know"

---

## 🗄️ Database Schema

Table: `analysis_results`

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK) | Auto-increment |
| input_text | TEXT | Submitted text |
| credibility_score | DOUBLE | Score from 0–100 |
| classification | VARCHAR | CREDIBLE / SUSPICIOUS / MISLEADING / PROPAGANDA |
| sentiment | VARCHAR | POSITIVE / NEGATIVE / NEUTRAL |
| propaganda_flags | TEXT | Detected techniques |
| summary | TEXT | Human-readable explanation |
| analyzed_at | TIMESTAMP | Time of analysis |

---

## 🧪 Testing

Run unit tests:
```bash
.\mvnw test
```

Manual API testing with Postman:
- Import the base URL `http://localhost:8080/api`
- Test `POST /analyze` with a JSON body `{"text": "your text here"}`
- Test `GET /history` to see saved results

---

## 🔧 Troubleshooting

| Problem | Solution |
|---------|----------|
| `.\mvnw` not recognized | Make sure you're in the project root folder where `mvnw` file exists |
| Cannot connect to MySQL | Make sure MySQL service is running. Check username/password in `application.properties` |
| Frontend shows "backend offline" | Make sure Spring Boot is running on port 8080 |
| Port 8080 already in use | Change `server.port=8081` in `application.properties` and update API URL in `index.html` |
| Tables not created | Set `spring.jpa.hibernate.ddl-auto=create` for first run, then change back to `update` |

---

## 📝 Module Breakdown

| Module | What Was Built |
|--------|---------------|
| Module 1 | Spring Boot setup, REST API, first endpoint |
| Module 2 | MySQL schema, JPA Entity, Repository layer |
| Module 3 | NLP Service Layer — all 5 services |
| Module 4 | Frontend UI, backend integration, end-to-end testing |

---

*Veritas.AI — Built for Professor Review · Coimbatore Institute of Technology · 2024*
