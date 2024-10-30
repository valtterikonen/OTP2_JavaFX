package com.example.easybankproject.ui;

import com.example.easybankproject.models.User;
import com.example.easybankproject.utils.JwtUtil;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import com.vaadin.flow.router.BeforeEnterObserver;

import java.util.Locale;

@PageTitle("Profile")
@CssImport("./styles/profileview.css")
@Route(value = "profile", layout = MainLayout.class)
public class ProfileView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;
    private final MessageSource messageSource;

    public ProfileView(JwtUtil jwtUtil, MessageSource messageSource) {
        this.restTemplate = new RestTemplate();
        this.jwtUtil = jwtUtil;
        this.messageSource = messageSource;
        getContent().addClassName("centered-column");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String token = (String) VaadinSession.getCurrent().getAttribute("token");
        String username = (String) VaadinSession.getCurrent().getAttribute("username");

        if (token == null || !jwtUtil.validateToken(token, username)) {
            Notification.show(messageSource.getMessage("login.prompt", null, getLocale()));
            event.rerouteTo(LoginView.class);
        }
        displayUserProfile();
    }

    private void displayUserProfile() {
        String url = "http://localhost:8080/api/user/me";
        String token = (String) VaadinSession.getCurrent().getAttribute("token");

        if (token == null) {
            getContent().add(new Div(messageSource.getMessage("unauthorized.no.token", null, getLocale())));
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            User user = restTemplate.exchange(url, HttpMethod.GET, request, User.class).getBody();

            if (user != null) {
                getContent().removeAll(); // Clear previous content

                VerticalLayout content = new VerticalLayout();
                content.addClassName("profile-column");

                H1 title = new H1(messageSource.getMessage("personal.information", null, getLocale()));
                H3 emailLabel = new H3(messageSource.getMessage("email.label", null, getLocale()));
                H3 addressLabel = new H3(messageSource.getMessage("address.label", null, getLocale()));
                H3 phoneLabel = new H3(messageSource.getMessage("phonenumber.label", null, getLocale()));
                H3 firstNameLabel = new H3(messageSource.getMessage("firstname.label", null, getLocale()));
                H3 lastNameLabel = new H3(messageSource.getMessage("lastname.label", null, getLocale()));

                Div emailDiv = new Div();
                emailDiv.setText(user.getEmail());

                Div addressDiv = new Div();
                addressDiv.setText(user.getAddress());

                Div phoneDiv = new Div();
                phoneDiv.setText("" + user.getPhonenumber());

                Div firstNameDiv = new Div();
                firstNameDiv.setText(user.getFirstname());

                Div lastNameDiv = new Div();
                lastNameDiv.setText(user.getLastname());

                Button updateButton = new Button(messageSource.getMessage("update.button", null, getLocale()), event -> openUpdateDialog(user, token));
                updateButton.addClassName("transaction-btn");

                content.add(emailLabel, emailDiv, addressLabel, addressDiv, phoneLabel, phoneDiv, firstNameLabel, firstNameDiv, lastNameLabel, lastNameDiv, updateButton);
                getContent().add(title, content);
            } else {
                getContent().add(new Div(messageSource.getMessage("error.user.not.found", null, getLocale())));
            }

        } catch (Exception e) {
            getContent().add(new Div(messageSource.getMessage("error.message", new Object[]{e.getMessage()}, getLocale())));
        }
    }

    private void openUpdateDialog(User user, String token) {
        Dialog dialog = new Dialog();
        dialog.addClassName("dialog");

        H3 title = new H3(messageSource.getMessage("update.profile", null, getLocale()));
        TextField emailField = new TextField(messageSource.getMessage("email.label", null, getLocale()), user.getEmail(), "");
        TextField addressField = new TextField(messageSource.getMessage("address.label", null, getLocale()), user.getAddress(), "");
        TextField phoneField = new TextField(messageSource.getMessage("phonenumber.label", null, getLocale()), String.valueOf(user.getPhonenumber()), "");
        TextField firstNameField = new TextField(messageSource.getMessage("firstname.label", null, getLocale()), user.getFirstname(), "");
        TextField lastNameField = new TextField(messageSource.getMessage("lastname.label", null, getLocale()), user.getLastname(), "");
        emailField.addClassName("field");
        addressField.addClassName("field");
        phoneField.addClassName("field");
        firstNameField.addClassName("field");
        lastNameField.addClassName("field");

        HorizontalLayout buttonsLayout = new HorizontalLayout();

        Button saveButton = new Button(messageSource.getMessage("save.button", null, getLocale()), event -> {
            user.setEmail(emailField.getValue());
            user.setAddress(addressField.getValue());
            user.setPhonenumber(Integer.parseInt(phoneField.getValue()));
            user.setFirstname(firstNameField.getValue());
            user.setLastname(lastNameField.getValue());

            updateUserProfile(user, token);
            dialog.close();
        });
        saveButton.addClassName("transaction-btn");

        Button cancelButton = new Button(messageSource.getMessage("cancel.button", null, getLocale()), event -> dialog.close());
        cancelButton.addClassName("delete");

        buttonsLayout.add(saveButton, cancelButton);
        VerticalLayout dialogLayout = new VerticalLayout(title, emailField, addressField, phoneField, firstNameField, lastNameField, buttonsLayout);
        dialog.add(dialogLayout);
        dialog.open();
    }

    private void updateUserProfile(User user, String token) {
        String url = "http://localhost:8080/api/user/update/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Content-Type", "application/json");
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        try {
            restTemplate.exchange(url, HttpMethod.PUT, request, User.class);
            Notification.show(messageSource.getMessage("profile.updated", null, getLocale()));
            displayUserProfile(); // Refresh user information
        } catch (Exception e) {
            Notification.show(messageSource.getMessage("error.message", new Object[]{e.getMessage()}, getLocale()));
        }
    }
}