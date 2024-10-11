package com.company.student_management.view.subject;

import com.company.student_management.entity.Subject;
import com.company.student_management.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "subjects/:id", layout = MainView.class)
@ViewController("Subject.detail")
@ViewDescriptor("subject-detail-view.xml")
@EditedEntityContainer("subjectDc")
public class SubjectDetailView extends StandardDetailView<Subject> {
}