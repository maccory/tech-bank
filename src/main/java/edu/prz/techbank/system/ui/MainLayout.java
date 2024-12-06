package edu.prz.techbank.system.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import edu.prz.techbank.authority.domain.AuthenticatedUserService;
import edu.prz.techbank.authority.domain.User;
import edu.prz.techbank.foundation.ui.I18nAware;
import java.io.ByteArrayInputStream;
import java.util.Optional;
import lombok.val;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout implements I18nAware {

  private AuthenticatedUserService authenticatedUserService;
  private AccessAnnotationChecker accessChecker;

  public MainLayout(AuthenticatedUserService authenticatedUserService, AccessAnnotationChecker accessChecker) {
    this.authenticatedUserService = authenticatedUserService;
    this.accessChecker = accessChecker;

    addToNavbar(createHeaderContent());
  }

  private Component createHeaderContent() {
    Header header = new Header();
    header.addClassNames(BoxSizing.BORDER, Display.FLEX, FlexDirection.COLUMN, Width.FULL);
    header.add(createStatusBar(), createSystemMenu());
    return header;
  }

  private Component createStatusBar() {
    Div layout = new Div();
    layout.addClassNames(Display.FLEX, AlignItems.CENTER, Padding.Horizontal.MEDIUM);

    H1 appName = new H1("Tech-Bank");
    appName.addClassNames(Margin.Vertical.MEDIUM, Margin.End.AUTO, "tech-bank");
    layout.add(appName);

    Optional<User> maybeUser = authenticatedUserService.getUsingContext();
    if (maybeUser.isPresent()) {
      User user = maybeUser.get();

      Avatar avatar = new Avatar(user.getName());
      if (user.getProfilePicture() != null) {
        StreamResource resource = new StreamResource("profile-pic",
            () -> new ByteArrayInputStream(user.getProfilePicture()));
        avatar.setImageResource(resource);
      }
      avatar.setThemeName("xsmall");
      avatar.getElement().setAttribute("tabindex", "-1");

      MenuBar userMenu = new MenuBar();
      userMenu.setThemeName("tertiary-inline contrast");

      MenuItem userName = userMenu.addItem("");
      Div div = new Div();
      div.add(avatar);
      div.add(user.getName());
      div.add(new Icon("lumo", "dropdown"));
      div.getElement().getStyle().set("display", "flex");
      div.getElement().getStyle().set("align-items", "center");
      div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
      userName.add(div);
      userName.getSubMenu().addItem(i18n("signOut"), e -> authenticatedUserService.logout());

      layout.add(userMenu);
    } else {
      Anchor loginLink = new Anchor("login", i18n("signIn"));
      layout.add(loginLink);
    }

    return layout;
  }

  private Component createSystemMenu() {
    MenuBar menu = new MenuBar();
    menu.setThemeName("tertiary");
    val icon = LineAwesomeIcon.USER_FRIENDS_SOLID.create();
    icon.getStyle().set("marginRight", "var(--lumo-space-s)");
    MenuItem customers = menu.addItem(icon);
    customers.add("Klienci");
    customers.addClassNames(Display.FLEX, Gap.XSMALL, Height.MEDIUM, AlignItems.CENTER, Padding.Horizontal.SMALL,
        TextColor.BODY);
    customers.addClickListener(e -> {
      val d = new MenuDialog(customers.getText(), accessChecker);
      getUI().get().add(d);
      d.open();
    });
    return menu;
  }

}
