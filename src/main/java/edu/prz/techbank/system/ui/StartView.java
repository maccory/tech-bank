package edu.prz.techbank.system.ui;

import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.prz.techbank.foundation.ui.I18nAware;
import jakarta.annotation.security.PermitAll;

@PageTitle("Start")
@Route("")
@PermitAll
@Uses(Icon.class)
public class StartView extends Div implements I18nAware {

  public StartView() {
    setSizeFull();
    VerticalLayout layout = new VerticalLayout();
    layout.setSizeFull();
    layout.setPadding(false);
    layout.setSpacing(false);
    add(layout);
  }

}
