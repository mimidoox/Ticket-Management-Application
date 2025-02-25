package com.example.ticketManager.ui;

import com.example.ticketManager.TicketManagerApplication;
import com.example.ticketManager.entities.Category;
import com.example.ticketManager.entities.Comment;
import com.example.ticketManager.entities.Ticket;
import com.example.ticketManager.entities.User;
import com.example.ticketManager.services.CategoryService;
import com.example.ticketManager.services.TicketService;
import java.awt.BorderLayout;
import net.miginfocom.swing.MigLayout;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class EmployeeForm extends JFrame {
    private TicketService ticketService;
    private CategoryService categoryService;
    private JTable ticketTable;
    private DefaultTableModel tableModel;
    private User employee;
    private JPanel contentPanel;
    private LoginForm loginForm;

    public EmployeeForm(User user, TicketService ticketService, CategoryService categoryService) {
        this.ticketService = ticketService;
        this.categoryService = categoryService;
        this.employee = user;

        setTitle("Employee Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setApplicationIcon("/template/hahnlogo.png");

        // Main panel using MigLayout: two columns (sidebar fixed width, content grows)
        JPanel mainPanel = new JPanel(new MigLayout("insets 0, fill", "[200!][grow]", "[grow]"));
        mainPanel.add(createSidebar(), "cell 0 0, growy");
        contentPanel = createMyTicketsPanel();
        mainPanel.add(contentPanel, "cell 1 0, grow");
        add(mainPanel);

        setVisible(true);
    }
    
    // Default constructor if needed
    public EmployeeForm() {}

    private void setApplicationIcon(String path) {
        URL appIconUrl = getClass().getResource(path);
        if (appIconUrl != null) {
            ImageIcon appIcon = new ImageIcon(appIconUrl);
            setIconImage(appIcon.getImage());
        } else {
            System.err.println("Application icon not found!");
        }
    }

    // ====================================================
    // Sidebar using MigLayout exclusively
    // ====================================================
    private JPanel createSidebar() {
        // MigLayout: insets 20, fill, gapy 20, one column
        JPanel sidebar = new JPanel(new MigLayout("insets 20, fill, gapy 20", "[grow]", "[]10[]"));
        sidebar.setBackground(new Color(41, 50, 61));

        // Profile panel (icon and greeting) with MigLayout
        JPanel profilePanel = new JPanel(new MigLayout("wrap 1, fill", "[grow]", "[]5[]"));
        profilePanel.setBackground(new Color(41, 50, 61));

        // Load the technical support icon (same as in SupportITForm)
        JLabel profilePic = new JLabel();
        profilePic.setIcon(loadAndResizeIcon("/template/emp.png", 80, 80));
        profilePanel.add(profilePic, "align center");

        // Greeting label
        JLabel welcomeLabel = new JLabel(employee.getFirstName() + " " + employee.getLastName());
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        profilePanel.add(welcomeLabel, "align center");

        sidebar.add(profilePanel, "growx, wrap");

        // Sidebar buttons (styled as in SupportITForm)
        
        JButton createTicket = createSidebarButton("Create Ticket", "/template/add.png", e -> onCreateNewTicket(e));
        JButton allTicketsButton = createSidebarButton("My Tickets", "/template/ticket.png", e -> onMyTickets(e));
        JButton logoutButton = createSidebarButton("Logout", "/template/logout.png", e -> onLogout(e));
        sidebar.add(createTicket, "growx, wrap");
        sidebar.add(allTicketsButton, "growx, wrap");
        sidebar.add(logoutButton, "growx");
        return sidebar;
    }
    
     private JButton createSidebarButton(String text, String iconPath, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 70, 70));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setIcon(loadAndResizeIcon(iconPath, 20, 20));
        button.addActionListener(action);
        return button;
    }


    // Utility method to load and resize icons
    private ImageIcon loadAndResizeIcon(String path, int width, int height) {
        URL iconUrl = getClass().getResource(path);
        if (iconUrl != null) {
            ImageIcon originalIcon = new ImageIcon(iconUrl);
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        System.err.println("Icon not found: " + path);
        return null;
    }


    // ====================================================
    // Content Panels
    // ====================================================
    // "Create New Ticket" panel
    private JPanel createCreateTicketForm() {
        JPanel formPanel = new JPanel(new MigLayout("insets 20, fill", "[grow]", "[]20[]20[]20[]20[shrink]"));
        JLabel createTicketLabel = new JLabel("Create a New Ticket");
        createTicketLabel.setFont(new Font("Arial", Font.BOLD, 18));
        formPanel.add(createTicketLabel, "wrap");

        // Title field
        formPanel.add(new JLabel("Title:"), "wrap");
        JTextField titleField = new JTextField();
        titleField.setFont(new Font("Arial", Font.PLAIN, 14));
        titleField.setBorder(new LineBorder(Color.GRAY));
        formPanel.add(titleField, "growx, wrap");

        // Description field
        formPanel.add(new JLabel("Description:"), "wrap");
        JTextArea descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setBorder(new LineBorder(Color.GRAY));
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        formPanel.add(descriptionScrollPane, "growx, wrap");

        // Priority dropdown
        formPanel.add(new JLabel("Priority:"), "wrap");
        JComboBox<String> priorityComboBox = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        priorityComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(priorityComboBox, "growx, wrap");

        // Category dropdown (populated from DB)
        formPanel.add(new JLabel("Category:"), "wrap");
        JComboBox<Category> categoryComboBox = new JComboBox<>();
        categoryComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        List<Category> categories = categoryService.getAllCategories();
        for (Category category : categories) {
            categoryComboBox.addItem(category);
        }
        formPanel.add(categoryComboBox, "growx, wrap");

        // Status dropdown (disabled for new tickets)
        formPanel.add(new JLabel("Status:"), "wrap");
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"New", "In Progress", "Resolved"});
        statusComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        statusComboBox.setEnabled(false);
        formPanel.add(statusComboBox, "growx, wrap");

        // Submit button
        JButton submitButton = new JButton("Create Ticket");
        submitButton.setBackground(new Color(128, 206, 221));
        submitButton.setForeground(Color.BLACK);
        submitButton.setFont(new Font("Arial", Font.PLAIN, 16));
        submitButton.addActionListener((ActionEvent e) -> {
            String title = titleField.getText();
            String description = descriptionArea.getText();
            String priority = (String) priorityComboBox.getSelectedItem();
            Category category = (Category) categoryComboBox.getSelectedItem();
            String status = (String) statusComboBox.getSelectedItem();

            if (!title.isEmpty() && !description.isEmpty() && category != null) {
                ticketService.createTicket(new Ticket(title, description, priority, category, status, employee));
                JOptionPane.showMessageDialog(this, "Ticket created successfully!");
                onMyTickets(e);
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            }
        });
        formPanel.add(submitButton, "align center, wrap");
        return formPanel;
    }

    // "My Tickets" panel
   private JPanel createMyTicketsPanel() {
        JPanel myTicketsPanel = new JPanel(new MigLayout("insets 20, fill", "[grow]", "[][grow]"));
        JLabel titleLabel = new JLabel("My Tickets");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        myTicketsPanel.add(titleLabel, "wrap");

        // Table to display tickets (removed "Actions" column)
        String[] columnNames = {"Ticket ID", "Description", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing for all cells
            }
        };
        
        ticketTable = new JTable(tableModel);
        ticketTable.setFont(new Font("Arial", Font.PLAIN, 14));
        ticketTable.setRowHeight(35);
        ticketTable.setSelectionBackground(new Color(63, 81, 181));
        
        // Add row click listener
        ticketTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = ticketTable.getSelectedRow();
                if (selectedRow != -1) {
                    showTicketDetails(selectedRow);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(ticketTable);
        myTicketsPanel.add(scrollPane, "grow");
        loadTickets();
        return myTicketsPanel;
    }

    private void loadTickets() {
        List<Ticket> tickets = ticketService.getTicketsByUser(employee);
        tableModel.setRowCount(0);
        for (Ticket ticket : tickets) {
            // Removed "View Details" button from the row data
            tableModel.addRow(new Object[]{
                ticket.getTicketId(),
                ticket.getDescription(),
                ticket.getStatus()
            });
        }
    }




    // ====================================================
    // Event Handlers for Sidebar buttons
    // ====================================================
    private void onCreateNewTicket(ActionEvent e) {
        contentPanel.removeAll();
        contentPanel.add(createCreateTicketForm(), "grow");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void onMyTickets(ActionEvent e) {
        contentPanel.removeAll();
        contentPanel.add(createMyTicketsPanel(), "grow");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void onLogout(ActionEvent e) {
        this.dispose();
        ConfigurableApplicationContext context = TicketManagerApplication.getContext();
        LoginForm loginForm = context.getBean(LoginForm.class);
        loginForm.clearFields();
        loginForm.setVisible(true);
    }

private void showTicketDetails(int row) {
    Long ticketId = (Long) tableModel.getValueAt(row, 0);
    Ticket ticket = ticketService.getTicketById(ticketId).orElse(null);
    if (ticket == null) return;

    JDialog detailsDialog = new JDialog(this, "Ticket Details", true);
    detailsDialog.setLayout(new BorderLayout());
    detailsDialog.setSize(700, 600);
    detailsDialog.setLocationRelativeTo(this);

    // Main container panel using MigLayout
    JPanel mainPanel = new JPanel(new MigLayout("insets 10, fill", "[grow]", "[][grow][]"));

    // Ticket Information Panel
    JPanel infoPanel = new JPanel(new MigLayout("wrap 2, fillx", "[right]10[grow]", "[]10[]"));
    infoPanel.setBorder(BorderFactory.createTitledBorder("Ticket Information"));
    
    // Add all ticket information fields
    infoPanel.add(new JLabel("Ticket ID:"));
    infoPanel.add(new JLabel(ticket.getTicketId().toString()), "growx");
    infoPanel.add(new JLabel("Title:"));
    infoPanel.add(new JLabel(ticket.getTitle()), "growx");
    infoPanel.add(new JLabel("Description:"));
    infoPanel.add(new JLabel(ticket.getDescription()), "growx");
    infoPanel.add(new JLabel("Priority:"));
    infoPanel.add(new JLabel(ticket.getPriority()), "growx");
    infoPanel.add(new JLabel("Created by:"));
    infoPanel.add(new JLabel(ticket.getCreatedBy().getFirstName()+" "+ticket.getCreatedBy().getLastName()), "growx");
    infoPanel.add(new JLabel("Creation Date:"));
    infoPanel.add(new JLabel(ticket.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))), "growx");
    infoPanel.add(new JLabel("Status:"));
    infoPanel.add(new JLabel(ticket.getStatus()), "growx");
    
    mainPanel.add(infoPanel, "growx, wrap");

    // Comments Panel with Scroll
    JPanel commentPanel = new JPanel(new MigLayout("fill, insets 0", "[grow]", "[grow]"));
    commentPanel.setBorder(BorderFactory.createTitledBorder("Comments"));
    
    JPanel commentsContainer = new JPanel(new MigLayout("fillx, wrap 1, insets 0", "[grow]", "[]0[]"));
    JScrollPane scrollPane = new JScrollPane(commentsContainer);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    List<Comment> comments = ticketService.getCommentsByTicket(ticket);
    LocalDate currentDate = null;
    
    for (Comment comment : comments) {
        LocalDateTime created = comment.getCreatedDate();
        LocalDate commentDate = created.toLocalDate();
        
        // Date header
        if (currentDate == null || !commentDate.equals(currentDate)) {
            currentDate = commentDate;
            String dateHeader = commentDate.equals(LocalDate.now()) ? "Today" : 
                commentDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
            
            JLabel dateLabel = new JLabel(dateHeader);
            dateLabel.setFont(dateLabel.getFont().deriveFont(Font.BOLD, 12));
            commentsContainer.add(dateLabel, "growx, gapbottom 5, wrap");
        }

        // Comment entry panel
        JPanel entryPanel = new JPanel(new MigLayout("fillx, ins 5", "[grow][]", "[][grow]"));
        entryPanel.setOpaque(true);
        entryPanel.setBackground(generateUserColor(comment.getCreatedBy()));
        entryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Author and time components
        JLabel authorLabel = new JLabel(comment.getCreatedBy().getFirstName()+" "+comment.getCreatedBy().getLastName());
        authorLabel.setFont(authorLabel.getFont().deriveFont(Font.BOLD));
        
        JLabel timeLabel = new JLabel(created.format(DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.setFont(timeLabel.getFont().deriveFont(Font.ITALIC, 10f));

        // Comment text
        JTextArea commentText = new JTextArea(comment.getCommentText());
        commentText.setWrapStyleWord(true);
        commentText.setLineWrap(true);
        commentText.setEditable(false);
        commentText.setOpaque(false);
        commentText.setMargin(new Insets(2, 0, 0, 0));

        // Add components
        entryPanel.add(authorLabel, "split 2");
        entryPanel.add(timeLabel, "gapright 0, align right, wrap");
        entryPanel.add(commentText, "growx, span 2");
        
        commentsContainer.add(entryPanel, "growx, hmin 60, wrap");
    }

    commentPanel.add(scrollPane, "grow");
    mainPanel.add(commentPanel, "grow, wrap");

    // Back button
    JButton backButton = new JButton("Back");
    backButton.addActionListener(e -> detailsDialog.dispose());
    mainPanel.add(backButton, "center, gapbottom 10");
    
    detailsDialog.add(mainPanel, BorderLayout.CENTER);
    detailsDialog.setVisible(true);
}

private Color generateUserColor(User user) {
    // Generate pastel color based on user ID hash
    int hash = user.getUsername().hashCode();
    return Color.getHSBColor(
        Math.abs(hash % 360) / 360f,  // Hue
        0.3f,                         // Saturation (pastel)
        0.95f                         // Brightness
    );
}

/*private Color generateUserColor(User user) {
    // Generate consistent color from user ID hash
    int hash = user.getUsername().hashCode();
    float hue = (Math.abs(hash) % 100) / 100f; // Range 0-1
    return Color.getHSBColor(hue, 0.3f, 0.95f); // Pastel colors
}*/
}
