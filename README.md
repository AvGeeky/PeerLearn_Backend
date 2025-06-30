# PeerLearn_Backend

Backend for the PeerLearn platform, built as an MVP during the Envision Hackathon at SSN College.

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [API Endpoints](#api-endpoints)
- [Project Structure](#project-structure)
- [Setup & Running](#setup--running)
- [License](#license)

---

## Overview

PeerLearn is a collaborative e-learning backend designed to facilitate course creation, enrollment, and progress tracking for students and teachers. This backend supports user authentication, course management, and detailed module/lesson handling.

---

## Tech Stack

- **Java** (Spring Boot)
- **MongoDB** (NoSQL Database)
- **Dotenv** for environment variable management
- **Maven** (for dependency management)
- **Deployed as a RESTful API**

---

## Features

- **User Authentication:** Verify and manage user credentials.
- **Course Management:** Create, retrieve, and manage courses, modules, and lessons.
- **Pinned & Top Courses:** Fetch pinned and top-rated courses for users.
- **Tag-based Search:** Retrieve courses based on selected tags.
- **User Profiles:** Fetch user details, including reputation, stars, and progress.
- **Module & Lesson Handling:** Add modules and lessons within courses, including quiz integration.
- **Reputation & Rewards:** Track stars, reputation, and course progress for users.
- **Teacher Profiles:** Track courses taught and reputation for teachers.
- **RESTful Endpoints:** CORS enabled for easy frontend integration.

---

## API Endpoints

Below are some key endpoints (see code for full details):

| Endpoint              | Method | Description                                  |
|---------------------- |--------|----------------------------------------------|
| `/setCredentials`     | POST   | Verify user login                            |
| `/getPinnedCourses`   | GET    | Fetch user's pinned courses                  |
| `/getUserDetails`     | GET    | Fetch detailed profile info for a user       |
| `/getTopCourses`      | GET    | Retrieve top 10 courses                      |
| `/getCoursesByTags`   | POST   | Retrieve courses matching given tags         |
| `/createCourse`       | GET    | Create a new course                          |
| `/createModule`       | POST   | Add a module to a course                     |
| `/createLesson`       | POST   | Add a lesson to a module                     |
| `/getModuleDetails`   | GET    | Get details of a module in a course          |
| `/getCoursesByName`   | GET    | Search courses by name                       |

*All endpoints are CORS-enabled.*

---

## Project Structure

```
PeerLearn_Backend/
└── login/
    └── src/
        └── main/
            └── java/
                └── com/
                    └── peerlearn/
                        └── controller/
                            └── RestController_v2.java
    └── functions.txt
```
- Main backend code: `login/src/main/java/com/peerlearn/controller/RestController_v2.java`
- Example utility code: `login/functions.txt`

---

## Setup & Running

1. **Clone the Repository**
   ```bash
   git clone https://github.com/techieRahul17/PeerLearn_Backend.git
   ```

2. **Configure Environment Variables**
   - Create an `.env` file or update `apiee.env` with your MongoDB connection string and other sensitive info.

3. **Build & Run**
   - Use Maven or your preferred Java build tool to compile and run the Spring Boot application.

4. **API Access**
   - Access the endpoints at the default Spring Boot port (e.g., `http://localhost:8080`).

---

## License

This project was developed as a hackathon MVP and may be subject to further changes. Please check with the repository owner for reuse or contributions.

---
