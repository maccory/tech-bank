package edu.prz.techbank.customers.ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import edu.prz.techbank.customers.domain.Customer;
import edu.prz.techbank.customers.domain.CustomerRole;
import edu.prz.techbank.customers.domain.CustomerService;
import edu.prz.techbank.foundation.ui.I18nAware;
import jakarta.annotation.security.PermitAll;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.vaadin.lineawesome.LineAwesomeIcon;

@PageTitle("Customers")
@Route("customers")
@PermitAll
@Uses(Icon.class)
public class CustomerListView extends Div implements I18nAware {

  private Grid<Customer> grid;

  private Filters filters;
  private final CustomerService customerService;

  public CustomerListView(CustomerService customerService) {
    this.customerService = customerService;
    setSizeFull();
    addClassNames("list-view");
    this.filters = new Filters(this::refreshGrid);
    VerticalLayout layout = new VerticalLayout(createMobileFilters(), filters, createToolbar(), createGrid());
    layout.setSizeFull();
    layout.setPadding(false);
    layout.setSpacing(false);
    add(layout);
  }

  private HorizontalLayout createMobileFilters() {
    HorizontalLayout mobileFilters = new HorizontalLayout();
    mobileFilters.setWidthFull();
    mobileFilters.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER,
        LumoUtility.AlignItems.CENTER);
    mobileFilters.addClassName("mobile-filters");

    Icon mobileIcon = new Icon("lumo", "plus");
    Span filtersHeading = new Span("Filters");
    mobileFilters.add(mobileIcon, filtersHeading);
    mobileFilters.setFlexGrow(1, filtersHeading);
    mobileFilters.addClickListener(e -> {
      if (filters.getClassNames().contains("visible")) {
        filters.removeClassName("visible");
        mobileIcon.getElement().setAttribute("icon", "lumo:plus");
      } else {
        filters.addClassName("visible");
        mobileIcon.getElement().setAttribute("icon", "lumo:minus");
      }
    });
    return mobileFilters;
  }

  public static class Filters extends Div implements Specification<Customer>, I18nAware {

    private TextField name;
    private TextField phone;
    private DatePicker startDate;
    private DatePicker endDate;
    private MultiSelectComboBox<String> occupations;
    private CheckboxGroup<CustomerRole> roles;

    public Filters(Runnable onSearch) {

      this.name = new TextField(i18n("name"));
      this.phone = new TextField("Phone");
      this.startDate = new DatePicker("Date of Birth");
      this.endDate = new DatePicker();
      this.occupations = new MultiSelectComboBox<>("Occupation");
      this.roles = new CheckboxGroup<>("Role");

      setWidthFull();
      addClassName("filter-layout");
      addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
          LumoUtility.BoxSizing.BORDER);
      name.setPlaceholder("First or last name");

      occupations.setItems("Insurance Clerk", "Mortarman", "Beer Coil Cleaner", "Scale Attendant");

      roles.setItems(CustomerRole.values());
      roles.setItemLabelGenerator(this::i18n);
      roles.addClassName("double-width");

      Button resetBtn = new Button("Reset");
      resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
      resetBtn.addClickListener(e -> {
        name.clear();
        phone.clear();
        startDate.clear();
        endDate.clear();
        occupations.clear();
        roles.clear();
        onSearch.run();
      });

      Button searchBtn = new Button("Search");
      searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
      searchBtn.addClickListener(e -> onSearch.run());

      Div actions = new Div(resetBtn, searchBtn);
      actions.addClassName(LumoUtility.Gap.SMALL);
      actions.addClassName("actions");

      add(name, phone, createDateRangeFilter(), occupations, roles, actions);
    }

    private Component createDateRangeFilter() {
      startDate.setPlaceholder("From");
      endDate.setPlaceholder("To");

      FlexLayout dateRangeComponent = new FlexLayout(startDate, new Text(" – "), endDate);
      dateRangeComponent.setAlignItems(FlexComponent.Alignment.BASELINE);
      dateRangeComponent.addClassName(LumoUtility.Gap.XSMALL);

      return dateRangeComponent;
    }

    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
      List<Predicate> predicates = new ArrayList<>();

      if (!name.isEmpty()) {
        String lowerCaseFilter = name.getValue().toLowerCase();
        Predicate firstNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
            lowerCaseFilter + "%");
        Predicate lastNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),
            lowerCaseFilter + "%");
        predicates.add(criteriaBuilder.or(firstNameMatch, lastNameMatch));
      }
      if (!phone.isEmpty()) {
        String databaseColumn = "phone";
        String ignore = "- ()";

        String lowerCaseFilter = ignoreCharacters(ignore, phone.getValue().toLowerCase());
        Predicate phoneMatch = criteriaBuilder.like(
            ignoreCharacters(ignore, criteriaBuilder, criteriaBuilder.lower(root.get(databaseColumn))),
            "%" + lowerCaseFilter + "%");
        predicates.add(phoneMatch);

      }
      if (startDate.getValue() != null) {
        String databaseColumn = "birthDate";
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(databaseColumn),
            criteriaBuilder.literal(startDate.getValue())));
      }
      if (endDate.getValue() != null) {
        String databaseColumn = "birthDate";
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.literal(endDate.getValue()),
            root.get(databaseColumn)));
      }
      if (!occupations.isEmpty()) {
        String databaseColumn = "occupation";
        List<Predicate> occupationPredicates = new ArrayList<>();
        for (String occupation : occupations.getValue()) {
          occupationPredicates
              .add(criteriaBuilder.equal(criteriaBuilder.literal(occupation), root.get(databaseColumn)));
        }
        predicates.add(criteriaBuilder.or(occupationPredicates.toArray(Predicate[]::new)));
      }
      if (!roles.isEmpty()) {
        String databaseColumn = "role";
        List<Predicate> rolePredicates = new ArrayList<>();
        for (val role : roles.getValue()) {
          rolePredicates.add(criteriaBuilder.equal(criteriaBuilder.literal(role), root.get(databaseColumn)));
        }
        predicates.add(criteriaBuilder.or(rolePredicates.toArray(Predicate[]::new)));
      }
      return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }

    private String ignoreCharacters(String characters, String in) {
      String result = in;
      for (int i = 0; i < characters.length(); i++) {
        result = result.replace("" + characters.charAt(i), "");
      }
      return result;
    }

    private Expression<String> ignoreCharacters(String characters, CriteriaBuilder criteriaBuilder,
        Expression<String> inExpression) {
      Expression<String> expression = inExpression;
      for (int i = 0; i < characters.length(); i++) {
        expression = criteriaBuilder.function("replace", String.class, expression,
            criteriaBuilder.literal(characters.charAt(i)), criteriaBuilder.literal(""));
      }
      return expression;
    }
  }

  private Component createToolbar() {
    HorizontalLayout toolbar = new HorizontalLayout();
    toolbar.setWidthFull();
    toolbar.addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
        LumoUtility.BoxSizing.BORDER);

    Button backBtn = new Button("Back");
    backBtn.addClickListener(this::onBack);

    HorizontalLayout actions = new HorizontalLayout();
    actions.setWidthFull();
    actions.setAlignItems(Alignment.END);

    Button previewBtn = new Button(LineAwesomeIcon.EYE_SOLID.create());
    previewBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    previewBtn.addClickListener(this::onPreview);
    previewBtn.getElement().getStyle().set("margin-left", "auto");

    Button editBtn = new Button("Edit");
    editBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    editBtn.addClickListener(this::onEdit);

    Button deleteBtn = new Button("Delete");
    deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
    deleteBtn.addClickListener(this::onDelete);

    actions.add(previewBtn, editBtn, deleteBtn);

    toolbar.add(backBtn, actions);

    return toolbar;
  }

  private void onBack(ClickEvent<Button> event) {
  }

  private void onPreview(ClickEvent<Button> event) {
  }

  private void onEdit(ClickEvent<Button> event) {
    grid.getSelectedItems().stream()
        .findFirst()
        .flatMap(item -> getUI())
        .ifPresent(ui -> ui.navigate(CustomerDetailsView.class));
  }

  private void onDelete(ClickEvent<Button> event) {
  }

  private Component createGrid() {
    grid = new Grid<>(Customer.class, false);
    grid.addColumn("firstName").setAutoWidth(true);
    grid.addColumn("lastName").setAutoWidth(true);
    grid.addColumn("email").setAutoWidth(true);
    grid.addColumn("phone").setAutoWidth(true);
    grid.addColumn("birthDate").setAutoWidth(true);
    grid.addColumn("occupation").setAutoWidth(true);
    grid.addColumn("role").setAutoWidth(true);

    grid.setItems(query -> customerService.list(
        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)),
        filters).stream());
    grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

    return grid;
  }

  private void refreshGrid() {
    grid.getDataProvider().refreshAll();
  }

}
