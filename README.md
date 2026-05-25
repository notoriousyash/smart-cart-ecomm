# 🛒 SmartCart E-Commerce

SmartCart is a modern, scalable e-commerce application built with a **Spring Boot Microservices** backend and a **Next.js** frontend. It features a clean, responsive UI for users to browse products, manage their cart, and place orders seamlessly.

## 🏗️ Architecture

The backend follows a microservices architecture, ensuring modularity and independent scalability.
- **Frontend**: Next.js, React, Tailwind CSS, TypeScript
- **Backend Microservices** (Java 17, Spring Boot 3):
  - **API Gateway**: Routes requests and handles global CORS configuration.
  - **Config Server**: Centralized configuration management.
  - **User Service**: Handles JWT-based authentication and user registration.
  - **Product Service**: Manages the product catalog and inventory.
  - **Order Service**: Handles checkout logic and communicates with the Product Service via Feign Client to automatically deduct inventory.
- **Database**: PostgreSQL (Each microservice has its own isolated database/schema).

## 🚀 Getting Started

### Prerequisites
- Java 17
- Node.js (v18+)
- PostgreSQL (running locally on port 5432)

### Backend Setup

1. **Start PostgreSQL** and ensure databases exist for the microservices (e.g., `user_db`, `product_db`, `order_db`).
2. **Run the Microservices**:
   Navigate into each microservice directory and run:
   ```bash
   mvn spring-boot:run
   ```
   *Note: Start the `config-server` first, followed by the `api-gateway`, and then the rest of the services.*

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd smartcart-frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run the development server:
   ```bash
   npm run dev
   ```
4. Open [http://localhost:3000](http://localhost:3000) in your browser.

## ✨ Features
- **Secure Authentication**: JWT-based stateless authentication.
- **Interactive Cart**: Slide-out cart drawer to review and adjust your items.
- **Automated Inventory**: Stock decreases automatically when an order is successfully placed.
- **Dynamic Routing**: Smooth client-side transitions via Next.js.
