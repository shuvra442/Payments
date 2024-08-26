package com.demo.coursesellApp.service;

import com.demo.coursesellApp.model.Enrollment;
import com.demo.coursesellApp.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    /**
     *
     * @return List of enrollment
     */
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    /**
     *
     * @param enrollment
     * @return save enrollment
     */
    public Enrollment saveEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }
}
