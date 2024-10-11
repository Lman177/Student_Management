package com.company.student_management.view.student;

import com.company.student_management.entity.Student;
import com.company.student_management.entity.Teacher;
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


@Route(value = "students", layout = MainView.class)
@ViewController("Student.list")
@ViewDescriptor("student-list-view.xml")
@LookupComponent("studentsDataGrid")
@DialogMode(width = "64em")
public class StudentListView extends StandardListView<Student> {


    @Autowired
    private DataManager dataManager;

    @ViewComponent
    private TypedTextField<String> nameField;

    @ViewComponent
    private TypedTextField<String> addressField;

    @ViewComponent
    private TypedTextField<String> codeField;

    @ViewComponent
    private TypedTextField<String> ageField;

    @ViewComponent
    private DataGrid<Student> studentsDataGrid;

    @Subscribe("studentSearch")
    public void onSearchButtonClick(ClickEvent<JmixButton> event) {
        searchStudent();
    }

    private void searchStudent() {
        String name = nameField.getValue() != null ? nameField.getValue().trim() : "";
        String address = addressField.getValue() != null ? addressField.getValue().trim() : "";
        String code = codeField.getValue() != null ? codeField.getValue().trim() : "";
        String age = ageField.getValue() != null ? ageField.getValue().trim() : "";


        StringBuilder queryBuilder = new StringBuilder("select s from Student s where 1=1");

        if (!name.isEmpty()) {
            queryBuilder.append(" and lower(s.name) like lower(concat('%', :name, '%'))");
        }

        if (!address.isEmpty()) {
            queryBuilder.append(" and lower(s.address) like lower(concat('%', :address, '%'))");
        }

        if (!code.isEmpty()) {
            queryBuilder.append(" and lower(s.code) like lower(concat('%', :code, '%'))");
        }

        if (!age.isEmpty()) {
            queryBuilder.append(" and s.age = :age");
        }

        List<Student> students = dataManager.load(Student.class)
                .query(queryBuilder.toString())
                .parameter("name", !name.isEmpty() ? name : null)
                .parameter("address", !address.isEmpty() ? address : null)
                .parameter("code", !code.isEmpty() ? code : null)
                .parameter("age", !age.isEmpty() ? age : null)
                .list();

        studentsDataGrid.setItems(students);






    }
}