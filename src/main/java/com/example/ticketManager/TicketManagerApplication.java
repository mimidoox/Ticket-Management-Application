package com.example.ticketManager;

import com.example.ticketManager.services.AuditLogService;
import com.example.ticketManager.services.CategoryService;
import com.example.ticketManager.services.TicketService;
import com.example.ticketManager.services.UserService;
import com.example.ticketManager.ui.LoginForm;
import java.awt.GraphicsEnvironment;
import javax.swing.SwingUtilities;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TicketManagerApplication {
private static ConfigurableApplicationContext context;
	public static void main(String[] args) {
    if (GraphicsEnvironment.isHeadless()) {
        System.err.println("L'environnement est en mode headless. Impossible d'afficher l'interface graphique.");
        return;
    }

     context = SpringApplication.run(TicketManagerApplication.class, args);
    UserService userService = context.getBean(UserService.class);
    TicketService ticketService = context.getBean(TicketService.class);
    CategoryService categoryService = context.getBean(CategoryService.class);
    AuditLogService auditLogService = context.getBean(AuditLogService.class);
    SwingUtilities.invokeLater(() -> new LoginForm(userService,ticketService,categoryService,auditLogService).setVisible(true));
}

    public static ConfigurableApplicationContext getContext() {
        return context;
    }

	

}
