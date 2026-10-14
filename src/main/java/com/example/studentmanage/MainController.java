package com.example.studentmanage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

// all features
public class MainController implements Initializable {

    // --- FXML Elements ---
    @FXML private TextField nameField;
    @FXML private TextField idField;
    @FXML private ComboBox<String> deptComboBox;
    @FXML private TextField emailField;
    @FXML private TextField cgpaField;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private TextField searchField;
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> nameCol;
    @FXML private TableColumn<Student, String> idCol;
    @FXML private TableColumn<Student, String> deptCol;
    @FXML private TableColumn<Student, String> emailCol;
    @FXML private TableColumn<Student, Double> cgpaCol;

    // --- Data and Constants ---
    private ObservableList<Student> studentData;
    private static final String CSV_FILE = "students.csv"; // Data file name

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        studentData = FXCollections.observableArrayList();

        // Initialize ComboBox
        deptComboBox.setItems(FXCollections.observableArrayList("CSE", "EEE", "BBA", "LAW"));

        // Initialize TableColumns
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        deptCol.setCellValueFactory(new PropertyValueFactory<>("dept"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        cgpaCol.setCellValueFactory(new PropertyValueFactory<>("cgpa"));

        // Load data from CSV and showing bytable
        loadStudentsFromCSV();
        studentTable.setItems(studentData);

        // Add listener for table selection
        studentTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showStudentDetails(newValue));

        // Search Student
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterStudents(newValue);
        });

        handleClearFields();
    }

    // --- Data (CSV) ---

    private void loadStudentsFromCSV() {
        File file = new File(CSV_FILE);
        if (!file.exists()) { return; }

        studentData.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 5) {
                    Student s = new Student(values[0], values[1], values[2], values[3], Double.parseDouble(values[4]));
                    studentData.add(s);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            showErrorAlert("File Error", "Could not load student data from " + CSV_FILE);
        }
    }

    private void saveStudentsToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (Student student : studentData) {
                String line = student.getName() + "," +
                        student.getId() + "," +
                        student.getDept() + "," +
                        student.getEmail() + "," +
                        student.getCgpa();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            showErrorAlert("File Error", "Could not save student data to " + CSV_FILE);
        }
    }

    private void showErrorAlert(String title, String content) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(content);
        errorAlert.showAndWait();
    }

    private void showStudentDetails(Student student) {
        if (student != null) {
            nameField.setText(student.getName());
            idField.setText(student.getId());
            idField.setDisable(true);

            deptComboBox.getSelectionModel().select(student.getDept());
            emailField.setText(student.getEmail());
            cgpaField.setText(String.valueOf(student.getCgpa()));

            updateButton.setDisable(false);
            deleteButton.setDisable(false);
        } else {
            handleClearFields();
        }
    }

    // --- Functional Requirements (CRUD operation & Search) ---

    // 1. Add Student
    @FXML
    private void handleAddStudent() {
        if (idField.getText().isEmpty() || cgpaField.getText().isEmpty() || deptComboBox.getSelectionModel().isEmpty()) {
            showErrorAlert("Validation Error", "Please fill in all mandatory fields.");
            return;
        }

        if (studentData.stream().anyMatch(s -> s.getId().equals(idField.getText()))) {
            showErrorAlert("Validation Error", "Student ID already exists!");
            return;
        }

        try {
            double cgpa = Double.parseDouble(cgpaField.getText());
            Student newStudent = new Student(
                    nameField.getText(), idField.getText(),
                    deptComboBox.getSelectionModel().getSelectedItem(),
                    emailField.getText(), cgpa
            );

            studentData.add(newStudent);
            saveStudentsToCSV();
            handleClearFields();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Student added successfully!");
            alert.showAndWait();

        } catch (NumberFormatException e) {
            showErrorAlert("Input Error", "CGPA must be a valid number.");
        }
    }

    // 3. Update Student
    @FXML
    private void handleUpdateStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showErrorAlert("Selection Error", "Please select a student to update.");
            return;
        }

        try {
            double newCgpa = Double.parseDouble(cgpaField.getText());

            // Update the properties
            selectedStudent.setName(nameField.getText());
            selectedStudent.setDept(deptComboBox.getSelectionModel().getSelectedItem());
            selectedStudent.setEmail(emailField.getText());
            selectedStudent.setCgpa(newCgpa);

            // Force TableView refresh
            studentTable.getColumns().get(0).setVisible(false);
            studentTable.getColumns().get(0).setVisible(true);

            saveStudentsToCSV();
            handleClearFields();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Student updated successfully!");
            alert.showAndWait();

        } catch (NumberFormatException e) {
            showErrorAlert("Input Error", "CGPA must be a valid number.");
        }
    }

    // 4. Delete Student
    @FXML
    private void handleDeleteStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showErrorAlert("Selection Error", "Please select a student to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Student: " + selectedStudent.getName());
        alert.setContentText("Are you sure you want to permanently delete this student record?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            studentData.remove(selectedStudent);
            saveStudentsToCSV();
            handleClearFields();

            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setContentText("Student deleted successfully.");
            success.showAndWait();
        }
    }

    @FXML
    private void handleClearFields() {
        nameField.clear();
        idField.clear();
        idField.setDisable(false);
        deptComboBox.getSelectionModel().clearSelection();
        emailField.clear();
        cgpaField.clear();
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        studentTable.getSelectionModel().clearSelection();
    }

    // 5. Search Student
    // @FXML annotation add kora holo jate FXML button-er sathe link hoy
    @FXML
    private void handleSearch() {
        filterStudents(searchField.getText());
    }

    // @FXML annotation add kora holo jate FXML button-er sathe link hoy
    @FXML
    private void handleClearSearch() {
        searchField.clear();
        filterStudents("");
    }

    private void filterStudents(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            studentTable.setItems(studentData);
            return;
        }

        String lowerCaseFilter = searchText.toLowerCase();

        ObservableList<Student> filteredList = studentData.stream()
                .filter(student ->
                        student.getName().toLowerCase().contains(lowerCaseFilter) ||
                                student.getId().toLowerCase().contains(lowerCaseFilter) ||
                                student.getDept().toLowerCase().contains(lowerCaseFilter))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        studentTable.setItems(filteredList);
    }
}