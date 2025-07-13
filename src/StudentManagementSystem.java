import java.io.*;
import java.util.*;

public class StudentManagementSystem {

    // Inner Student class to represent student data
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

        public void setName(String name) { this.name = name; }
        public void setGrade(String grade) { this.grade = grade; }
        public void setEmail(String email) { this.email = email; }

        @Override
        public String toString() {
            return "Name: " + name + ", Roll No: " + rollNumber +
                    ", Grade: " + grade + ", Email: " + email;
        }
    }

    private static final String FILE_PATH = "students.dat";
    private List<Student> students;

    public StudentManagementSystem() {
        students = new ArrayList<>();
        loadFromFile();
    }

    // Add new student
    public void addStudent(Student s) {
        students.add(s);
        saveToFile();
    }

    // Remove student by roll number
    public boolean removeStudent(String rollNumber) {
        Iterator<Student> iterator = students.iterator();
        while (iterator.hasNext()) {
            Student s = iterator.next();
            if (s.getRollNumber().equalsIgnoreCase(rollNumber)) {
                iterator.remove();
                saveToFile();
                return true;
            }
        }
        return false;
    }

    // Search for a student
    public Student searchStudent(String rollNumber) {
        for (Student s : students) {
            if (s.getRollNumber().equalsIgnoreCase(rollNumber)) {
                return s;
            }
        }
        return null;
    }

    // Display all students
    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records found.");
        } else {
            for (Student s : students) {
                System.out.println(s);
            }
        }
    }

    // Save list to file
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(students);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load list from file
    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            students = (List<Student>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    // Main method with simple console UI
    public static void main(String[] args) {
        StudentManagementSystem sms = new StudentManagementSystem();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n==== Student Management System ====");
            System.out.println("1. Add Student");
            System.out.println("2. Remove Student");
            System.out.println("3. Search Student");
            System.out.println("4. Display All Students");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Invalid input. Enter a number: ");
                sc.next();
            }

            choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine().trim();

                    System.out.print("Enter Roll Number: ");
                    String roll = sc.nextLine().trim();

                    System.out.print("Enter Grade: ");
                    String grade = sc.nextLine().trim();

                    System.out.print("Enter Email: ");
                    String email = sc.nextLine().trim();

                    if (name.isEmpty() || roll.isEmpty() || grade.isEmpty() || email.isEmpty()) {
                        System.out.println("⚠ All fields are required.");
                    } else {
                        sms.addStudent(new Student(name, roll, grade, email));
                        System.out.println("✅ Student added successfully.");
                    }
                    break;

                case 2:
                    System.out.print("Enter Roll Number to remove: ");
                    String rollToRemove = sc.nextLine().trim();
                    if (sms.removeStudent(rollToRemove)) {
                        System.out.println("✅ Student removed.");
                    } else {
                        System.out.println("❌ Student not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter Roll Number to search: ");
                    String rollToSearch = sc.nextLine().trim();
                    Student found = sms.searchStudent(rollToSearch);
                    if (found != null) {
                        System.out.println("🎯 Student Found: " + found);
                    } else {
                        System.out.println("❌ Student not found.");
                    }
                    break;

                case 4:
                    sms.displayAllStudents();
                    break;

                case 5:
                    System.out.prin
                tln("👋 Exiting system. Goodbye!");
                    break;

                default:
                    System.out.println("⚠ Invalid choice. Try again.");
            }

        } while (choice != 5);

        sc.close();
    }
}
