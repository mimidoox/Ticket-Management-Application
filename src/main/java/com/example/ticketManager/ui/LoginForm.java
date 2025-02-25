/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ticketManager.ui;

import com.example.ticketManager.controllers.AuthController;
import com.example.ticketManager.entities.User;
import com.example.ticketManager.repos.UserRepository;
import com.example.ticketManager.services.AuditLogService;
import com.example.ticketManager.services.CategoryService;
import com.example.ticketManager.services.TicketService;
import com.example.ticketManager.services.UserService;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author macbookmimid
 */
@Component
public class LoginForm extends JFrame {
    private JPanel formPanel;
    private CardLayout cardLayout;
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JTextField signupFirstNameField;
    private JTextField signupLastNameField;
    private JTextField signupUsernameField;
    private JPasswordField signupPasswordField;
    private JComboBox<String> roleCombo;

    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired 
    private TicketService ticketService;

    public LoginForm(UserService userService, TicketService ticketService, 
                    CategoryService categoryService, AuditLogService auditLogService) {
        this.userService = userService;
        this.ticketService = ticketService;
        this.categoryService = categoryService;
        this.auditLogService = auditLogService;

        initUI();
        setupFrame();
    }

    private void initUI() {
        setTitle("Ticket System Authentication");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(getAppIcon());

        JPanel mainPanel = new JPanel(new MigLayout("fill", "[200!][grow]", "[grow]"));
        mainPanel.add(createSidebar(), "w 200!, h 100%, gapright 20");
        mainPanel.add(createFormPanel(), "grow");
        add(mainPanel);
    }

    private Image getAppIcon() {
        URL iconUrl = getClass().getResource("/template/hahnlogo.png");
        return iconUrl != null ? new ImageIcon(iconUrl).getImage() : null;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new MigLayout("ins 20, wrap 1", "[grow]", "[][grow]"));
        sidebar.setBackground(new Color(44, 62, 80));

        JLabel logoLabel = new JLabel("Ticket System");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(Color.WHITE);
        sidebar.add(logoLabel, "center, gapbottom 30, wrap");

        JButton loginBtn = createSidebarButton("Sign In", this::showLoginForm);
        JButton signupBtn = createSidebarButton("Sign Up", this::showSignupForm);

        JPanel buttonPanel = new JPanel(new MigLayout("wrap 1, ins 0, gapy 10", "[grow]"));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginBtn, "growx");
        buttonPanel.add(signupBtn, "growx");

        sidebar.add(buttonPanel, "grow");
        return sidebar;
    }

    private JButton createSidebarButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 73, 94));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(52, 73, 94));
            }
            public void mouseHover(MouseEvent e) {
                button.setBackground(new Color(52, 73, 94));
            }
        });

        button.addActionListener(e -> action.run());
        return button;
    }

    private JPanel createFormPanel() {
        cardLayout = new CardLayout();
        formPanel = new JPanel(cardLayout);
        formPanel.add(createLoginForm(), "login");
        formPanel.add(createSignupForm(), "signup");
        return formPanel;
    }

    private JPanel createLoginForm() {
        JPanel panel = new JPanel(new MigLayout("ins 30, wrap 2", "[right]10[grow]", "[][][][grow]"));

        JLabel title = new JLabel("Sign In to Your Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(title, "span 2, center, gapbottom 20, wrap");

        panel.add(new JLabel("Username:"), "align right");
        loginUsernameField = new JTextField(20);
        panel.add(loginUsernameField, "growx, wrap");

        panel.add(new JLabel("Password:"), "align right");
        loginPasswordField = new JPasswordField(20);
        panel.add(loginPasswordField, "growx, wrap");

        JButton loginBtn = createPrimaryButton("Login", e -> login());
        panel.add(loginBtn, "span 2, center, gaptop 20, wrap");

        return panel;
    }

    private JPanel createSignupForm() {
        JPanel panel = new JPanel(new MigLayout("ins 30, wrap 2", "[right]10[grow]", "[][][][][][grow]"));

        JLabel title = new JLabel("Create New Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(title, "span 2, center, gapbottom 20, wrap");

        panel.add(new JLabel("First Name:"), "align right");
        signupFirstNameField = new JTextField(20);
        panel.add(signupFirstNameField, "growx, wrap");

        panel.add(new JLabel("Last Name:"), "align right");
        signupLastNameField = new JTextField(20);
        panel.add(signupLastNameField, "growx, wrap");

        panel.add(new JLabel("Username:"), "align right");
        signupUsernameField = new JTextField(20);
        panel.add(signupUsernameField, "growx, wrap");

        panel.add(new JLabel("Password:"), "align right");
        signupPasswordField = new JPasswordField(20);
        panel.add(signupPasswordField, "growx, wrap");

        panel.add(new JLabel("Role:"), "align right");
        roleCombo = new JComboBox<>(new String[]{"Employee", "SupportIT"});
        panel.add(roleCombo, "growx, wrap");

        JButton signupBtn = createPrimaryButton("Create Account", e ->signup());
        panel.add(signupBtn, "span 2, center, gaptop 20, wrap");

        return panel;
    }

    private JButton createPrimaryButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(44, 62, 80));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);
        return button;
    }

    private void showLoginForm() {
        cardLayout.show(formPanel, "login");
        clearFields();
    }

    private void showSignupForm() {
        cardLayout.show(formPanel, "signup");
        clearFields();
    }

    private void login() {
        String username = loginUsernameField.getText();
        String password = new String(loginPasswordField.getPassword());

        User user = userService.authenticateUser(username, password);
        if (user == null) {
            showError("Invalid credentials", "Login Error");
            return;
        }

        openDashboard(user);
        dispose();
    }

    private void signup() {
        User newUser = new User(
            signupUsernameField.getText(),
            new String(signupPasswordField.getPassword()),
            signupFirstNameField.getText(),
            signupLastNameField.getText(),
            (String) roleCombo.getSelectedItem()
        );

        try {
            userService.createUser(newUser);
            JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            showLoginForm();
        } catch (Exception e) {
            showError("Registration failed: " + e.getMessage(), "Signup Error");
        }
    }

    private void openDashboard(User user) {
        switch (user.getRole()) {
            case "Employee":
                new EmployeeForm(user, ticketService, categoryService).setVisible(true);
                break;
            case "SupportIT":
                new SupportITForm(user, ticketService, auditLogService).setVisible(true);
                break;
            default:
                showError("Unknown user role", "Authorization Error");
        }
    }

    private void showError(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void clearFields() {
        loginUsernameField.setText("");
        loginPasswordField.setText("");
        signupFirstNameField.setText("");
        signupLastNameField.setText("");
        signupUsernameField.setText("");
        signupPasswordField.setText("");
        roleCombo.setSelectedIndex(0);
    }

    private void setupFrame() {
        setVisible(true);
    }
    
}