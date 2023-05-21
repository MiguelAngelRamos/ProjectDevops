package com.kibernumacademy.devops.controllers;

import com.kibernumacademy.devops.entitys.Student;
import com.kibernumacademy.devops.services.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class StudentController {

  private final IStudentService service;

  @Autowired
  public StudentController(IStudentService service) {
    this.service = service;
  }

  @GetMapping({"/students", "/"})
  public String listarEstudiantes(Model model) {
    model.addAttribute("students", service.listAllStudents());
    return "students";
  }

  @GetMapping("/students/new")
  public String createStundentForm(Model model) {
    Student stundent = new Student();
    model.addAttribute("student", stundent);
    return "create-student";
  }

  @PostMapping("/students")
  public String saveStudent(@ModelAttribute("student") Student student) {
    service.saveStudent(student);
    return "redirect:/students";
  }

  @GetMapping("/students/edit/{id}")
  public String showFormEditStudent(@PathVariable Long id, Model model) {
    Optional<Student> optionalStudent = service.getStudentById(id);
    if (!optionalStudent.isPresent()) {
      throw new StudentNotFoundException("No se encontró un estudiante con id: " + id);
    }
    Student studentExists = optionalStudent.get();
    model.addAttribute("student", studentExists);
    return "edit_student";
  }

  @PostMapping("/students/{id}")
  public String updatedStudent(@PathVariable Long id, @ModelAttribute("student") Student student, Model model ) {
    Optional<Student> optionalStudent = service.getStudentById(id);
    System.out.println(optionalStudent.isPresent());
    if (!optionalStudent.isPresent()) {
      throw new StudentNotFoundException("No se encontró un estudiante con id: " + id);
    }
    Student studentExists = optionalStudent.get();
    // Actualizar el estudiante...
    studentExists.setId(id);
    studentExists.setName(student.getName());
    studentExists.setLastname(student.getLastname());
    studentExists.setEmail(student.getEmail());

    service.updatedStudent(studentExists);
    return "redirect:/students";
  }

  @GetMapping("/students-delete/{id}")
  public String eliminarEstudiante(@PathVariable Long id) {
    service.deleteStudentById(id);
    return "redirect:/students";
  }


}

class StudentNotFoundException extends RuntimeException {
  public StudentNotFoundException(String message) {
    super(message);
  }
}