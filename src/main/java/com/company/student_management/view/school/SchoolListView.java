package com.company.student_management.view.school;

import com.company.student_management.entity.School;
import com.company.student_management.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Route(value = "schools", layout = MainView.class)
@ViewController("School.list")
@ViewDescriptor("school-list-view.xml")
@LookupComponent("schoolsDataGrid")
@DialogMode(width = "64em")
public class SchoolListView extends StandardListView<School> {

    @ViewComponent
    private TypedTextField<String> nameField;

    @ViewComponent
    private TypedTextField<String> addressField;

    @ViewComponent
    private DataGrid<School> schoolsDataGrid;

    @ViewComponent
    private TypedTextField<String> codeField;

    @Autowired
    private DataManager dataManager;

    @Subscribe("schoolSearch")
    public void onSearchButtonClick(final ClickEvent<JmixButton> event) {
        searchSchool();
    }

    private void searchSchool() {
        String name = nameField.getValue() != null ? nameField.getValue().trim() : "";
        String address = addressField.getValue() != null ? addressField.getValue().trim() : "";
        String code = codeField.getValue() != null ? codeField.getValue().trim() : "";

        String queryString = "select s from School s where 1=1";
        StringBuilder queryBuilder = new StringBuilder(queryString);

        // Only add parameters if they have values
        if (!name.isEmpty()) {
            queryBuilder.append(" and lower(s.name) like lower(concat('%', :name, '%'))");
        }

        if (!address.isEmpty()) {
            queryBuilder.append(" and lower(s.address) like lower(concat('%', :address, '%'))");
        }

        if (!code.isEmpty()) {
            queryBuilder.append(" and lower(s.code) like lower(concat('%', :code, '%'))");
        }

        List<School> schools = dataManager.load(School.class)
                .query(queryBuilder.toString())
                .parameter("name", !name.isEmpty() ? name : null)
                .parameter("address", !address.isEmpty() ? address : null)
                .parameter("code", !code.isEmpty() ? code : null)
                .list();
        schoolsDataGrid.setItems(schools);
    }

}