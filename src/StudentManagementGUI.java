import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StudentManagementGUI extends JFrame {

    static class Student implements Serializable {
        private String name;
        private String rollNumber;
        private String grade;
        private String email;

        public Student(String name, String rollNumber, String grade, String email) {
            this.name = name;
            this.rollNumber = rollNumber;
            this.grade = grade;
            this.email = email;
        }

        public String getRollNumber() {
            return rollNumber;
        }

        @Override
        public String toString() {
            return "🧑 Name: " + name + " | 🎓 Roll No: " + rollNumber + " | 🏅 Grade: " + grade + " | 📧 Email: " + email;
        }
    }

    private static final String FILE_PATH = "students.dat";
    private List<Student> students = new ArrayList<>();

    private JTextField nameField, rollField, gradeField, emailField;
    private JTextField searchField, removeField;
    private JTextArea outputArea;

    public StudentManagementGUI() {
        setTitle("🎓 Student Management System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 255));

        loadFromFile();

        // ========== Top Panel for Adding Student ==========
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("➕ Add New Student"));
        inputPanel.setBackground(new Color(220, 240, 255));

        nameField = new JTextField();
        rollField = new JTextField();
        gradeField = new JTextField();
        emailField = new JTextField();

        inputPanel.add(new JLabel("🧑 Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("🎓 Roll Number:"));
        inputPanel.add(rollField);
        inputPanel.add(new JLabel("🏅 Grade:"));
        inputPanel.add(gradeField);
        inputPanel.add(new JLabel("📧 Email:"));
        inputPanel.add(emailField);

        JButton addButton = new JButton("➕ Add Student");
        addButton.setBackground(Color.GREEN);
        addButton.setForeground(Color.BLACK);
        addButton.addActionListener(e -> addStudent());

        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);

        // ========== Center Panel to Display Area ==========
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setBackground(new Color(255, 255, 230));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("📋 Student Records"));
        add(scrollPane, BorderLayout.CENTER);

        // ========== Bottom Panel ==========
        JPanel bottomPanel = new JPanel(new GridLayout(2, 4, 10, 5));
        bottomPanel.setBorder(BorderFactory.createTitledBorder("🔍 Search / ❌ Remove / 📃 Display"));
        bottomPanel.setBackground(new Color(240, 255, 240));

        searchField = new JTextField();
        removeField = new JTextField();

        JButton searchButton = new JButton("🔍 Search");
        searchButton.setBackground(new Color(173, 216, 230));
        searchButton.addActionListener(e -> searchStudent());

        JButton removeButton = new JButton("❌ Remove");
        removeButton.setBackground(new Color(255, 160, 122));
        removeButton.addActionListener(e -> removeStudent());

        JButton displayButton = new JButton("📃 Display All");
        displayButton.setBackground(new Color(255, 255, 153));
        displayButton.addActionListener(e -> displayAllStudents());

        bottomPanel.add(new JLabel("🔍 Roll No to Search:"));
        bottomPanel.add(searchField);
        bottomPanel.add(searchButton);

        bottomPanel.add(new JLabel("❌ Roll No to Remove:"));
        bottomPanel.add(removeField);
        bottomPanel.add(removeButton);

        bottomPanel.add(new JLabel(""));
        bottomPanel.add(displayButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addStudent() {
        String name = nameField.getText().trim();
        String roll = rollField.getText().trim();
        String grade = gradeField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || roll.isEmpty() || grade.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ All fields are required!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        students.add(new Student(name, roll, grade, email));
        saveToFile();
        JOptionPane.showMessageDialog(this, "✅ Student added successfully!");
        clearInputFields();
        displayAllStudents();
    }

    private void searchStudent() {
        String roll = searchField.getText().trim();
        for (Student s : students) {
            if (s.getRollNumber().equalsIgnoreCase(roll)) {
                outputArea.setText("🎯 Student Found:\n" + s);
                return;
            }
        }
        outputArea.setText("❌ Student not found.");
    }

    private void removeStudent() {
        String rollToRemove = removeField.getText().trim();
        if (rollToRemove.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Enter roll number to remove!");
            return;
        }

        Iterator<Student> iterator = students.iterator();
        boolean removed = false;
        while (iterator.hasNext()) {
            Student s = iterator.next();
            if (s.getRollNumber().equalsIgnoreCase(rollToRemove)) {
                iterator.remove();
                removed = true;
                break;
            }
        }

        if (removed) {
            saveToFile();
            JOptionPane.showMessageDialog(this, "✅ Student removed successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "❌ Student not found.");
        }

        displayAllStudents();
    }

    private void displayAllStudents() {
        if (students.isEmpty()) {
            outputArea.setText("⚠️ No student records found.");
        } else {
            StringBuilder sb = new StringBuilder("📚 All Students:\n\n");
            for (Student s : students) {
                sb.append(s).append("\n");
            }
            outputArea.setText(sb.toString());
        }
    }

    private void clearInputFields() {
        nameField.setText("");
        rollField.setText("");
        gradeField.setText("");
        emailField.setText("");
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(students);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "❌ Error saving data: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            students = (List<Student>) ois.readObject();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error loading data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentManagementGUI frame = new StudentManagementGUI();
            frame.setVisible(true);
        });
    }
}
