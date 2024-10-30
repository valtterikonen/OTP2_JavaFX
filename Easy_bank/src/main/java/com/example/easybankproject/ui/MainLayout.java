package com.example.easybankproject.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.context.MessageSource;

import java.util.Locale;

@CssImport("./styles/mainlayout.css")
@PreserveOnRefresh
public class MainLayout extends AppLayout {

    private final MessageSource messageSource;
    private Button logoutButton; // Store logout button
    private RouterLink profileLink; // Store profile link
    private RouterLink homeLink; // Store home link

    public MainLayout(MessageSource messageSource) {
        this.messageSource = messageSource; // Initialize MessageSource
        setPrimarySection(Section.DRAWER);
        addNavBarContent();
        addDrawerContent();
    }

    private void addNavBarContent() {
        var toggle = new DrawerToggle();
        toggle.setAriaLabel("Toggle menu");
        toggle.setTooltipText("Toggle menu");
        toggle.addClassName("toggle");
        var header = new Header(toggle);
        header.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.BETWEEN, LumoUtility.Padding.End.MEDIUM);

        addToNavbar(false, header);
    }

    private void addDrawerContent() {
        VerticalLayout drawerLayout = new VerticalLayout();
        drawerLayout.setWidthFull();
        drawerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        addToDrawer(drawerLayout);

        Image logo = new Image("./images/easybank_logo-1__1_-removebg-preview.png", "logo");
        logo.setWidth("180px");
        logo.setHeight("200px");
        drawerLayout.add(logo);

        profileLink = createDrawerLink("profile.page", ProfileView.class);
        homeLink = createDrawerLink("home.page", MainView.class);
        logoutButton = createLogoutButton();

        drawerLayout.add(profileLink);
        drawerLayout.add(homeLink);
        drawerLayout.add(logoutButton);

        // Language flags
        HorizontalLayout languageLayout = createLanguageFlags();
        drawerLayout.add(languageLayout);
    }

    private RouterLink createDrawerLink(String textKey, Class<?> navigationTarget) {
        RouterLink link = new RouterLink(getMessage(textKey), (Class<? extends Component>) navigationTarget);
        link.addClassNames("btn-hover", "color-5");
        return link;
    }

    private Button createLogoutButton() {
        Button button = new Button(getMessage("logout.button"), event -> {
            VaadinSession.getCurrent().setAttribute("token", null);
            VaadinSession.getCurrent().getSession().invalidate();
            getUI().ifPresent(ui -> ui.navigate("login"));
        });
        button.addClassNames("logout-btn-hover", "color-5");
        return button;
    }

    private HorizontalLayout createLanguageFlags() {
        Image englishFlag = new Image("images/united-kingdom.png", getMessage("English"));
        englishFlag.addClickListener(event -> changeLanguage(new Locale("en")));

        Image finnishFlag = new Image("images/finland.png", getMessage("Finland"));
        finnishFlag.addClickListener(event -> changeLanguage(new Locale("fi")));

        Image arabicFlag = new Image("images/arabic.png", getMessage("Arabic"));
        arabicFlag.addClickListener(event -> changeLanguage(new Locale("ar")));

        Image koreanFlag = new Image("images/south-korea.png", getMessage("Korean"));
        koreanFlag.addClickListener(event -> changeLanguage(new Locale("ko")));

        englishFlag.setHeight("30px");
        englishFlag.setWidth("30px");
        finnishFlag.setHeight("30px");
        finnishFlag.setWidth("30px");
        arabicFlag.setHeight("30px");
        arabicFlag.setWidth("30px");
        koreanFlag.setHeight("30px");
        koreanFlag.setWidth("30px");

        return new HorizontalLayout(englishFlag, finnishFlag, arabicFlag, koreanFlag);
    }

    private void changeLanguage(Locale locale) {
        VaadinSession.getCurrent().setLocale(locale);
        updateUIText(); // Update the text of UI components
        getUI().ifPresent(ui -> ui.getPage().reload());
    }

    private void updateUIText() {
        // Update the text of the buttons and links
        logoutButton.setText(getMessage("logout.button"));
        profileLink.setText(getMessage("profile.page"));
        homeLink.setText(getMessage("home.page"));
    }

    private String getMessage(String code) {
        Locale currentLocale = VaadinSession.getCurrent().getLocale();
        System.out.println("Current Locale: " + currentLocale); // Debugging
        return messageSource.getMessage(code, null, currentLocale);
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
    }
}