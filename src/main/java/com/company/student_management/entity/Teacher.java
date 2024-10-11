package com.company.student_management.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

@JmixEntity
@Table(name = "TEACHER")
@Entity
public class Teacher {

    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @NotNull
    @Column(name = "CODE", nullable = false)
    private String code;

    @InstanceName
    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Positive(message = "Age must be a positive number")
    @Column(name = "AGE", nullable = false)
    private String age;

    @NotNull
    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Integer version;

    // Default constructor
    public Teacher() {}

    // Parameterized constructor for convenience
    public Teacher(String code, String name, String age, String address) {
        this.code = code;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
