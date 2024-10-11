package com.company.student_management.view.student;

import com.company.student_management.entity.Grade;
import com.company.student_management.entity.Student;
import com.company.student_management.entity.Subject;
import com.company.student_management.entity.User;
import com.company.student_management.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.app.inputdialog.DialogActions;
import io.jmix.flowui.app.inputdialog.DialogOutcome;
import io.jmix.flowui.app.inputdialog.InputParameter;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Route(value = "students/:id", layout = MainView.class)
@ViewController("Student.detail")
@ViewDescriptor("student-detail-view.xml")
@EditedEntityContainer("studentDc")
public class StudentDetailView extends StandardDetailView<Student> {

    @ViewComponent
    private DataContext dataContext;

    @ViewComponent
    private InstanceContainer<Student> studentDc; // To access the selected student

    @Autowired
    private Notifications notifications;
    @Autowired
    private Dialogs dialogs;

    @ViewComponent
    private CollectionContainer<Subject> subjectDc; // Updated to CollectionContainer for subjects

    @ViewComponent
    private CollectionContainer<Grade> gradeDc; // Updated to CollectionContainer for grades

    @ViewComponent
    private CollectionLoader<Grade> gradeDl; // Updated to CollectionLoader for grades
    @Autowired
    private CurrentAuthentication currentAuthentication;

    @Subscribe("addGradeBtn")
    public void onAddGradeClick(final ClickEvent<JmixButton> event) {
        dialogs.createInputDialog(this)
                .withHeader("Nhập điểm cho sinh viên")
                .withParameter(
                        InputParameter.entityParameter("subject", Subject.class)
                                .withLabel("Chọn môn học")
                )
                .withParameter(
                        InputParameter.doubleParameter("score")
                                .withLabel("Nhập điểm")
                )
                .withActions(DialogActions.OK_CANCEL)
                .withCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(DialogOutcome.OK)) {
                        Subject selectedSubject = closeEvent.getValue("subject");
                        Double scoreDouble = closeEvent.getValue("score");
                        // Chuyển đổi Double sang BigDecimal
                        BigDecimal score = scoreDouble != null ? BigDecimal.valueOf(scoreDouble) : null;
                        // Kiểm tra giá trị
                        if (selectedSubject != null && score != null) {
                            // Lấy student từ data container
                            Student student = studentDc.getItem();

                            // Check if the subject already exists in the current gradeDc
                            boolean subjectExists = gradeDc.getItems().stream()
                                    .anyMatch(grade -> grade.getSubject().equals(selectedSubject));

                            if (subjectExists) {
                                // Notify that the grade for the subject already exists
                                notifications.create("Đã có điểm cho môn học này.")
                                        .withType(Notifications.Type.WARNING)
                                        .withDuration(3000)
                                        .show();
                                return;  // Exit the method to prevent saving
                            }
                            // Tạo đối tượng Grade mới và lưu vào dataContext
                            Grade grade = dataContext.create(Grade.class);
                            grade.setStudent(studentDc.getItem()); // Lấy student từ data container
                            grade.setSubject(selectedSubject);
                            grade.setScore(score);

                            gradeDc.getMutableItems().add(grade);

                        } else {
                            // Thông báo nếu thiếu thông tin
                            notifications.create("Vui lòng chọn môn học và nhập điểm hợp lệ.")
                                    .withType(Notifications.Type.WARNING)
                                    .withDuration(3000)
                                    .show();
                        }
                    }
                }).open();
    }

    @Subscribe(id = "studentDc", target = Target.DATA_CONTAINER)
    public void onStudentDcItemChange(InstanceContainer.ItemChangeEvent<Student> event) {
        Student student = event.getItem();  // Lấy sinh viên hiện tại từ studentDc

        if (student != null) {
            // Thiết lập tham số cho gradeDl loader và tải lại dữ liệu
            gradeDl.setParameter("student", student);
            gradeDl.load();
        }
    }

}