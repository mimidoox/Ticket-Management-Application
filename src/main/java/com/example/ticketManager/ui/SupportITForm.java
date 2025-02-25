package com.example.ticketManager.ui;

import com.example.ticketManager.TicketManagerApplication;
import com.example.ticketManager.entities.AuditLog;
import com.example.ticketManager.entities.Comment;
import com.example.ticketManager.entities.Ticket;
import com.example.ticketManager.entities.User;
import com.example.ticketManager.services.AuditLogService;
import com.example.ticketManager.services.TicketService;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.table.TableRowSorter;
import net.miginfocom.swing.MigLayout;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.ticketManager.TicketManagerApplication;
import com.example.ticketManager.entities.AuditLog;
import com.example.ticketManager.entities.Comment;
import com.example.ticketManager.entities.Ticket;
import com.example.ticketManager.entities.User;
import com.example.ticketManager.services.AuditLogService;
import com.example.ticketManager.services.TicketService;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import net.miginfocom.swing.MigLayout;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SupportITForm extends JFrame {
    private JTable ticketTable;
    private JTable auditLogTable;
    private DefaultTableModel tableModel;
    private final User supportUser;
    private final TicketService ticketService;
    private final AuditLogService auditLogService;
    private TableRowSorter<DefaultTableModel> tableSorter;
    private JTextField searchField;
    private JComboBox<String> statusFilterComboBox;
    private final JPanel mainPanel;
    private JComboBox<String> actionFilterComboBox;

    public SupportITForm(User supportUser, TicketService ticketService, AuditLogService auditLogService) {
        this.supportUser = supportUser;
        this.ticketService = ticketService;
        this.auditLogService = auditLogService;

        // Thème moderne FlatLaf
        FlatLaf.setup(new FlatIntelliJLaf());

        setTitle("Support IT Dashboard");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setApplicationIcon("/template/hahnlogo.png");

        // Main panel en MigLayout : 2 colonnes (sidebar fixe et contenu extensible)
        mainPanel = new JPanel(new MigLayout("insets 0, fill", "[250!][grow]", "[grow]"));
        mainPanel.add(createSidebar(), "cell 0 0, growy");
        mainPanel.add(createContentPanel(), "cell 1 0, grow");
        add(mainPanel);

        refreshTickets();
        setVisible(true);
    }

    private void setApplicationIcon(String path) {
        URL iconUrl = getClass().getResource(path);
        if (iconUrl != null) {
            setIconImage(new ImageIcon(iconUrl).getImage());
        } else {
            System.err.println("Application icon not found!");
        }
    }

    // ================================================================
    // Sidebar et navigation
    // ================================================================
    private JPanel createSidebar() {
        JPanel sidebarPanel = new JPanel(new MigLayout("insets 20 10 20 10, fill, gapy 20"));
        sidebarPanel.setBackground(new Color(50, 50, 50));

        // Profil (photo + nom)
        JPanel profilePanel = new JPanel(new MigLayout("wrap 1, fill", "[center]", "[]10[]"));
        profilePanel.setBackground(new Color(50, 50, 50));

        JLabel profilePicture = new JLabel();
        profilePicture.setIcon(loadAndResizeIcon("/template/technical-support.png", 80, 80));
        profilePanel.add(profilePicture, "center");

        JLabel userNameLabel = new JLabel(supportUser.getFirstName() + " " + supportUser.getLastName());
        userNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        userNameLabel.setForeground(Color.WHITE);
        profilePanel.add(userNameLabel, "center");
        sidebarPanel.add(profilePanel, "wrap");

        // Boutons de navigation
        JButton auditLogsButton = createSidebarButton("Audit Logs", "/template/audit_logs.png", e -> auditLogs());
        JButton allTicketsButton = createSidebarButton("All Tickets", "/template/ticket.png", e -> refreshTickets());
        JButton logoutButton = createSidebarButton("Logout", "/template/logout.png", e -> onLogout());

        sidebarPanel.add(auditLogsButton, "growx, wrap");
        sidebarPanel.add(allTicketsButton, "growx, wrap");
        sidebarPanel.add(logoutButton, "growx");
        return sidebarPanel;
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

    // ================================================================
    // Contenu principal (Tickets)
    // ================================================================
    private JPanel createContentPanel() {
        // Content panel en MigLayout, vertical (wrap 1)
        JPanel contentPanel = new JPanel(new MigLayout("insets 20, fill", "[grow]", "[]10[grow, fill]"));
        //JLabel welcomeLabel = new JLabel("Hello, " + supportUser.getFirstName() + " " + supportUser.getLastName() + "!");
        //welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        //contentPanel.add(welcomeLabel, "wrap");

        // Panneau de recherche & filtre
        contentPanel.add(createSearchFilterPanel(), "wrap, growx");

        // Tableau des tickets dans une scrollPane
        ticketTable = createTicketTable();
        JScrollPane scrollPane = new JScrollPane(ticketTable);
        contentPanel.add(scrollPane, "grow");
        return contentPanel;
    }

    private JPanel createSearchFilterPanel() {
        // Utilisation de MigLayout pour disposer les labels/champs sur 2 lignes
        JPanel searchFilterPanel = new JPanel(new MigLayout("insets 10, fillx", "[right]10[grow]", "[]10[]"));
        searchFilterPanel.setBorder(BorderFactory.createTitledBorder("Search and Filter"));

        // Ligne 1 : Search par Ticket ID
        JLabel searchLabel = new JLabel("Search by Ticket ID:");
        searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterTickets(); }
            public void removeUpdate(DocumentEvent e) { filterTickets(); }
            public void changedUpdate(DocumentEvent e) { filterTickets(); }
        });
        searchFilterPanel.add(searchLabel);
        searchFilterPanel.add(searchField, "wrap");

        // Ligne 2 : Filtre par Status
        JLabel statusFilterLabel = new JLabel("Filter by Status:");
        statusFilterComboBox = new JComboBox<>(new String[]{"All", "New", "In Progress", "Resolved"});
        statusFilterComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusFilterComboBox.addActionListener(e -> filterTickets());
        searchFilterPanel.add(statusFilterLabel);
        searchFilterPanel.add(statusFilterComboBox, "wrap");

        return searchFilterPanel;
    }

    private JTable createTicketTable() {
        String[] columnNames = {"Ticket ID", "Title", "Description", "Priority", "Category", "Status", "Created By", "Creation Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        ticketTable = new JTable(tableModel);
        ticketTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ticketTable.setRowHeight(30);
        ticketTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ticketTable.setAutoCreateRowSorter(true);
        ticketTable.setShowGrid(true);
        ticketTable.setGridColor(new Color(230, 230, 230));
        ticketTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = ticketTable.rowAtPoint(evt.getPoint());
                if (row >= 0) showTicketDetails(row);
            }
        });
        tableSorter = new TableRowSorter<>(tableModel);
        ticketTable.setRowSorter(tableSorter);
        return ticketTable;
    }

    private void refreshTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Ticket ticket : tickets) {
            Object[] row = {
                ticket.getTicketId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPriority(),
                ticket.getCategory().getTitle(),
                ticket.getStatus(),
                ticket.getCreatedBy().getFirstName() + " " + ticket.getCreatedBy().getLastName(),
                ticket.getCreationDate().format(formatter)
            };
            tableModel.addRow(row);
        }
    }

    private void filterTickets() {
        String searchText = searchField.getText().trim();
        String statusFilter = (String) statusFilterComboBox.getSelectedItem();
        RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.andFilter(List.of(
                RowFilter.regexFilter("(?i)" + searchText, 0),
                RowFilter.regexFilter(statusFilter.equals("All") ? "" : "(?i)" + statusFilter, 5)
        ));
        tableSorter.setRowFilter(rowFilter);
    }

    // ================================================================
    // Détails d'un ticket (dialog)
    // ================================================================
private void showTicketDetails(int row) {
    Long ticketId = (Long) tableModel.getValueAt(row, 0);
    Ticket ticket = ticketService.getTicketById(ticketId).orElse(null);
    if (ticket == null) return;

    JDialog detailsDialog = new JDialog(this, "Ticket Details", true);
    detailsDialog.setLayout(new BorderLayout());
    detailsDialog.setSize(700, 600);
    detailsDialog.setLocationRelativeTo(this);

    JPanel mainPanel = new JPanel(new MigLayout("insets 10, fill", "[grow]", "[][grow][]"));

    // Ticket Information Panel (editable)
    JPanel infoPanel = new JPanel(new MigLayout("wrap 2, fillx", "[right]10[grow]", "[]10[]"));
    infoPanel.setBorder(BorderFactory.createTitledBorder("Ticket Information"));
    
    infoPanel.add(new JLabel("Ticket ID:"));
    infoPanel.add(new JLabel(ticket.getTicketId().toString()));
    infoPanel.add(new JLabel("Title:"));
    infoPanel.add(new JLabel(ticket.getTitle()));
    infoPanel.add(new JLabel("Description:"));
    infoPanel.add(new JLabel(ticket.getDescription()));
    infoPanel.add(new JLabel("Priority:"));
    infoPanel.add(new JLabel(ticket.getPriority()));
    infoPanel.add(new JLabel("Created by:"));
    infoPanel.add(new JLabel(ticket.getCreatedBy().getFirstName()+" "+ticket.getCreatedBy().getLastName()));
    infoPanel.add(new JLabel("Creation Date:"));
    infoPanel.add(new JLabel(ticket.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
    infoPanel.add(new JLabel("Status:"));
    
    JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"New", "In Progress", "Resolved"});
    statusComboBox.setSelectedItem(ticket.getStatus());
    if (ticket.getStatus().equals("Resolved")) {
        statusComboBox.setEnabled(false);
    }
    infoPanel.add(statusComboBox, "wrap");
    mainPanel.add(infoPanel, "growx, wrap");

    // Comments Panel with editing capability
    JPanel commentPanel = new JPanel(new MigLayout("fill, insets 0", "[grow]", "[grow][]"));
    commentPanel.setBorder(BorderFactory.createTitledBorder("Comments"));
    
    // Comments display area
    JPanel commentsContainer = new JPanel(new MigLayout("fillx, wrap 1, insets 0", "[grow]", "[]0[]"));
    JScrollPane commentScrollPane = new JScrollPane(commentsContainer);
    commentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    
    // Existing comments
    List<Comment> comments = ticketService.getCommentsByTicket(ticket);
    LocalDate currentDate = null;
    for (Comment comment : comments) {
        LocalDateTime created = comment.getCreatedDate();
        LocalDate commentDate = created.toLocalDate();
        
        if (currentDate == null || !commentDate.equals(currentDate)) {
            currentDate = commentDate;
            String dateHeader = commentDate.equals(LocalDate.now()) ? "Today" : 
                commentDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
            
            JLabel dateLabel = new JLabel(dateHeader);
            dateLabel.setFont(dateLabel.getFont().deriveFont(Font.BOLD, 12));
            commentsContainer.add(dateLabel, "growx, gapbottom 5, wrap");
        }

        JPanel entryPanel = new JPanel(new MigLayout("fillx, ins 5", "[grow][]", "[][grow]"));
        entryPanel.setOpaque(true);
        entryPanel.setBackground(generateUserColor(comment.getCreatedBy()));
        entryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel authorLabel = new JLabel(comment.getCreatedBy().getFirstName()+" "+comment.getCreatedBy().getLastName());
        authorLabel.setFont(authorLabel.getFont().deriveFont(Font.BOLD));
        
        JLabel timeLabel = new JLabel(created.format(DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.setFont(timeLabel.getFont().deriveFont(Font.ITALIC, 10f));

        JTextArea commentText = new JTextArea(comment.getCommentText());
        commentText.setWrapStyleWord(true);
        commentText.setLineWrap(true);
        commentText.setEditable(false);
        commentText.setOpaque(false);

        entryPanel.add(authorLabel, "split 2");
        entryPanel.add(timeLabel, "gapright 0, align right, wrap");
        entryPanel.add(commentText, "growx, span 2");
        
        commentsContainer.add(entryPanel, "growx, hmin 60, wrap");
    }
    commentPanel.add(commentScrollPane, "grow, wrap");

    // New comment input area
    JTextArea newCommentArea = new JTextArea(3, 30);
    newCommentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    JScrollPane newCommentScroll = new JScrollPane(newCommentArea);
    commentPanel.add(newCommentScroll, "grow");
    mainPanel.add(commentPanel, "grow, wrap");

    // Save button
    JButton saveButton = new JButton("Save Changes");
    saveButton.addActionListener(e -> {
        ticket.setStatus((String) statusComboBox.getSelectedItem());
        ticketService.updateTicketStatus(ticket.getTicketId(), 
            (String) statusComboBox.getSelectedItem(),
            supportUser.getFirstName()+" "+supportUser.getLastName());
        
        String newComment = newCommentArea.getText().trim();
        if (!newComment.isEmpty()) {
            ticketService.addCommentToTicket(ticket, newComment, supportUser);
        }
        
        refreshTickets();
        detailsDialog.dispose();
    });
    mainPanel.add(saveButton, "center, gapbottom 10");
    
    detailsDialog.add(mainPanel, BorderLayout.CENTER);
    detailsDialog.setVisible(true);
}

private Color generateUserColor(User user) {
    int hash = user.getUsername().hashCode();
    return Color.getHSBColor(
        Math.abs(hash % 360) / 360f, 
        0.3f,
        0.95f
    );
}
    // ================================================================
    // Audit logs
    // ================================================================
    private JPanel createAuditLogsPanel() {
        JPanel auditLogsPanel = new JPanel(new MigLayout("insets 20, fill", "[grow]", "[]10[grow, fill]"));
        
        // Panneau de recherche & filtre pour audit logs
        JPanel searchPanel = new JPanel(new MigLayout("insets 10, fillx", "[right]10[grow]", "[]"));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search and Filter"));
        JLabel searchLabel = new JLabel("Search by Ticket ID:");
        searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterAuditLogs(); }
            public void removeUpdate(DocumentEvent e) { filterAuditLogs(); }
            public void changedUpdate(DocumentEvent e) { filterAuditLogs(); }
        });
        JLabel actionFilterLabel = new JLabel("Filter by Action:");
        actionFilterComboBox = new JComboBox<>(new String[]{"All", "Status change", "New Comment"});
        actionFilterComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        actionFilterComboBox.addActionListener(e -> filterAuditLogs());
        searchPanel.add(searchLabel);
        searchPanel.add(searchField, "wrap");
        searchPanel.add(actionFilterLabel);
        searchPanel.add(actionFilterComboBox, "wrap");
        auditLogsPanel.add(searchPanel, "wrap");

        // Tableau des logs
        String[] columnNames = {"Log ID", "Action", "Ticket", "Date", "Created By", "Old Status", "New Status", "Message"};
        DefaultTableModel auditLogTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        auditLogTable = new JTable(auditLogTableModel);
        auditLogTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        auditLogTable.setRowHeight(30);
        auditLogTable.setAutoCreateRowSorter(true);
        JScrollPane tableScroll = new JScrollPane(auditLogTable);
        auditLogsPanel.add(tableScroll, "grow");
        return auditLogsPanel;
    }

    private void filterAuditLogs() {
        String searchText = searchField.getText().trim();
        String actionFilter = (String) actionFilterComboBox.getSelectedItem();
        RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.andFilter(List.of(
                RowFilter.regexFilter("(?i)" + searchText, 2),
                RowFilter.regexFilter(actionFilter.equals("All") ? "" : "(?i)" + actionFilter, 1)
        ));
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) auditLogTable.getRowSorter();
        sorter.setRowFilter(rowFilter);
    }

    private void auditLogs() {
        mainPanel.removeAll();
        // Recrée le panneau principal avec sidebar et panneau audit logs
        mainPanel.setLayout(new MigLayout("insets 0, fill", "[250!][grow]", "[grow]"));
        mainPanel.add(createSidebar(), "cell 0 0, growy");
        mainPanel.add(createAuditLogsPanel(), "cell 1 0, grow");
        mainPanel.revalidate();
        mainPanel.repaint();
        refreshAuditLogs();
    }

    private void refreshAuditLogs() {
        List<AuditLog> logs = auditLogService.getAll();
        DefaultTableModel model = (DefaultTableModel) auditLogTable.getModel();
        model.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (AuditLog log : logs) {
            if (log.getAction().equals("Status Change")) {
                Object[] row = {
                        log.getAuditId(),
                        log.getAction(),
                        log.getTicketId(),
                        log.getChangeDate().format(formatter),
                        log.getChangedBy(),
                        log.getOldValue(),
                        log.getNewValue(),
                        "---"
                };
                model.addRow(row);
            } else {
                Object[] row = {
                        log.getAuditId(),
                        log.getAction(),
                        log.getTicketId(),
                        log.getChangeDate().format(formatter),
                        log.getChangedBy(),
                        "---",
                        "---",
                        log.getComment()
                };
                model.addRow(row);
            }
        }
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        auditLogTable.setRowSorter(sorter);
    }

    private void onLogout() {
        this.dispose();
        ConfigurableApplicationContext context = TicketManagerApplication.getContext();
        LoginForm loginForm = context.getBean(LoginForm.class);
        loginForm.clearFields();
        loginForm.setVisible(true);
    }
}
