# ecommerce_app

Here’s an improved `README.md` for your project in English:

---

# 🛒 E-Commerce API

**E-Commerce API** is a fully functional **Spring Boot**-based backend system for managing an online store. It provides a complete set of APIs for handling products, orders, users, and authentication.

## 🚀 Features

- 🔐 **User Authentication & Registration** with **JWT Authentication**
- 🛍️ **Product Management** (Create, Update, Delete, View Products)
- 📦 **Order Management** (Create & Track Orders)
- 🛒 **Shopping Cart Management** (Add & Remove Items)
- 👥 **User Roles & Permissions** with **RBAC (Role-Based Access Control)**
- 📊 **API Documentation** with **Springdoc OpenAPI**

## 🛠️ Technologies Used

- **Java 21**
- **Spring Boot 3.4.3**
- **Spring Security & JWT**
- **Spring Data JPA & MySQL**
- **Swagger (Springdoc OpenAPI)**
- **Lombok & ModelMapper**

## 🏃 How to Run the Project

1️⃣ Ensure you have **Java 21**, **Maven**, and **MySQL** installed on your system.  
2️⃣ Create a MySQL database named **ecommerce_db**.  
3️⃣ Update the database connection settings in `application.properties`.  
4️⃣ Run the following commands in the terminal:

```bash
mvn clean install
mvn spring-boot:run
```

5️⃣ Open your browser and navigate to:
- 🔹 **API Documentation**: [http://localhost:8080/swagger-ui.html](http://localhost:8181/swagger-ui.html)

## ✨ Future Enhancements

- Add **payment gateway integration**
- Improve **recommendation system** using AI
- Support **product image uploads**

---

💡 **Contributions are welcome!** Feel free to open an **Issue** or submit a **Pull Request** to improve the project. 🚀