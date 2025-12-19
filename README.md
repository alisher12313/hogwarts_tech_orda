# Harry Potter Explorer 🪄

A web application for exploring the magical Harry Potter universe. Browse information about Hogwarts houses and discover characters with advanced search and filtering capabilities.

![Java](https://img.shields.io/badge/Java-17+-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![Maven](https://img.shields.io/badge/Maven-3.8+-blue)

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Design Process](#-design-process)
- [Trade-offs](#-trade-offs)
- [Known Issues](#-known-issues)
- [Future Improvements](#-future-improvements)

## ✨ Features

### Home Page
- Atmospheric Harry Potter themed landing page
- Navigation to all major sections

### Houses Section
Detailed information about all four Hogwarts houses:
- **Gryffindor** - Courage and bravery
- **Slytherin** - Ambition and cunning
- **Hufflepuff** - Loyalty and hard work
- **Ravenclaw** - Intelligence and wisdom

Each house displays:
- House colors and visual styling
- House symbol/crest
- Detailed description of traits and values

### Characters Catalog
Comprehensive character browser with:
- Character cards displaying:
    - Character image (with placeholder fallback)
    - Full name
    - Hogwarts house affiliation
    - Patronus information
- **Search functionality** - Find characters by name
- **House filter** - Filter by specific Hogwarts house
- **Pagination** - Navigate through characters with fixed page sizes

## 🛠 Tech Stack

### Backend
- **Java 17+**
    - *Why*: Modern LTS version with excellent performance, strong typing, and robust ecosystem for enterprise applications

- **Spring Boot 3.x**
    - *Why*: Industry-standard framework that dramatically accelerates development with auto-configuration, embedded server, and comprehensive starter dependencies. Provides built-in support for REST APIs, dependency injection, and production-ready features.

- **Spring RestClient**
    - *Why*: Modern, fluent API for HTTP client operations introduced in Spring 6. Cleaner and more intuitive than RestTemplate, with better error handling and type safety. **Required by task specifications** - all external API calls must be made server-side.

- **Thymeleaf**
    - *Why*: Natural templating engine that allows server-side rendering without requiring JavaScript framework knowledge. Perfect for rapid prototyping and eliminates the complexity of maintaining separate frontend/backend codebases.

- **Maven**
    - *Why*: De facto standard build tool for Java projects with excellent dependency management, widespread IDE support, and consistent project structure conventions.

### External Services
- **HP API** (https://hp-api.onrender.com/)
    - Free, public API providing comprehensive Harry Potter universe data
    - **Critical architectural constraint**: All API calls must originate from backend (frontend never calls external API directly)

## 🏗 Architecture

### Architectural Pattern
The application follows a classic **MVC (Model-View-Controller)** pattern with a **Backend Proxy** architecture:

```
Client Browser
    ↓
Thymeleaf Views (Server-rendered HTML)
    ↓
Spring Controllers
    ↓
Service Layer (Business Logic)
    ↓
RestClient (HTTP Client)
    ↓
External HP API
```

### Key Design Decisions

#### 1. **Server-Side Proxy Pattern**
All external API calls are proxied through our backend. The frontend never directly communicates with the HP API.

**Benefits**:
- Centralized API key management (if needed in future)
- Request/response transformation and validation
- Caching opportunities
- Rate limiting control
- Consistent error handling

#### 2. **Fixed-Size Pagination**
Pagination is implemented with a fixed page size on the backend, even though the external API doesn't provide native pagination.

**Implementation**:
- Fetch all characters from external API
- Apply search and filter logic in memory
- Slice results into fixed-size pages
- Return paginated response with metadata

#### 3. **Centralized Exception Handling**
Global exception handler (`@ControllerAdvice`) catches and transforms all errors into consistent response formats.

**Handles**:
- Invalid request parameters
- External API failures (503 Service Unavailable)
- Data validation errors
- Unexpected server errors

### Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com.example.hp/
│   │       ├── controller/     # REST endpoints & page controllers
│   │       ├── service/        # Business logic & API integration
│   │       ├── model/          # Data models & DTOs
│   │       └── exception/      # Custom exceptions & handlers
│   └── resources/
│       ├── templates/          # Thymeleaf HTML templates
│       ├── static/             # CSS, JS, images
│       └── application.properties
```

## 🚀 Getting Started

### Prerequisites
- **Java 17 or higher** ([Download JDK](https://adoptium.net/))
- **Maven 3.8+** ([Download Maven](https://maven.apache.org/download.cgi))

Verify installation:
```bash
java -version
mvn -version
```

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/alisher12313/hogwarts_tech_orda.git
cd hogwarts_tech_orda

```

2. **Install dependencies**
```bash
mvn clean install
```

3. **Run the application**
```bash
mvn spring-boot:run
```

4. **Access the application**
- Home: http://localhost:8080/
- Houses: http://localhost:8080/houses
- Characters (UI): http://localhost:8080/characters
- Characters (API): http://localhost:8080/api/characters?page=1

### Configuration
No additional configuration required. The application uses sensible defaults:
- Server port: `8080`
- External API: `https://hp-api.onrender.com/`

To change defaults, edit `src/main/resources/application.properties`:
```properties
server.port=8080
hp.api.base-url=https://hp-api.onrender.com
```

## 📡 API Documentation

### Get Characters (Paginated)

```http
GET /api/characters?page={page}&search={name}&house={house}
```

**Query Parameters:**

| Parameter | Type   | Required | Description                                      |
|-----------|--------|----------|--------------------------------------------------|
| page      | int    | No       | Page number (default: 1, min: 1)                |
| search    | string | No       | Search by character name (case-insensitive)     |
| house     | string | No       | Filter by house: gryffindor, slytherin, hufflepuff, ravenclaw |

**Example Request:**
```bash
curl "http://localhost:8080/api/characters?page=2&search=harry&house=gryffindor"
```

**Example Response:**
```json
{
  "page": 2,
  "size": 20,
  "total": 42,
  "items": [
    {
      "id": "uuid",
      "name": "Harry Potter",
      "house": "Gryffindor",
      "patronus": "stag",
      "image": "https://..."
    }
  ]
}
```

**Error Responses:**

- `400 Bad Request` - Invalid parameters (e.g., page < 1, invalid house name)
- `503 Service Unavailable` - External HP API is down or unreachable

## 🎨 Design Process

### Phase 1: Foundation (Level 1)
**Goal**: Establish basic application structure

**Implemented**:
- Created Home page with Harry Potter theming
- Built Houses section with all four Hogwarts houses
- Designed responsive navigation system
- Set up Spring Boot project structure with Maven

**Challenges**:
- Balancing visual appeal with server-side rendering limitations
- Ensuring consistent styling across different house themes

### Phase 2: Backend Integration (Level 2)
**Goal**: Connect to external HP API following task requirements

**Implemented**:
- Configured Spring RestClient for HTTP communication
- Created service layer for API integration
- Built backend endpoints that proxy external API
- Ensured frontend **never** directly calls external API

**Challenges**:
- Handling external API rate limits and availability
- Transforming external API responses to match our domain model

### Phase 3: Advanced Features (Level 3)
**Goal**: Implement search, filtering, and pagination

**Implemented**:
- Server-side search by character name (case-insensitive)
- House-based filtering with validation
- Custom pagination logic (since external API doesn't support it)
- Global exception handling for robust error management

**Challenges**:
- Implementing pagination without database or native API support
- Balancing in-memory processing with performance considerations

## 🔬 Unique Approaches

### 1. In-Memory Pagination Strategy
Since the external HP API returns full character lists without pagination support, we implemented a custom pagination approach:

```java
// Fetch all characters
List<Character> allCharacters = externalApi.getCharacters();

// Apply filters
List<Character> filtered = filterByHouseAndSearch(allCharacters);

// Calculate pagination
int start = (page - 1) * pageSize;
int end = Math.min(start + pageSize, filtered.size());

// Return page slice
return filtered.subList(start, end);
```

**Benefits**:
- Consistent UI/UX with predictable page sizes
- Flexible filtering without multiple API calls
- Fast response times for small-to-medium datasets

**Trade-offs**: See below

### 2. Backend-as-Gateway Pattern
All data flows through our backend, creating a single source of truth:

```
Frontend ❌→ External API   (Not allowed)
Frontend ✅→ Our Backend → External API   (Correct)
```

This pattern enables future enhancements like caching, rate limiting, and API key rotation without frontend changes.

### 3. Defensive Error Handling
Comprehensive error handling at multiple levels:
- Request validation with custom exceptions
- External API failure detection with fallback messages
- Global exception handler for consistent error responses

## ⚖️ Trade-offs

### 1. No Database Layer
**Decision**: Fetch data directly from external API without persistence

**Pros**:
- Faster initial development
- No database setup or maintenance
- Always up-to-date with external API
- Simpler deployment (no migrations, backups)

**Cons**:
- Dependent on external API availability
- Higher latency for repeated requests
- No offline functionality
- Cannot implement custom data relationships

**Alternative considered**: PostgreSQL with scheduled sync jobs
**Why rejected**: Overengineering for current scope; external API is authoritative source

### 2. In-Memory Pagination
**Decision**: Process full dataset in memory for each request

**Pros**:
- Simpler implementation
- No database indexes needed
- Filters and search work seamlessly together

**Cons**:
- Scales poorly with large datasets (1000+ characters)
- Redundant API calls for same data
- Memory overhead for concurrent requests

**Alternative considered**: Caching with Redis
**Why rejected**: Premature optimization; current dataset is small (~200 characters)

### 3. Server-Side Rendering (Thymeleaf)
**Decision**: Use Thymeleaf instead of modern SPA framework (React/Vue)

**Pros**:
- Single technology stack (just Java)
- Faster time-to-market
- Better SEO out-of-the-box
- Simpler deployment (single artifact)

**Cons**:
- Less interactive UI
- Full page reloads for navigation
- Limited client-side state management

**Alternative considered**: React + Spring Boot REST API
**Why rejected**: Unnecessary complexity for read-heavy application; team expertise in Java prioritized

## 🐛 Known Issues

### 1. Missing Character Images
**Symptom**: Some characters display placeholder images

**Root Cause**: External API returns null or invalid image URLs for certain characters

**Workaround**: Application detects missing images and displays a default placeholder

**Impact**: Low - doesn't affect functionality, only visual appeal

### 2. External API Dependency
**Symptom**: Application returns 503 errors when HP API is unavailable

**Root Cause**: No local caching or fallback data source

**Workaround**: Global exception handler returns user-friendly error message

**Impact**: Medium - application is unusable when external API is down (rare occurrence)

**Mitigation strategy**: Consider implementing Redis cache for frequently accessed data

### 3. House Name Case Sensitivity
**Symptom**: Filter only works with lowercase house names (gryffindor, not Gryffindor)

**Root Cause**: Direct string comparison without normalization

**Workaround**: Frontend sends lowercase values; API documentation specifies valid values

**Impact**: Low - primarily a UX concern if users directly modify URLs

**Fix planned**: Case-insensitive house name matching in backend

### 4. Pagination Performance with Filters
**Symptom**: Slight delay when applying complex filters on large result sets

**Root Cause**: Full dataset processing for each request

**Impact**: Low for current dataset size (~200 characters)

**Monitoring**: Acceptable performance (<100ms response time)

## 🔮 Future Improvements

### Short-term
- [ ] Add Redis caching layer for external API responses
- [ ] Implement case-insensitive house filtering
- [ ] Add loading indicators for better UX
- [ ] Expand character detail pages with spells, wands, etc.

### Medium-term
- [ ] Introduce database for favorites/bookmarks functionality
- [ ] Add character comparison feature
- [ ] Implement advanced filters (actor, species, wizard status)
- [ ] Add unit and integration tests (JUnit, Mockito)

### Long-term
- [ ] Build RESTful API for third-party integrations
- [ ] Implement user authentication for personalized features
- [ ] Add admin panel for content management
- [ ] Consider migrating frontend to React for richer interactivity

## 📄 License

This project is created for educational purposes.

Harry Potter and all related names, characters, and elements are trademarks of Warner Bros. Entertainment Inc.

---

## Deployment
Live demo: https://hogwartstechorda-production.up.railway.app


**Built with ❤️ and ☕ by Alisher Abden**