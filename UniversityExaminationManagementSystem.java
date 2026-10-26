import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * University Examination Management System
 * Built with Java Swing. Data is stored locally in a serialized file.
 */
public class UniversityExaminationManagementSystem extends JFrame {
    private static final String DATA_FILE = "exam_system_data.ser";

    private final java.util.List<Student> students = new ArrayList<>();
    private final java.util.List<Subject> subjects = new ArrayList<>();
    private final java.util.List<MarkRecord> marks = new ArrayList<>();

    private JTextField studentIdField, studentNameField, studentYearField;
    private JComboBox<String> studentDeptCombo;
    private JTextField subjectIdField, subjectNameField, subjectMaxField;
    private JTextField marksStudentIdField, marksSubjectIdField, marksValueField;
    private JTextField searchIdField;
    private JTable studentTable, subjectTable, marksTable, resultTable;
    private JLabel statusLabel;

    public UniversityExaminationManagementSystem() {
        super("University Examination Management System");
        loadData();
        buildInterface();
        applyTheme();
        refreshAllTables();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveData();
                dispose();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1000, 650);
        setMinimumSize(new Dimension(820, 520));
        setLocationRelativeTo(null);
    }

    private void buildInterface() {
        setLayout(new BorderLayout(8, 8));
        setJMenuBar(createMenuBar());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Student Registration", createStudentPanel());
        tabs.addTab("Subject Registration", createSubjectPanel());
        tabs.addTab("Marks Entry / Modification", createMarksPanel());
        tabs.addTab("Result Search", createResultPanel());
        add(tabs, BorderLayout.CENTER);

        statusLabel = new JLabel(" Ready. Data is saved locally.");
        add(statusLabel, BorderLayout.SOUTH);
    }

    
    private void applyTheme() {
    Color navy = new Color(23, 54, 93);
    Color blue = new Color(37, 99, 235);
    Color background = new Color(243, 246, 250);
    Color white = Color.WHITE;
    Color text = new Color(35, 45, 60);
    Color border = new Color(215, 223, 233);

    getContentPane().setBackground(background);

    styleComponents(getContentPane(), navy, blue,
                    background, white, text, border);

    if (getJMenuBar() != null) {
        getJMenuBar().setBackground(navy);
        getJMenuBar().setForeground(white);

        for (int i = 0; i < getJMenuBar().getMenuCount(); i++) {
            JMenu menu = getJMenuBar().getMenu(i);

            if (menu != null) {
                menu.setForeground(white);
                menu.setBackground(navy);
                }
            }
        }
    }

    private void styleComponents(
        Container container,
        Color navy,
        Color blue,
        Color background,
        Color white,
        Color text,
        Color border) {

    for (Component component : container.getComponents()) {

        if (component instanceof JTable) {
            JTable table = (JTable) component;

            table.setBackground(white);
            table.setForeground(text);
            table.setGridColor(border);
            table.setRowHeight(28);
            table.setFont(new Font("SansSerif", Font.PLAIN, 13));
            table.setSelectionBackground(new Color(219, 234, 254));
            table.setSelectionForeground(text);

            table.getTableHeader().setBackground(navy);
            table.getTableHeader().setForeground(white);
            table.getTableHeader().setFont(
                    new Font("SansSerif", Font.BOLD, 13));
            table.getTableHeader().setPreferredSize(
                    new Dimension(0, 32));

        } else if (component instanceof JButton) {
            JButton button = (JButton) component;

            button.setFont(
                    new Font("SansSerif", Font.BOLD, 12));
            button.setFocusPainted(false);
            button.setOpaque(true);
            button.setBorderPainted(false);
            button.setBackground(blue);
            button.setForeground(white);
            button.setMargin(new Insets(8, 14, 8, 14));

            if (button.getText() != null
                    && button.getText().toLowerCase().contains("delete")) {
                button.setBackground(new Color(185, 48, 48));
            }

        } else if (component instanceof JTextField) {
            JTextField field = (JTextField) component;

            field.setBackground(white);
            field.setForeground(text);
            field.setCaretColor(navy);
            field.setFont(new Font("SansSerif", Font.PLAIN, 13));
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(border),
                    BorderFactory.createEmptyBorder(5, 7, 5, 7)));

        } else if (component instanceof JComboBox) {
            component.setBackground(white);
            component.setForeground(text);
            component.setFont(new Font("SansSerif", Font.PLAIN, 13));

        } else if (component instanceof JLabel) {
            component.setForeground(text);
            component.setFont(new Font("SansSerif", Font.PLAIN, 13));

        } else if (component instanceof JTabbedPane) {
            component.setBackground(background);
            component.setForeground(navy);

        } else if (component instanceof JPanel) {
            component.setBackground(background);
        }

        if (component instanceof Container) {
            styleComponents((Container) component, navy, blue,
                            background, white, text, border);
            }
        }
    }


    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save Data");
        JMenuItem exitItem = new JMenuItem("Exit");
        saveItem.addActionListener(e -> {
            saveData();
            setStatus("Data saved successfully.");
        });
        exitItem.addActionListener(e -> {
            saveData();
            dispose();
        });
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "University Examination Management System\nJava Swing mini-project",
                "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);

        bar.add(fileMenu);
        bar.add(helpMenu);
        return bar;
    }

    private JPanel createStudentPanel() {
        JPanel outer = new JPanel(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Student Details"));

        studentIdField = new JTextField();
        studentNameField = new JTextField();

        studentDeptCombo = new JComboBox<>(new String[]{
            "Select Department",
            "CSE",
            "CSD",
            "CSM",
            "ECE",
            "EEE",
            "IT",
            "Mechanical",
            "Civil",
            "Chemical",
        });

        studentYearField = new JTextField();

        addFormRow(form, "Student ID:", studentIdField);
        addFormRow(form, "Student Name:", studentNameField);
        addFormRow(form, "Department:", studentDeptCombo);
        addFormRow(form, "Year:", studentYearField);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Register Student");
        JButton deleteButton = new JButton("Delete Student");
        JButton clearButton = new JButton("Clear");
        addButton.addActionListener(e -> registerStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearButton.addActionListener(e -> clearStudentForm());
        buttons.add(addButton);
        buttons.add(deleteButton);
        buttons.add(clearButton);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        studentTable = makeTable(new String[]{"Student ID", "Name", "Department", "Year"});
        outer.add(top, BorderLayout.NORTH);
        outer.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        return outer;
    }

    private JPanel createSubjectPanel() {
        JPanel outer = new JPanel(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Subject Details"));

        subjectIdField = new JTextField();
        subjectNameField = new JTextField();
        subjectMaxField = new JTextField("100");

        addFormRow(form, "Subject ID:", subjectIdField);
        addFormRow(form, "Subject Name:", subjectNameField);
        addFormRow(form, "Maximum Marks:", subjectMaxField);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Register Subject");
        JButton deleteButton = new JButton("Delete Subject");
        JButton clearButton = new JButton("Clear");
        addButton.addActionListener(e -> registerSubject());
        deleteButton.addActionListener(e -> deleteSubject());
        clearButton.addActionListener(e -> clearSubjectForm());
        buttons.add(addButton);
        buttons.add(deleteButton);
        buttons.add(clearButton);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        subjectTable = makeTable(new String[]{"Subject ID", "Subject Name", "Maximum Marks"});
        outer.add(top, BorderLayout.NORTH);
        outer.add(new JScrollPane(subjectTable), BorderLayout.CENTER);
        return outer;
    }

    private JPanel createMarksPanel() {
        JPanel outer = new JPanel(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Enter or Modify Marks"));

        marksStudentIdField = new JTextField();
        marksSubjectIdField = new JTextField();
        marksValueField = new JTextField();

        addFormRow(form, "Student ID:", marksStudentIdField);
        addFormRow(form, "Subject ID:", marksSubjectIdField);
        addFormRow(form, "Marks Obtained:", marksValueField);

        // Key events provide early input validation; final validation occurs before saving.
        marksValueField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && !Character.isISOControl(c)) {
                    e.consume();
                }
            }
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton saveMarksButton = new JButton("Save / Modify Marks");
        JButton clearButton = new JButton("Clear");
        saveMarksButton.addActionListener(e -> saveMarks());
        clearButton.addActionListener(e -> clearMarksForm());
        buttons.add(saveMarksButton);
        buttons.add(clearButton);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        marksTable = makeTable(new String[]{"Student ID", "Student Name", "Subject ID", "Subject Name", "Marks", "Maximum"});
        outer.add(top, BorderLayout.NORTH);
        outer.add(new JScrollPane(marksTable), BorderLayout.CENTER);
        JLabel note = new JLabel(" Enter the same Student ID and Subject ID to modify an existing mark.");
        outer.add(note, BorderLayout.SOUTH);
        return outer;
    }

    private JPanel createResultPanel() {
        JPanel outer = new JPanel(new BorderLayout(8, 8));
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        search.setBorder(BorderFactory.createTitledBorder("Search Student Result"));
        searchIdField = new JTextField(18);
        JButton searchButton = new JButton("Search by Student ID");
        JButton allResultsButton = new JButton("Show All Results");
        searchButton.addActionListener(e -> searchResult());
        allResultsButton.addActionListener(e -> showAllResults());
        searchIdField.addActionListener(e -> searchResult());
        search.add(new JLabel("Student ID:"));
        search.add(searchIdField);
        search.add(searchButton);
        search.add(allResultsButton);

        resultTable = makeTable(new String[]{"Student ID", "Student Name", "Subjects Recorded", "Total Marks", "Average", "Grade"});
        outer.add(search, BorderLayout.NORTH);
        outer.add(new JScrollPane(resultTable), BorderLayout.CENTER);
        return outer;
    }

    private void addFormRow(JPanel panel, String label, JComponent field) {
        panel.add(new JLabel(label));
        panel.add(field);
    }

    
    private JTable makeTable(String[] columns) {
    DefaultTableModel model = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    JTable table = new JTable(model);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setAutoCreateRowSorter(true);

    // Improve table appearance
    table.setRowHeight(30);
    table.setShowGrid(true);
    table.setIntercellSpacing(new Dimension(1, 1));
    table.setFillsViewportHeight(true);

    // Keep text clear and readable
    table.setFont(new Font("SansSerif", Font.PLAIN, 13));

    // Style column headings
    table.getTableHeader().setReorderingAllowed(false);

    // Keep the existing sorting feature
    table.setAutoCreateRowSorter(true);

    // Highlight selected rows
    table.setSelectionBackground(new Color(219, 234, 254));
    table.setSelectionForeground(new Color(23, 54, 93));

    return table;
    }


    private void registerStudent() {
        String id = studentIdField.getText().trim();
        String name = studentNameField.getText().trim();
        String dept = (String) studentDeptCombo.getSelectedItem();
        String year = studentYearField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || studentDeptCombo.getSelectedIndex() == 0 || year.isEmpty()) {
            showError("Please fill in all student details.");
            return;
        }

         if(!year.equals("1") && !year.equals("2") && !year.equals("3") && !year.equals("4") )
        {
            showError( "Year must be 1,2,3 or 4.");
            return;
        }

        if (findStudent(id) != null) {
            showError("A student with this ID is already registered.");
            return;
        }
        students.add(new Student(id, name, dept, year));
        saveData();
        refreshAllTables();
        clearStudentForm();
        setStatus("Student registered.");
    }

    private void deleteStudent() {
    int selectedRow = studentTable.getSelectedRow();

    if (selectedRow == -1) {
        showError("Please select a student to delete.");
        return;
    }

    int modelRow = studentTable.convertRowIndexToModel(selectedRow);

    String id = studentTable.getModel()
            .getValueAt(modelRow, 0).toString();

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete student " + id + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    students.removeIf(s -> s.id.equalsIgnoreCase(id));

    // Also remove this student's marks
    marks.removeIf(m -> m.studentId.equalsIgnoreCase(id));

    saveData();
    refreshAllTables();

    setStatus("Student deleted successfully.");
    }

    private void registerSubject() {
        String id = subjectIdField.getText().trim();
        String name = subjectNameField.getText().trim();
        String maxText = subjectMaxField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || maxText.isEmpty()) {
            showError("Please fill in all subject details.");
            return;
        }
        if (findSubject(id) != null) {
            showError("A subject with this ID is already registered.");
            return;
        }
        int max;
        try {
            max = Integer.parseInt(maxText);
        } catch (NumberFormatException ex) {
            showError("Maximum marks must be a whole number.");
            return;
        }
        if (max <= 0) {
            showError("Maximum marks must be greater than zero.");
            return;
        }
        subjects.add(new Subject(id, name, max));
        saveData();
        refreshAllTables();
        clearSubjectForm();
        setStatus("Subject registered.");
    }

    private void deleteSubject() {
    int selectedRow = subjectTable.getSelectedRow();

    if (selectedRow == -1) {
        showError("Please select a subject to delete.");
        return;
    }

    int modelRow = subjectTable.convertRowIndexToModel(selectedRow);

    String id = subjectTable.getModel()
            .getValueAt(modelRow, 0).toString();

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete subject " + id + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    subjects.removeIf(s -> s.id.equalsIgnoreCase(id));

    // Also remove marks recorded for this subject
    marks.removeIf(m -> m.subjectId.equalsIgnoreCase(id));

    saveData();
    refreshAllTables();

    setStatus("Subject deleted successfully.");
    }

    private void saveMarks() {
        String studentId = marksStudentIdField.getText().trim();
        String subjectId = marksSubjectIdField.getText().trim();
        String valueText = marksValueField.getText().trim();

        if (studentId.isEmpty() || subjectId.isEmpty() || valueText.isEmpty()) {
            showError("Enter Student ID, Subject ID and marks.");
            return;
        }
        Student student = findStudent(studentId);
        Subject subject = findSubject(subjectId);
        if (student == null) {
            showError("Student ID not found. Register the student first.");
            return;
        }
        if (subject == null) {
            showError("Subject ID not found. Register the subject first.");
            return;
        }

        double value;
        try {
            value = Double.parseDouble(valueText);
        } catch (NumberFormatException ex) {
            showError("Marks must be a number.");
            return;
        }
        if (value < 0 || value > subject.maxMarks) {
            showError("Marks must be between 0 and " + subject.maxMarks + ".");
            return;
        }

        MarkRecord record = findMark(studentId, subjectId);
        if (record == null) {
            marks.add(new MarkRecord(studentId, subjectId, value));
            setStatus("Marks entered.");
        } else {
            record.marks = value;
            setStatus("Marks modified.");
        }
        saveData();
        refreshAllTables();
        clearMarksForm();
    }

    private void searchResult() {
        String id = searchIdField.getText().trim();
        if (id.isEmpty()) {
            showError("Enter a Student ID to search.");
            return;
        }
        Student student = findStudent(id);
        if (student == null) {
            showError("No student found with ID: " + id);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
        model.setRowCount(0);
        addResultRow(model, student);
    }

    private void showAllResults() {
        DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
        model.setRowCount(0);
        for (Student student : students) addResultRow(model, student);
    }

    private void addResultRow(DefaultTableModel model, Student student) {
        double total = 0;
        int count = 0;
        for (MarkRecord record : marks) {
            if (record.studentId.equalsIgnoreCase(student.id)) {
                total += record.marks;
                count++;
            }
        }
        double average = count == 0 ? 0 : total / count;
        String grade = count == 0 ? "N/A" : calculateGrade(average);
        model.addRow(new Object[]{
                student.id, student.name, count,
                String.format(Locale.US, "%.2f", total),
                String.format(Locale.US, "%.2f", average), grade
        });
    }

    private String calculateGrade(double average) {
        if (average >= 90) return "A+";
        if (average >= 80) return "A";
        if (average >= 70) return "B";
        if (average >= 60) return "C";
        if (average >= 50) return "D";
        return "F";
    }

    private Student findStudent(String id) {
        for (Student s : students) if (s.id.equalsIgnoreCase(id)) return s;
        return null;
    }

    private Subject findSubject(String id) {
        for (Subject s : subjects) if (s.id.equalsIgnoreCase(id)) return s;
        return null;
    }

    private MarkRecord findMark(String studentId, String subjectId) {
        for (MarkRecord m : marks) {
            if (m.studentId.equalsIgnoreCase(studentId) &&
                m.subjectId.equalsIgnoreCase(subjectId)) return m;
        }
        return null;
    }

    private void refreshAllTables() {
        if (studentTable != null) {
            DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
            model.setRowCount(0);
            for (Student s : students) model.addRow(new Object[]{s.id, s.name, s.department, s.year});
        }
        if (subjectTable != null) {
            DefaultTableModel model = (DefaultTableModel) subjectTable.getModel();
            model.setRowCount(0);
            for (Subject s : subjects) model.addRow(new Object[]{s.id, s.name, s.maxMarks});
        }
        if (marksTable != null) {
            DefaultTableModel model = (DefaultTableModel) marksTable.getModel();
            model.setRowCount(0);
            for (MarkRecord m : marks) {
                Student s = findStudent(m.studentId);
                Subject sub = findSubject(m.subjectId);
                if (s != null && sub != null) {
                    model.addRow(new Object[]{s.id, s.name, sub.id, sub.name, m.marks, sub.maxMarks});
                }
            }
        }
        if (resultTable != null) showAllResults();
    }

    private void clearStudentForm() {
        studentIdField.setText("");
        studentNameField.setText("");
        studentDeptCombo.setSelectedIndex(0);
        studentYearField.setText("");
    }

    private void clearSubjectForm() {
        subjectIdField.setText("");
        subjectNameField.setText("");
        subjectMaxField.setText("100");
    }

    private void clearMarksForm() {
        marksStudentIdField.setText("");
        marksSubjectIdField.setText("");
        marksValueField.setText("");
    }

    private void setStatus(String message) {
        statusLabel.setText(" " + message);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation", JOptionPane.WARNING_MESSAGE);
    }

    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(new AppData(students, subjects, marks));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not save data: " + ex.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            AppData data = (AppData) in.readObject();
            students.addAll(data.students);
            subjects.addAll(data.subjects);
            marks.addAll(data.marks);
        } catch (IOException | ClassNotFoundException | ClassCastException ex) {
            JOptionPane.showMessageDialog(null,
                    "Saved data could not be loaded. A new empty session will be started.\n" + ex.getMessage(),
                    "File Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static class Student implements Serializable {
        private static final long serialVersionUID = 1L;
        String id, name, department, year;
        Student(String id, String name, String department, String year) {
            this.id = id; this.name = name; this.department = department; this.year = year;
        }
    }

    private static class Subject implements Serializable {
        private static final long serialVersionUID = 1L;
        String id, name;
        int maxMarks;
        Subject(String id, String name, int maxMarks) {
            this.id = id; this.name = name; this.maxMarks = maxMarks;
        }
    }

    private static class MarkRecord implements Serializable {
        private static final long serialVersionUID = 1L;
        String studentId, subjectId;
        double marks;
        MarkRecord(String studentId, String subjectId, double marks) {
            this.studentId = studentId; this.subjectId = subjectId; this.marks = marks;
        }
    }

    private static class AppData implements Serializable {
        private static final long serialVersionUID = 1L;
        ArrayList<Student> students;
        ArrayList<Subject> subjects;
        ArrayList<MarkRecord> marks;
        AppData(java.util.List<Student> students, java.util.List<Subject> subjects,
                java.util.List<MarkRecord> marks) {
            this.students = new ArrayList<>(students);
            this.subjects = new ArrayList<>(subjects);
            this.marks = new ArrayList<>(marks);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UniversityExaminationManagementSystem().setVisible(true));
    }
}