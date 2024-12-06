package edu.prz.techbank.system.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import edu.prz.techbank.customers.ui.CustomerListView;
import edu.prz.techbank.foundation.ui.I18nAware;

public class MenuDialog extends Dialog implements I18nAware {

  public MenuDialog(String title, AccessAnnotationChecker accessChecker) {

    setHeaderTitle(title);

    Nav nav = new Nav();

    nav.addClassNames(Display.FLEX, Overflow.AUTO, Padding.Horizontal.MEDIUM, Padding.Vertical.XSMALL);
    UnorderedList list = new UnorderedList();
    list.addClassNames(Display.FLEX, Gap.SMALL, ListStyleType.NONE, Margin.NONE, Padding.NONE);
    nav.add(list);

    for (MenuItemInfo menuItem : createMenuItems()) {
      if (accessChecker.hasAccess(menuItem.getView())) {
        list.add(menuItem);
      }
    }

    add(nav);
  }

  public static class MenuItemInfo extends ListItem {

    private final Class<? extends Component> view;

    public MenuItemInfo(MenuDialog menuDialog, String menuTitle, Component icon, Class<? extends Component> view) {
      this.view = view;

      RouterLink link = new RouterLink() {
        @Override
        public void afterNavigation(AfterNavigationEvent event) {
          super.afterNavigation(event);
          menuDialog.close();
        }
      };
      link.addClassNames(Display.FLEX, Gap.XSMALL, Height.MEDIUM, AlignItems.CENTER, Padding.Horizontal.SMALL,
          TextColor.BODY);
      link.setRoute(view);

      Span text = new Span(menuTitle);
      text.addClassNames(FontWeight.MEDIUM, FontSize.MEDIUM, Whitespace.NOWRAP);

      if (icon != null) {
        link.add(icon);
      }
      link.add(text);
      add(link);
    }

    public Class<?> getView() {
      return view;
    }
  }

  private MenuItemInfo[] createMenuItems() {
    return new MenuItemInfo[]{
        new MenuItemInfo(this, i18n("customers"), null, CustomerListView.class)
    };
  }
}
