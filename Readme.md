# Book Search Engine

A full-stack application for searching books. It uses a Spring Boot backend and a React.js frontend. The frontend primarily serves as a demonstration to showcase the effectiveness of the PostgreSQL GIN index for full-text searching.

## Technologies

### Backend
* Java (JDK 21)
* Spring Boot (3.x)
* Maven
* PostgreSQL
* Spring Data JPA
* Flyway (database migrations)
* Lombok
* Spring Web (REST API)
* Spring Validation
* OpenCSV

### Frontend
* React.js
* Vite
* Tailwind CSS

## Features

* **Advanced Book Search:** Leverages PostgreSQL Full-Text Search (using a GIN index applied on `title`, `description`, and `isbn` fields) for fast and relevant search results.
* **Efficient ISBN Lookup:** Utilizes a separate B-tree index on the `isbn` field for rapid lookups and uniqueness checks.
* **RESTful API:** A well-defined backend API for managing both book and author data.
* **Database Migrations:** Uses Flyway for robust and version-controlled database schema management.

## Setup and Installation

### Prerequisites
* Java Development Kit (JDK) 21+
* Maven 3.x
* Node.js (LTS version, includes npm)
* PostgreSQL database server

### Steps

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/2016mehrab/book-search-engine.git
    cd book-search-engine
    ```

2.  **Database Setup:**
    * Create a PostgreSQL database (e.g., `book_db`).
    * Update database connection details in `src/main/resources/application.properties` (or `application.yml`).

3.  **Build Backend:**
    * From the project root:
        ```bash
        mvn clean install 
        ```

4.  **Build Frontend:**
    * Navigate to the `client` directory:
        ```bash
        cd client
        ```
    * Install dependencies:
        ```bash
        npm install
        ```
    * Build the production version:
        ```bash
        npm run build
        ```
    * Go back to the project root:
        ```bash
        cd ..
        ```

## How to Run

### 1. Development Mode (Backend and Frontend Separately)

This mode is ideal for active development, offering hot-reloading for the frontend and faster backend restarts.

* **Run Backend API:**
    * Open a terminal in the project root (`book-search-engine`).
    * Start the Spring Boot application:
        ```bash
        mvn spring-boot:run
        ```
    * The API will be available at `http://localhost:8080` (default port).

* **Run Frontend Development Server:**
    * Open a **new** terminal.
    * Navigate into the `client` directory:
        ```bash
        cd client
        ```
    * Start the Vite development server:
        ```bash
        npm run dev
        ```
    * The frontend will be accessible at `http://localhost:5173` (or the port configured in `client/vite.config.js`).

### 2. Production Mode (Single Executable JAR)

This mode packages the entire application (backend API + pre-built frontend) into a single JAR file for easy deployment.

* **Package the Application:**
    * Ensure you have completed the "Frontend Build" step (`npm run build` in `client/`) recently.
    * From the project root (`book-search-engine`):
        ```bash
        mvn clean package
        ```
* **Run the JAR:**
    * Navigate to the `target` directory:
        ```bash
        cd target
        ```
    * Execute the JAR file (replace `book-search-engine-0.0.1-SNAPSHOT.jar` with your actual generated file name):
        ```bash
        java -jar book-search-engine-0.0.1-SNAPSHOT.jar
        ```
    * Access the application in your browser at `http://localhost:8080`.

## API Endpoints

### Books
* `GET /api/books`: Search for books.
    * Query Parameters: `search={searchTerm}`, `page={pageNumber}`, `pageSize={size}`
    * Example: `http://localhost:8080/api/books?search=harry+potter&page=0&pageSize=10`
* `GET /api/books/{isbn}`: Retrieve a single book by its ISBN.
* `POST /api/books`: Create a new book (requires a BookRequest JSON body).
* `DELETE /api/books/{isbn}`: Delete a book by its ISBN.

### Authors
* `GET /api/v1/authors`: Get a paginated list of all authors.
    * Query Parameters: `page={pageNumber}`, `pageSize={size}`
* `GET /api/v1/authors/{authorName}/books`: Get a paginated list of books by a specific author.
* `POST /api/v1/authors`: Create a new author (requires an AuthorRequest JSON body).
* `DELETE /api/v1/authors/{authorName}`: Delete an author by their name.