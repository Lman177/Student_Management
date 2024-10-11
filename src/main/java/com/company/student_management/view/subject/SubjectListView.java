package com.company.student_management.view.subject;

import com.company.student_management.entity.Subject;
import com.company.student_management.view.main.MainView;
import com.oracle.svm.core.annotate.Inject;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.util.List;

@Route(value = "subjects", layout = MainView.class)
@ViewController("Subject.list")
@ViewDescriptor("subject-list-view.xml")
@LookupComponent("subjectsDataGrid")
@DialogMode(width = "64em")
public class SubjectListView extends StandardListView<Subject> {

    @Autowired
    private DataManager dataManager;

    @ViewComponent
    private TypedTextField<String> nameField;

    @ViewComponent
    private TypedTextField<String> subjectCodeField;


    @ViewComponent
    private DataGrid<Subject> subjectsDataGrid;

    @Subscribe("searchButton")
    public void onSearchButtonClick(final ClickEvent<JmixButton> event) {
        searchSubjects();
    }

    private void searchSubjects() {
        String name = nameField.getValue() != null ? nameField.getValue().trim() : "";
        String subjectCode = subjectCodeField.getValue() != null ? subjectCodeField.getValue().trim() : "";

        // Base query
        String queryString = "select s from Subject s where 1=1";
        // Use a StringBuilder to construct the query
        StringBuilder queryBuilder = new StringBuilder(queryString);

        // Only add parameters if they have values
        boolean hasName = !name.isEmpty();
        boolean hasSubjectCode = !subjectCode.isEmpty();

        // Check if name is provided and append to query
        if (hasName) {
            queryBuilder.append(" and lower(s.name) like lower(concat('%', :name, '%'))");
        }
        // Check if subjectCode is provided and append to query
        if (hasSubjectCode) {
            queryBuilder.append(" and lower(s.subjectCode) like lower(concat('%', :subjectCode, '%'))");
        }

        // Load the results based on the constructed query
        List<Subject> subjects = dataManager.loadValue(queryBuilder.toString(), Subject.class)
                .parameter("name", hasName ? name : null) // Set parameter if it's used in the query
                .parameter("subjectCode", hasSubjectCode ? subjectCode : null) // Set parameter if it's used in the query
                .list();

        // Display results in the data grid
        subjectsDataGrid.setItems(subjects);
    }


}