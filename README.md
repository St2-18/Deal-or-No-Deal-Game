# Deal or No Deal 🎲💼

A JavaFX-based **Deal or No Deal** game with MySQL database connectivity.  
The game follows the classic rules where the player selects a box and progresses through multiple rounds while receiving dynamic offers from the Banker. At the end, the player can either keep their original box or swap it with the last remaining one.

---

## 📌 Features

- 🎮 Classic **Deal or No Deal** gameplay with **22 boxes**.
- 💰 5 Rounds:
  1. Select **5 boxes** → Banker Offer
  2. Select **5 boxes** → Banker Offer
  3. Select **5 boxes** → Banker Offer
  4. Select **3 boxes** → Banker Offer
  5. Select **2 boxes** → Final choice between original or last box
- 🏦 Banker offer logic: **dynamic calculation** encouraging continued play.
- 👤 Player name is entered at the start.
- 📊 MySQL Database stores:
  - Player name
  - Final amount won
- 🔁 Option to **play again** after the game ends.

---

## 📂 Project Structure

DealOrNoDeal/
│── src/main/java/
│ ├── com.dealornodeal/
│ │ ├── Main.java # Entry point
│ │ ├── GameController.java # Handles game logic & rounds
│ │ ├── UIController.java # JavaFX GUI
│ │ ├── Database.java # MySQL connectivity
│── src/main/resources/
│ ├── styles.css # JavaFX styles
│ ├── layout.fxml # JavaFX layout
│── pom.xml # Maven dependencies
│── README.md # Project documentation

---

## ⚙️ Prerequisites

Make sure you have installed:

- **Java 17+**
- **Maven**
- **MySQL** (running and accessible)

---

## 🛠️ Setup Instructions

### 1. Clone the repository

git clone https://github.com/YOUR-USERNAME/DealOrNoDeal.git
cd DealOrNoDeal

### 2. Configure MySQL

#### 1. Create a database:

CREATE DATABASE dealornodeal;

#### 2. Create a table to store results:

CREATE TABLE players (
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100),
amount_won INT
);

#### 3. Update your Database.java file with your MySQL credentials:

private static final String URL = "jdbc:mysql://localhost:3306/dealornodeal";
private static final String USER = "root";
private static final String PASSWORD = "your_password";

### 3. Build & Run

#### Using Maven:

mvn clean install
mvn javafx:run

#### Or directly from NetBeans (if using NetBeans, just run the project).

## Author

#### Developed by Harshit Sharma
