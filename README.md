# Demo Application

This is a **Spring Boot** application example.

## Summary

The Demo Application is a simple Spring Boot-based project designed to 
demonstrate how to integrate with external weather service rest APIs.  
The current implementations access NOAA weather service data. 
It is also contains GeoCode service integrated with  MapQuest Service.  
Note: You need to provide your own Map Quest API key


## Getting Started

These instructions will help you get a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java 17 or higher (compatible with your development setup).
- Maven installed.
- An IDE (such as IntelliJ IDEA, Eclipse, or VS Code) is recommended.

### Installing

1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Navigate into the project directory:
   ```bash
   cd demo-application
   ```
3. Import the project into your favorite IDE.
4. Ensure that dependencies are synced:
    - For Maven:
      ```bash
      mvn clean install
      ```
    
### Running the Application

1. Use the following command to run the application via Maven:
   ```bash
   mvn spring-boot:run
   ```

2. Alternatively, you can run the application by executing the `main` method in the `DemoApplication` class from your IDE.

The application will start on **http://localhost:8080** by default.

### Project Structure

- **`src/main/java`**: Contains the Java source code.
    - The entry point for the application is in `com.example.demo.DemoApplication`.
- **`src/main/resources`**: Contains configuration files such as `application.properties`.

### Built With

- [Spring Boot](https://spring.io/projects/spring-boot) - The framework for building Java applications.

### License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

### Additional Notes

If you encounter any issues, please ensure dependencies are properly loaded and your Java version matches the project's requirements.