package com.company.student_management.view.teacher;

import com.company.student_management.entity.Teacher;
import com.company.student_management.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import io.jmix.flowui.kit.component.button.JmixButton;

import java.util.List;

@Route(value = "teachers", layout = MainView.class)
@ViewController("Teacher.list")
@ViewDescriptor("teacher-list-view.xml")
@LookupComponent("teachersDataGrid")
@DialogMode(width = "64em")
public class TeacherListView extends StandardListView<Teacher> {

    @Autowired
    private DataManager dataManager;

    @ViewComponent
    private TypedTextField<String> nameField;

    @ViewComponent
    private TypedTextField<String> addressField;

    @ViewComponent
    private DataGrid<Teacher> teachersDataGrid;

    @ViewComponent
    private TypedTextField<String> codeField;

    @ViewComponent
    private TypedTextField<String> ageField;

    @Subscribe("teacherSearch")
    public void onSearchButtonClick(final ClickEvent<JmixButton> event) {
        searchTeacher();
    }

    private void searchTeacher() {
        String name = nameField.getValue() != null ? nameField.getValue().trim() : "";
        String address = addressField.getValue() != null ? addressField.getValue().trim() : "";
        String code = codeField.getValue() != null ? codeField.getValue().trim() : "";
        String age = ageField.getValue() != null ? ageField.getValue().trim() : "";


        // Base query
        String queryString = "select t from Teacher t where 1=1";
        StringBuilder queryBuilder = new StringBuilder(queryString);

        // Only add parameters if they have values
        if (!name.isEmpty()) {
            queryBuilder.append(" and lower(t.name) like lower(concat('%', :name, '%'))");
        }

        if (!address.isEmpty()) {
            queryBuilder.append(" and lower(t.address) like lower(concat('%', :address, '%'))");
        }

        if (!code.isEmpty()) {
            queryBuilder.append(" and lower(t.code) like lower(concat('%', :code, '%'))");
        }

        if (!age.isEmpty()) {
            queryBuilder.append(" and t.age = :age");
        }

        // Load the results based on the constructed query
        List<Teacher> teachers = dataManager.loadValue(queryBuilder.toString(), Teacher.class)
                .parameter("name", !name.isEmpty() ? name : null)
                .parameter("address", !address.isEmpty() ? address : null)
                .parameter("code", !code.isEmpty() ? code : null)
                .parameter("age", !age.isEmpty() ? age : null)
                .list();

        teachersDataGrid.setItems(teachers);
    }



}
