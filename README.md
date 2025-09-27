# LoLStats – Engineering Thesis Project

LoLStats is a web application for analyzing and presenting champion statistics from *League of Legends*.  
This project was developed as part of an engineering thesis and uses modern frontend and backend technologies.

---

## 🎯 Project Objective

The aim of this thesis was the **design and implementation of a web platform** that retrieves gameplay data via **APIs**.  
The platform includes:

- Data retrieval, validation, and updates in a **NoSQL database (CouchDB)**  
- A **clear and intuitive user interface** for presenting data  
- **Graphical visualizations** of match statistics  
- **Personalization and filtering** of data based on user criteria  

---

## 🛠️ Technologies Used

- **Vite** – Fast development environment for React  
- **React** – User interface  
- **NoSQL (CouchDB)** – Database for backend  
- **Spring Boot** – Java backend  
- **Docker** – Containerization of the app and database  
- **ESLint** – Static code analysis for JavaScript/React  
- **GitHub** – Version control and collaboration  
- **UML** – Database and system architecture design  
- **Gradle** – Java backend build tool  
- **Postman** – API testing  

---

## 📂 Project Structure

```
LolStats/
├── Frontend/           # React + Vite frontend app
│   └── frontend/
│       ├── src/
│       ├── public/
│       ├── package.json
│       ├── vite.config.js
│       └── README.md
├── SpringBootBackend/  # Java + CouchDB backend
│   └── src/
│   ├── build.gradle
│   └── settings.gradle
├── Docker/             # docker-compose files for running the project
│   └── docker-compose.yaml
├── UML/                # UML diagrams for database and system
│   └── DatabaseUML.puml
└── HELP.md             # Additional documentation
```

---

## 🚀 How to Run

### Frontend
```sh
cd Frontend/frontend
npm install
npm run dev
```

### Backend
```sh
cd SpringBootBackend
./gradlew bootRun
```

### Docker (recommended)
```sh
docker-compose -f Docker/docker-compose.yaml up --build
```

---

## 🗄️ Database

- Uses **CouchDB** (NoSQL) for storing data about players, matches, and champions.  
- Database design is documented with UML diagrams in `UML/DatabaseUML.puml`.  

---

## ✅ Code Quality

- **ESLint + Prettier** enforce code quality and style in the frontend.  
- **Unit and integration tests** implemented in the Java backend.  

---

## 📌 Scope of Work (Thesis)

- Selection and integration of appropriate **APIs** for acquiring gameplay data  
- Implementation of mechanisms for data retrieval from external sources  
- Design and implementation of a **NoSQL database**  
- Validation and update mechanisms ensuring **data consistency**  
- User-friendly **interface for presenting statistics**  
- **Graphical visualization** of results  
- Performance testing and optimization of the application  

---

## 👤 Author

Project created as part of an **Engineering Thesis**.  

---

💡 *For suggestions, improvements, or bug reports, please use Issues on GitHub.*
