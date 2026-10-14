# Student Management System

This is a simple, desktop-based Student Management System application built using **JavaFX** and managed by **Maven**. The application performs standard **CRUD** (Create, Read, Update, Delete) operations and uses a CSV file for data persistence.

##  Features

* **Add Student:** Input fields for Name, ID, Department, Email, and CGPA.
<img width="791" height="623" alt="Image" src="https://github.com/user-attachments/assets/a026e77c-701e-4a0e-8995-4e1533c34224" />

* **View/Display:** Shows all student records in a dynamic TableView.
<img width="807" height="457" alt="Image" src="https://github.com/user-attachments/assets/e64c3d62-cb5b-402b-a1fe-ff65304f9a44" />

* **Update Student:** Allows editing of selected student details (except ID).
<img width="800" height="627" alt="Image" src="https://github.com/user-attachments/assets/7f1ef9e9-6962-4e3c-a4f2-ae279eb0fa7c" />

* **Delete Student:** Allows removal of selected student records.
<img width="806" height="635" alt="Image" src="https://github.com/user-attachments/assets/64bc3700-f790-41a7-a1ac-006cd7da9819" />

* **Search:** Filters the table instantly by Name, ID, or Department.
<img width="802" height="622" alt="Image" src="https://github.com/user-attachments/assets/1b8c7192-c2df-4457-9d96-fabf6e1d7583" />

* **Data Storage:** Uses a local **`students.csv`** file for saving data.
<img width="580" height="139" alt="Image" src="https://github.com/user-attachments/assets/8ccf8ebc-a67f-4eb1-8000-6e1278d629e1" />

##  Technology Stack

| Component | Technology | Role |
| :--- | :--- | :--- |
| **GUI Framework** | JavaFX 21 (OpenJFX) | Creates the desktop interface. |
| **Build Tool** | Apache Maven | Manages dependencies and provides the runnable JavaFX plugin. |
| **Language** | Java 17+ | Core programming language. |
| **Data Storage** | CSV File | Simple file-based persistence (`students.csv`). |

## 🚀 How to Run the Project

Since this is a Maven project, running it is very simple using the Maven Wrapper (`mvnw`).

### Prerequisites

1.  **JDK 17 or higher** installed on your system.
2.  **IntelliJ IDEA** or any IDE with Maven support.

### Running from Terminal

1.  Clone or download the project folder.
2.  Open your terminal (Command Prompt/PowerShell) inside the project root directory (where `pom.xml` is located).
3.  Execute the following command:

    ```bash
    .\mvnw clean javafx:run
    ```

    * This command will automatically download the necessary dependencies and launch the JavaFX application window.

##  Project Structure and Data Flow

The application logic is clearly separated among three main files:

| File Name | Role in the Application |
| :--- | :--- |
| **`MainController.java`** | **Logic Layer:** Handles all button events, form validation, and data manipulation (CRUD). **This is the brain of the application.** |
| **`main_view.fxml`** | **View Layer:** Defines the GUI layout, visual elements, and links them to the `MainController` using `fx:controller` and `onAction`. |
| **`Student.java`** | **Model Layer:** Defines the Student object. Uses JavaFX Properties to ensure the TableView updates immediately when data is changed. |

### Data Persistence Details

The application saves all student records to a file named **`students.csv`** located in the project's root folder.

* **Loading:** When the app starts, the `loadStudentsFromCSV()` method reads the file and populates the table.
* **Saving:** Every time a student is added, updated, or deleted, the `saveStudentsToCSV()` method is called to **overwrite** the file with the complete, current list of students.

## Code Highlights

### Table Refresh & Data Binding

The TableView updates dynamically because the `Student` class uses **JavaFX Properties** (e.g., `StringProperty`, `DoubleProperty`).

Search Logic
The search filter is applied instantly using a Listener on the searchField's text property:

// Example in MainController.java's initialize method
searchField.textProperty().addListener((observable, oldValue, newValue) -> {
    filterStudents(newValue);
});
