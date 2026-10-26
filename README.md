# University Examination Management System

A desktop-based examination management system built with **Java Swing**. It lets you register students and subjects, enter and modify marks, and automatically generate results with grades.

## Features

- Student registration (ID, name, department, year)
- Subject registration (ID, name, maximum marks)
- Marks entry and modification (entering the same student and subject again updates the mark)
- Input validation (empty fields, duplicate IDs, marks within the allowed range)
- Automatic total, average and grade calculation
- Search a student's result by Student ID
- Results shown in tables
- Data saved locally and loaded automatically on start-up

## Concepts Used

- **Components:** JFrame, JPanel, JTextField, JComboBox, JTable, JButton, JMenuBar, JMenu, JMenuItem, JTabbedPane
- **Layout managers:** BorderLayout, GridLayout, FlowLayout
- **Events:** ActionListener (buttons and menus), KeyAdapter (marks input), WindowAdapter (save on close)
- **Storage:** Java serialization

## Grading Scale

| Average | Grade |
|---------|-------|
| 90 and above | A+ |
| 80 – 89.99 | A |
| 70 – 79.99 | B |
| 60 – 69.99 | C |
| 50 – 59.99 | D |
| Below 50 | F |

## Requirements

- JDK 8 or newer
- Windows, Linux or macOS

## How to Run

```bash
javac UniversityExaminationManagementSystem.java
java UniversityExaminationManagementSystem
```

Data is saved in `exam_system_data.ser`, which is created in the folder where you run the program.

## Screenshots

**Student Registration**

![Student Registration](screenshots/1-student-registration.png)

**Modifying an existing mark**

![Marks Modification](screenshots/2-marks-modification.png)

**Auto-generated results**

![Results](screenshots/3-results.png)

**Search result by Student ID**

![Result Search](screenshots/4-result-search.png)
