

package com.example.easybankproject.ui;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.MessageSource;
import java.util.Locale;

@PageTitle("Registeration")
@Route(value = "register")
@RouteAlias(value = "")
@CssImport("./styles/register.css")
public class RegisterationView extends Composite<VerticalLayout> {
    private final RestTemplate restTemplate;
    private final MessageSource messageSource;

    public RegisterationView(MessageSource messageSource) {
        this.restTemplate = new RestTemplate();
        this.messageSource = messageSource;

        // Language flags
        Image englishFlag = new Image("images/united-kingdom.png", "English");
        englishFlag.addClickListener(event -> changeLanguage(new Locale("en")));
        Image koreanFlag = new Image("images/south-korea.png", "Korean");
        koreanFlag.addClickListener(event -> changeLanguage(new Locale("ko")));
        Image arabicFlag = new Image("images/arabic.png", "Arabic");
        arabicFlag.addClickListener(event -> changeLanguage(new Locale("ar")));
        Image finnishFlag = new Image("images/finland.png", "Finnish");
        finnishFlag.addClickListener(event -> changeLanguage(new Locale("fi")));

        englishFlag.setHeight("30px");
        englishFlag.setWidth("30px");
        koreanFlag.setHeight("30px");
        koreanFlag.setWidth("30px");
        arabicFlag.setHeight("30px");
        arabicFlag.setWidth("30px");
        finnishFlag.setHeight("30px");
        finnishFlag.setWidth("30px");

        HorizontalLayout languageLayout = new HorizontalLayout(englishFlag, finnishFlag, arabicFlag, koreanFlag);

        TextField username = new TextField(messageSource.getMessage("username.label", null, getLocale()));
        TextField firstname = new TextField(messageSource.getMessage("firstname.label", null, getLocale()));
        TextField lastname = new TextField(messageSource.getMessage("lastname.label", null, getLocale()));
        EmailField emailField = new EmailField(messageSource.getMessage("email.label", null, getLocale()));
        TextField phonenumber = new TextField(messageSource.getMessage("phonenumber.label", null, getLocale()));
        TextField address = new TextField(messageSource.getMessage("address.label", null, getLocale()));
        PasswordField passwordField = new PasswordField(messageSource.getMessage("password.label", null, getLocale()));

        username.addClassName("field");
        firstname.addClassName("field");
        lastname.addClassName("field");
        emailField.addClassName("field");
        phonenumber.addClassName("field");
        address.addClassName("field");
        passwordField.addClassName("field");

        H2 h2 = new H2();
        h2.setText(messageSource.getMessage("welcome.message", null, getLocale()));

        Image logo = new Image("images/easybank_logo.jpg", "Company Logo");
        logo.setHeight("200px");
        logo.setWidth("200px");

        Button registerButton = new Button(messageSource.getMessage("register.button", null, getLocale()), event -> registerUser(username.getValue(), passwordField.getValue(), emailField.getValue(), firstname.getValue(), lastname.getValue(), phonenumber.getValue(), address.getValue()));
        registerButton.addClassName("register-btn");
        RouterLink loginLink = new RouterLink(messageSource.getMessage("login.link", null, getLocale()), LoginView.class);

        HorizontalLayout layoutRow = new HorizontalLayout();
        HorizontalLayout infoRow = new HorizontalLayout();
        VerticalLayout layoutColumn = new VerticalLayout();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        layoutColumn.addClassName("register-column");
        layoutColumn2.addClassName("register-column");
        VerticalLayout main = new VerticalLayout();

        layoutRow.add(logo, h2);
        layoutRow.addClassName("layout-row");
        layoutColumn.add(username, passwordField, firstname, lastname);
        layoutColumn2.add(emailField, phonenumber, address, registerButton, loginLink);
        infoRow.add(layoutColumn, layoutColumn2);
        infoRow.addClassName("info-row");
        main.add(layoutRow, infoRow, languageLayout);
        main.addClassName("main");

        getContent().addClassName("register-body");
        getContent().add(main);
    }

    private void changeLanguage(Locale locale) {
        VaadinSession.getCurrent().setLocale(locale);
        getUI().ifPresent(ui -> ui.getPage().reload());
    }

    private void registerUser(String username, String password, String email, String firstname, String lastname, String phonenumber, String address) {
        String url = "http://localhost:8080/api/user/register";

        String jsonPayload = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"email\":\"%s\",\"firstname\":\"%s\",\"lastname\":\"%s\",\"phonenumber\":\"%s\",\"address\":\"%s\"}",
                username, password, email, firstname, lastname, phonenumber, address);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        try {
            if (username.isEmpty() || password.isEmpty()) {
                Notification.show("Please fill in all fields");
                return;
            }
            System.out.println(jsonPayload);
            String token = restTemplate.postForObject(url, request, String.class);
            Notification.show(token);

            VaadinSession.getCurrent().setAttribute("token", token);
            VaadinSession.getCurrent().setAttribute("username", username);

            getUI().ifPresent(ui -> ui.navigate("main"));

        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage());
        }
    }
}