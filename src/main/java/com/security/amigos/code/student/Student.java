package com.security.amigos.code.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class Student {

    private final Integer studentId;
    private final String studentName;
}