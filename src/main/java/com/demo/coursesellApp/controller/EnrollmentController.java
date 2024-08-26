package com.demo.coursesellApp.controller;

import com.demo.coursesellApp.model.Course;
import com.demo.coursesellApp.model.Enrollment;
import com.demo.coursesellApp.service.CourseService;
import com.demo.coursesellApp.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }


    @PostMapping
    public Enrollment createEnrollment(@RequestBody Map<String, Object> payload) {
        Enrollment newEnrollment = new Enrollment();
        newEnrollment.setName((String) payload.get("name"));
        newEnrollment.setEmail((String) payload.get("email"));
        newEnrollment.setDate((String) payload.get("date"));
        newEnrollment.setMessage((String) payload.get("message"));

        Long courseId = Long.valueOf((Integer) payload.get("courseId"));

        // Fetch the Course entity directly
        Course course = courseService.getCourseByIdEntity(courseId);
        newEnrollment.setCourse(course);

        return enrollmentService.saveEnrollment(newEnrollment);
    }

}
