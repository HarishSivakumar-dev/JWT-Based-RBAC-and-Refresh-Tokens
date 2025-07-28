# JWT-Based-RBAC-and-Refresh-Tokens
Spring Boot backend with Role-Based Access Control (RBAC) and JWT authentication. Supports refresh tokens, custom login flow, and @PreAuthorize for role-based method security. Built using Spring Security, Spring Data JPA, and MySQL. Ensures secure and scalable access control.


This project is a backend application built using Spring Boot, focused on implementing secure authentication and authorization. It features Role-Based Access Control (RBAC) where users are assigned specific roles such as USER, ADMIN, etc., and access to APIs is restricted based on these roles using Spring Security’s @PreAuthorize annotations.

The authentication mechanism is powered by JWT (JSON Web Tokens) for stateless sessions. Once a user logs in, they receive an access token and a refresh token. The access token is used for authorization, while the refresh token can be used to generate a new access token when the previous one expires, without requiring the user to log in again.

To provide additional control, the project includes a custom authentication provider that directly verifies user credentials from the database, instead of using default in-memory authentication. This makes the project suitable for real-world use where user data is persisted and managed securely.

The project follows a stateless session policy, ensuring no session data is stored on the server, making it highly scalable and aligned with RESTful best practices. MySQL is used as the database, and Spring Data JPA handles the persistence logic.

This repository serves as a solid foundation for anyone building a secure backend with role-based access, token-based authentication, and refresh token flows.
