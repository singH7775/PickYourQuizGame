# 🧠 QuizCraft

## 🌟 Overview
PickYourQuizGame is an innovative, full-stack web application that offers an engaging and challenging quiz experience. Built with Spring Boot and powered by OpenAI's API, this project demonstrates a robust combination of modern web technologies, database management, and AI integration.

## 🚀 Key Features
- **Dynamic Quiz Generation**: Utilizes OpenAI's API to create unique and challenging questions across various subjects.
- **Personalized Learning**: Users can select their preferred subjects for a tailored quiz experience.
- **Engaging Scoring System**: 
  - +10 points for correct answers
  - -20 points for incorrect answers
  - Win achieved at 100 points
  - Loss at 0 points
  - Automatic reset to 50 points after a win or loss
- **Comprehensive Quiz Sessions**: Each subject quiz consists of 10 questions.
- **Secure Authentication**: Implements basic auth for user security and personalized experiences.
- **Persistent Data Storage**: Utilizes MySQL for efficient data management and user progress tracking.

## 🛠 Technology Stack
- **Backend**: Spring Boot
- **Frontend**: Thymeleaf, HTML, CSS
- **Database**: MySQL
- **Security**: Basic Authentication
- **AI Integration**: OpenAI API
- **Containerization**: Docker

## 🏗 Architecture
The application follows a modern, scalable architecture:
1. **Presentation Layer**: Thymeleaf templates render dynamic HTML views.
2. **Application Layer**: Spring Boot handles business logic and API integration.
3. **Data Layer**: MySQL database for persistent storage.
4. **Security Layer**: Basic auth ensures secure access to user data.
5. **AI Layer**: OpenAI API integration for intelligent question generation.

## 🚀 Getting Started

### Prerequisites
- Docker and Docker Compose
- Your own OpenAI API key

### Setup and Running
1. Clone the repository:
   ```
   git clone https://github.com/singH7775/PickYourQuizGame.git
   ```
2. Navigate to the project directory:
   ```
   cd PickYourQuizGame
   ```
3. Set up your environment variables:
   - Create a `.env` file in the root directory
   - Add your OpenAI API key: `OPENAI_API_KEY=your_api_key_here`
4. Build and run with Docker Compose:
   ```
   docker compose up --build
   ```
5. Access the application at `http://localhost:8080`

## 🧪 Testing
- Comprehensive unit tests for backend logic
- Integration tests for database operations
- End-to-end tests for critical user journeys

## 🛣 Roadmap
- Implement user performance analytics
- Add multiplayer quiz mode
- Integrate more AI-driven features for adaptive difficulty

## 📞 Contact
harsingh10000@gmail.com

## 🙏 Acknowledgements
- [Spring Boot](https://spring.io/projects/spring-boot)
- [OpenAI](https://openai.com/)
- [Docker](https://www.docker.com/)
- [MySQL](https://www.mysql.com/)
- [Thymeleaf](https://www.thymeleaf.org/)
