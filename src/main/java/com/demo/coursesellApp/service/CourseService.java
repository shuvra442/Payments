package com.demo.coursesellApp.service;

import com.demo.coursesellApp.dto.CourseDTO;
import com.demo.coursesellApp.model.Course;
import com.demo.coursesellApp.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    /**
     *
     * @param course
     * @return save the course
     */
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    /**
     *
     * @return List of courses
     */
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream().map(course -> {
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setId(course.getId());
            courseDTO.setTitle(course.getTitle());
            courseDTO.setDescription(course.getDescription());
            courseDTO.setPrice(course.getPrice());

            if (course.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(course.getImage());
                courseDTO.setImage("data:image/jpeg;base64," + base64Image);
            }

            return courseDTO;
        }).collect(Collectors.toList());
    }

    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setTitle(course.getTitle());
        courseDTO.setDescription(course.getDescription());
        courseDTO.setPrice(course.getPrice());

        if (course.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(course.getImage());
            courseDTO.setImage("data:image/jpeg;base64," + base64Image);
        }

        return courseDTO;
    }

    /**
     * To help us find the client
     * @param id
     * @return
     */
       public Course getCourseByIdEntity(Long id) {
             return courseRepository.findById(id)
                     .orElseThrow(() -> new RuntimeException("Course not found"));
    }
}
