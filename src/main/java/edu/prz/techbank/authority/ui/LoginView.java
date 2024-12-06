package edu.prz.techbank.authority.ui;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import edu.prz.techbank.authority.domain.AuthenticatedUserService;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

  private final AuthenticatedUserService authenticatedUserService;

  public LoginView(AuthenticatedUserService authenticatedUserService) {

    this.authenticatedUserService = authenticatedUserService;
    setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

    LoginI18n i18n = LoginI18n.createDefault();
    i18n.setHeader(new LoginI18n.Header());
    i18n.getHeader().setTitle("Tech-Bank");
    i18n.getHeader().setDescription("Login using user/user or admin/admin");
    i18n.setAdditionalInformation(null);
    setI18n(i18n);

    setForgotPasswordButtonVisible(false);
    setOpened(true);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (authenticatedUserService.getUsingContext().isPresent()) {
      // Already logged in
      setOpened(false);
      event.forwardTo("");
    }

    setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
  }
}
