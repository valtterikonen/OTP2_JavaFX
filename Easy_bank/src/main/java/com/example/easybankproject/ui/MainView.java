package com.example.easybankproject.ui;

import com.example.easybankproject.models.BankAccount;
import com.example.easybankproject.models.Transaction;
import com.example.easybankproject.services.TransactionService;
import com.example.easybankproject.utils.JwtUtil;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@PageTitle("main")
@Route(value = "main", layout = MainLayout.class)
public class MainView extends Composite<VerticalLayout> implements BeforeEnterObserver {
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final MessageSource messageSource;

    private int notificationsCount = 0;

    private Button notificationsLink;
    private Grid<com.example.easybankproject.models.Notification> grid;
    private List<com.example.easybankproject.models.Notification> notificationList;
    private final Paragraph balanceParagraph;

    private final Grid<Transaction> transactionGrid;
    private final TransactionService transactionService;

    private Image image;

    public Locale getLocale() {
        return VaadinSession.getCurrent().getLocale();
    }


    public MainView(JwtUtil jwtUtil, TransactionService transactionService, MessageSource messageSource) {
        this.jwtUtil = jwtUtil;
        this.transactionGrid = new Grid<>(Transaction.class);
        this.restTemplate = new RestTemplate();
        this.balanceParagraph = new Paragraph();
        this.transactionService = transactionService;
        this.messageSource = messageSource;



        HorizontalLayout notificationsLayout = new HorizontalLayout();
        HorizontalLayout container = new HorizontalLayout();
        Button transactionButton = new Button(messageSource.getMessage("transaction.new.button", null, getLocale()), event -> openTransactionDialog());
        transactionButton.addClassName("transaction-btn");
        notificationsLink = new Button(messageSource.getMessage("notifications.link", new Object[]{notificationsCount}, getLocale()));
        notificationsLink.addClickListener(event -> openNotificationsDialog());
        notificationsLink.addClassName("toggle");
        image = new Image("./images/notification.png", messageSource.getMessage("notifications.icon.alt", null, getLocale()));
        image.addClassName("notification-icon");
        notificationsLayout.add(image, notificationsLink);
        Paragraph balanceText = new Paragraph();
        balanceText.setText(messageSource.getMessage("balance.total", null, getLocale()));
        balanceText.getStyle().set("font-size", "var(--lumo-font-size-xl)");
        H2 transactionText = new H2();
        transactionText.setText(messageSource.getMessage("transaction.history", null, getLocale()));
        balanceParagraph.getStyle().set("font-size", "var(--lumo-font-size-xxl)");
        balanceParagraph.getStyle().set("left", "100");
        container.add(balanceText, notificationsLayout);
        container.addClassName("container");

        getContent().add(container, balanceParagraph, transactionButton, transactionText, transactionGrid);
        getContent().addClassName("body");

        fetchNotificationsCount();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String token = (String) VaadinSession.getCurrent().getAttribute("token");
        String username = (String) VaadinSession.getCurrent().getAttribute("username");

        if (token == null || !jwtUtil.validateToken(token, username)) {
            Notification.show(messageSource.getMessage("login.prompt", null, getLocale()));
            event.rerouteTo(LoginView.class);
        }
        fetchBalance();
        fetchTransactions();
        getUI().ifPresent(ui -> ui.getPage().reload());
    }

    public void fetchNotificationsCount() {
        String token = (String) VaadinSession.getCurrent().getAttribute("token");
        if (token == null) {
            Notification.show(messageSource.getMessage("unauthorized.no.token", null, getLocale()));
            return;
        }

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/notifications/count";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Integer> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Integer.class
            );
            notificationsCount = response.getBody();
            notificationsLink.setText(messageSource.getMessage("notifications.link", new Object[]{notificationsCount}, getLocale()));
            System.out.println("Notifications count: " + notificationsCount);
        } catch (Exception e) {
            Notification.show(messageSource.getMessage("error.message", new Object[]{e.getMessage()}, getLocale()));
        }
    }

    private void openNotificationsDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("800px");
        VerticalLayout dialogLayout = new VerticalLayout();
        dialog.add(dialogLayout);

        String token = (String) VaadinSession.getCurrent().getAttribute("token");
        if (token == null) {
            Notification.show(messageSource.getMessage("unauthorized.no.token", null, getLocale()));
            return;
        }

        notificationList = fetchNotifications(token);
        if (notificationList == null) {
            return;
        }

        H2 title = new H2(messageSource.getMessage("notifications.title", new Object[]{notificationsCount}, getLocale()));
        grid = new Grid<>(com.example.easybankproject.models.Notification.class);
        grid.setItems(notificationList);
        grid.setColumns("content", "timestamp");

        grid.addComponentColumn(notification -> {
            Button deleteButton = new Button(messageSource.getMessage("delete.button", null, getLocale()));
            deleteButton.addClassName("delete");
            deleteButton.addClickListener(event -> {
                deleteNotification(notification.getNotificationId(), token);
                fetchNotificationsCount();
                title.setText(messageSource.getMessage("notifications.title", new Object[]{notificationsCount}, getLocale()));
            });
            return deleteButton;
        }).setHeader(messageSource.getMessage("actions.header", null, getLocale()));

        dialogLayout.addClassName("notifications-dialog");
        dialogLayout.add(title, grid);
        dialog.open();
    }

    private List<com.example.easybankproject.models.Notification> fetchNotifications(String token) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/notifications";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<com.example.easybankproject.models.Notification[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    com.example.easybankproject.models.Notification[].class
            );
            List<com.example.easybankproject.models.Notification> notificationList = Arrays.asList(response.getBody());
            notificationList.sort((n1, n2) -> n2.getTimestamp().compareTo(n1.getTimestamp()));
            return notificationList;
        } catch (Exception e) {
            Notification.show(messageSource.getMessage("error.message", new Object[]{e.getMessage()}, getLocale()));
            return null;
        }
    }

    private void deleteNotification(Integer notificationId, String token) {
        RestTemplate restTemplate = new RestTemplate();
        String deleteUrl = "http://localhost:8080/api/notifications/" + notificationId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> deleteEntity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(deleteUrl, HttpMethod.DELETE, deleteEntity, Void.class);
            Notification.show(messageSource.getMessage("notification.deleted", null, getLocale()));

            notificationList = fetchNotifications(token);
            grid.setItems(notificationList);
            fetchNotificationsCount();
        } catch (Exception e) {
            Notification.show(messageSource.getMessage("error.message", new Object[]{e.getMessage()}, getLocale()));
        }
    }

    private void fetchTransactions() {
        try {
            String token = (String) VaadinSession.getCurrent().getAttribute("token");

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Transaction[]> response = restTemplate.exchange(
                    "http://localhost:8080/api/transaction/history",
                    HttpMethod.GET,
                    entity,
                    Transaction[].class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                List<Transaction> transactions = Arrays.asList(response.getBody());
                transactions.sort((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()));
                transactionGrid.setItems(transactions);
                transactionGrid.setClassName("transactions-grid");
                transactionGrid.removeColumnByKey("amount");

                if (transactionGrid.getColumnByKey("amount") == null) {
                    transactionGrid.addColumn(new ComponentRenderer<>(transaction -> {
                        Div container = new Div();
                        container.getStyle().set("padding", "10px");
                        container.getStyle().set("border-radius", "5px");

                        String amountPrefix = "";
                        int currentAccountId = transactionService.getSenderId(token);
                        if (transaction.getSenderAccountId() == currentAccountId) {
                            container.getStyle().set("background-color", "#ffcccc");
                            amountPrefix = "-";
                        } else if (transaction.getReceiverAccountId() == currentAccountId) {
                            container.getStyle().set("background-color", "#ccffcc");
                            amountPrefix = "+";
                        }

                        container.setText(amountPrefix + transaction.getAmount() + " €");
                        return container;
                    })).setHeader(messageSource.getMessage("amount.header", null, getLocale())).setKey("amount");
                }
            }
        } catch (HttpServerErrorException e) {
            Notification.show(messageSource.getMessage("server.error", new Object[]{e.getMessage()}, getLocale()), 3000, Notification.Position.MIDDLE);
        }
    }




    private void fetchBalance() {
        String url = "http://localhost:8080/api/bankaccount/balance";
        String token = (String) VaadinSession.getCurrent().getAttribute("token");

        if (token == null) {
            getContent().add(new Div(messageSource.getMessage("unauthorized.no.token", null, getLocale())));
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            BankAccount bankAccount = restTemplate.exchange(url, HttpMethod.GET, request, BankAccount.class).getBody();
            balanceParagraph.setText("" + bankAccount.getBalance() + " €");
        } catch (Exception e) {
            Notification.show(messageSource.getMessage("error.message", new Object[]{e.getMessage()}, getLocale()));
        }
    }

    private void openTransactionDialog() {
        Dialog dialog = new Dialog();
        VerticalLayout formLayout = new VerticalLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout();

        NumberField amountField = new NumberField(messageSource.getMessage("amount.label", null, getLocale()));
        TextField receiverField = new TextField(messageSource.getMessage("receiver.label", null, getLocale()));
        TextField messageField = new TextField(messageSource.getMessage("message.label", null, getLocale()));
        H2 title = new H2(messageSource.getMessage("transaction.new.title", null, getLocale()));
        amountField.addClassName("field");
        receiverField.addClassName("field");
        messageField.addClassName("field");

        Button submitButton = new Button(messageSource.getMessage("submit.button", null, getLocale()), event -> {
            double amount = amountField.getValue();
            int receiver = Integer.parseInt(receiverField.getValue());
            String message = messageField.getValue();
            transaction(amount, receiver, message);
            dialog.close();
        });

        Button cancelButton = new Button(messageSource.getMessage("cancel.button", null, getLocale()), event -> dialog.close());
        cancelButton.addClassName("delete");

        buttonLayout.add(submitButton, cancelButton);

        submitButton.setClassName("transaction-btn");
        formLayout.add(title, amountField, receiverField, messageField, buttonLayout);
        dialog.add(formLayout);
        dialog.open();
    }

    private void transaction(double amount, int receiver, String message) {
        String url = "http://localhost:8080/api/transaction/create";
        String token = (String) VaadinSession.getCurrent().getAttribute("token");
        if (token == null) {
            Notification.show(messageSource.getMessage("unauthorized.no.token", null, getLocale()));
            return;
        }
        int sender = transactionService.getSenderId(token);

        String jsonPayload = String.format("{\"amount\":\"%s\",\"receiverAccountId\":\"%s\",\"message\":\"%s\",\"senderAccountId\":\"%s\"}",
                amount, receiver, message, sender);
        System.out.println("TransactionInfo: " + jsonPayload);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        try {
            String transaction = restTemplate.postForObject(url, request, String.class);
            Notification.show(messageSource.getMessage("transaction.success", new Object[]{transaction}, getLocale()));
            fetchTransactions();
            fetchBalance();
            fetchNotificationsCount();
        } catch (Exception e) {
            Notification.show(messageSource.getMessage("error.message", new Object[]{e.getMessage()}, getLocale()));
        }
    }
}