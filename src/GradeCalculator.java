import java.util.Scanner;

public class GradeCalculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("📘 Grade Calculator");
        System.out.print("Enter the number of subjects: ");

        int numSubjects;
        while (true) {
            if (scanner.hasNextInt()) {
                numSubjects = scanner.nextInt();
                if (numSubjects > 0) break;
            }
            System.out.print("Please enter a valid positive number: ");
            scanner.next();
        }

        int[] marks = new int[numSubjects];
        int totalMarks = 0;

        for (int i = 0; i < numSubjects; i++) {
            System.out.print("Enter marks for Subject " + (i + 1) + " (out of 100): ");
            while (true) {
                if (scanner.hasNextInt()) {
                    int mark = scanner.nextInt();
                    if (mark >= 0 && mark <= 100) {
                        marks[i] = mark;
                        totalMarks += mark;
                        break;
                    }
                }
                System.out.print("Invalid input! Enter a number between 0 and 100: ");
                scanner.next();
            }
        }

        double average = (double) totalMarks / numSubjects;
        String grade;

        // Grade calculation
        if (average >= 90) {
            grade = "A+";
        } else if (average >= 80) {
            grade = "A";
        } else if (average >= 70) {
            grade = "B";
        } else if (average >= 60) {
            grade = "C";
        } else if (average >= 50) {
            grade = "D";
        } else {
            grade = "F";
        }

        // Display Results
        System.out.println("\n📊 Result Summary:");
        System.out.println("Total Marks: " + totalMarks + " out of " + (numSubjects * 100));
        System.out.printf("Average Percentage: %.2f%%\n", average);
        System.out.println("Grade: " + grade);

        scanner.close();
    }
}
