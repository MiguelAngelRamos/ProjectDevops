package com.kibernumacademy.devops.services;

import com.kibernumacademy.devops.entitys.Student;

import java.util.List;
import java.util.Optional;

public interface IStudentService {
  public List<Student> listAllStudents();
  public Student saveStudent(Student student);
  public Optional<Student> getStudentById(Long id);
  public Student updatedStudent(Student student);
  public void deleteStudentById(Long id);
}
