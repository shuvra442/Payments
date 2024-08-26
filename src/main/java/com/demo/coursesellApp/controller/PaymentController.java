//package com.demo.coursesellApp.controller;
//
//import com.demo.coursesellApp.model.Course;
//import com.demo.coursesellApp.model.Enrollment;
//import com.demo.coursesellApp.service.CourseService;
//import com.demo.coursesellApp.service.EmailService;
//import com.demo.coursesellApp.service.EnrollmentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.client.SimpleClientHttpRequestFactory;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/payments")
//@CrossOrigin("*")
//public class PaymentController {
//
//    @Value("${ntt.api.url}")
//    private String apiUrl;
//
//    @Value("${ntt.api.key}")
//    private String apiKey;
//
//    @Autowired
//    private EnrollmentService enrollmentService;
//
//    @Autowired
//    private CourseService courseService;
//
//    @Autowired
//    private EmailService emailService;
//
//        // A simple in-memory map of coupon codes to discounts (in percentage)
//    private static final Map<String, Integer> COUPONS = new HashMap<>();
//
//    static {
//        COUPONS.put("SAVE10", 10);
//        COUPONS.put("SAVE20", 20);
//        COUPONS.put("SAVE30", 30);
//    }
//
//    @PostMapping("/create-checkout-session")
//    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody Map<String, Object> payload) {
//        Double price = Double.valueOf(payload.get("price").toString());
//        Double priceInPaise = price * 100;
//        String name = payload.get("name").toString();
//        String email = payload.get("email").toString();
//        String date = payload.get("date").toString();
//        String message = payload.get("message").toString();
//        String courseId = payload.get("courseId").toString();
//
//        Map<String, Object> paymentRequest = new HashMap<>();
//        paymentRequest.put("amount", priceInPaise.longValue());
//        paymentRequest.put("currency", "INR");
//        paymentRequest.put("payment_method", "CARD");
//        paymentRequest.put("description", "Course Payment");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + apiKey);
//
//        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(paymentRequest, headers);
//
//        // Set time because server not respond
////        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
////        factory.setConnectTimeout(10000); // 10 seconds
////        factory.setReadTimeout(10000);    // 10 seconds
//
//
//        RestTemplate restTemplate = new RestTemplate();
//        try {
//            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl /*+ "/create-payment"*/, entity, Map.class);
//            Map<String, String> responseData = new HashMap<>();
//            responseData.put("id", response.getBody().get("id").toString());
//            return ResponseEntity.ok(responseData);
//        } catch (HttpClientErrorException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(e.getStatusCode()).body(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body(null);
//        }
//    }
//
//    /**
//     *
//     * @param
//     * @return success data
//     */
//    @GetMapping("/success")
//    public ResponseEntity<String> paymentSuccess(@RequestParam Map<String, String> params) {
//        String name = params.get("name");
//        String email = params.get("email");
//        String date = params.get("date");
//        String message = params.get("message");
//        Double enrollPrice = Double.valueOf(params.get("price"));
//        Long courseId;
//        try {
//            courseId = Long.valueOf(params.get("courseId"));
//        } catch (NumberFormatException e) {
//            return ResponseEntity.status(400).body("Invalid courseId");
//        }
//
//        Enrollment newEnrollment = new Enrollment();
//        newEnrollment.setName(name);
//        newEnrollment.setEmail(email);
//        newEnrollment.setDate(date);
//        newEnrollment.setMessage(message);
//        newEnrollment.setEnrollPrice(enrollPrice);
//
//        Course course = courseService.getCourseByIdEntity(courseId);
//        newEnrollment.setCourse(course);
//
//        enrollmentService.saveEnrollment(newEnrollment);
//
//        String subject = "Enrollment Confirmation";
//        String emailText = String.format("Dear %s,\n\nYou have successfully enrolled in the course %s.\n\nCourse Details:\nTitle: %s\nDescription: %s\nPrice: %s\nDate: %s\nMessage: %s\n\nThank you!",
//                name, course.getTitle(), course.getTitle(), course.getDescription(), enrollPrice, date, message);
//        emailService.sendSimpleEmail(email, subject, emailText);
//        System.out.println("Api call means mail has been sent");
//
//        return ResponseEntity.ok("Payment successful and enrollment saved");
//    }
//
//    /**
//     * This api for Coupon (Discount price)
//     * @param requestBody
//     * @return
//     */
//    @PostMapping("/apply-coupon")
//    public ResponseEntity<Map<String, Object>> applyCoupon(@RequestBody Map<String, String> requestBody) {
//        Long courseId = Long.parseLong(requestBody.get("courseId"));
//        String couponCode = requestBody.get("couponCode");
//
//        // Fetch course price from database
//        Course course = courseService.getCourseByIdEntity(courseId);
//
//        double originalPrice = course.getPrice();  //100.00;  // Assume you get the course price from your database
//        double discountedPrice = originalPrice;
//
//        // Apply the discount if the coupon code is valid
//        if (COUPONS.containsKey(couponCode)) {
//            int discountPercentage = COUPONS.get(couponCode);
//            discountedPrice = originalPrice - (originalPrice * discountPercentage / 100.0);
//        }
//
//        // Prepare the response
//        Map<String, Object> response = new HashMap<>();
//        response.put("discountedPrice", discountedPrice);
//        response.put("validCoupon", COUPONS.containsKey(couponCode));  // Include if coupon was valid
//
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * This api call when cancel the api
//     * @return
//     */
//    @GetMapping("/cancel")
//    public ResponseEntity<String> paymentCancel() {
//        return ResponseEntity.ok("Payment canceled");
//    }
//}


package com.demo.coursesellApp.controller;

import com.demo.coursesellApp.model.Course;
import com.demo.coursesellApp.model.Enrollment;
import com.demo.coursesellApp.service.CourseService;
import com.demo.coursesellApp.service.EmailService;
import com.demo.coursesellApp.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin("*")
public class PaymentController {


    @Value("${ntt.api.url}")
    private String apiUrl;

    @Value("${ntt.merchant.id}")
    private String merchantId;

    @Value("${ntt.merchant.password}")
    private String merchantPassword;

    @Value("${ntt.product.name}")
    private String productName;

    @Value("${ntt.merchant.name}")
    private String merchantName;

    @Value("${ntt.merchant.returnUrl}")
    private String returnUrl;

    @Value("${ntt.encrypt.key}")
    private String encryptKey;

    @Value("${ntt.decrypt.key}")
    private String decryptKey;



    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EmailService emailService;

    public PaymentController(EnrollmentService enrollmentService, CourseService courseService, EmailService emailService) {
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
        this.emailService = emailService;
    }

    // A simple in-memory map of coupon codes to discounts (in percentage)
    private static final Map<String, Integer> COUPONS = new HashMap<>();

    static {
        COUPONS.put("SAVE10", 10);
        COUPONS.put("SAVE20", 20);
        COUPONS.put("SAVE30", 30);
    }


    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody Map<String, Object> payload) {
        Double price = payload.get("price") != null ? Double.valueOf(payload.get("price").toString()) : 0.0;
        Double priceInPaise = price * 100;
        String name = payload.get("name") != null ? payload.get("name").toString() : "Unknown";
        String email = payload.get("email") != null ? payload.get("email").toString() : "no-email@example.com";
        String date = payload.get("date") != null ? payload.get("date").toString() : "N/A";
        String message = payload.get("message") != null ? payload.get("message").toString() : "";
        String courseId = payload.get("courseId") != null ? payload.get("courseId").toString() : "0";

        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("amount", priceInPaise.longValue());
        paymentRequest.put("currency", "INR");
        paymentRequest.put("merchant_id", merchantId);
        paymentRequest.put("password", merchantPassword);
        paymentRequest.put("product_name", productName);
        paymentRequest.put("return_url", returnUrl);
        paymentRequest.put("description", "Course Payment");
//        paymentRequest.put("encryptKey", encryptKey);

        String encryptedData = encryptData(paymentRequest, encryptKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<String> entity = new HttpEntity<>(encryptedData, headers);

                RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            // print response data
            System.out.println("Response data::: "+response);

            Map<String, String> responseData = new HashMap<>();

            responseData.put("id", response.getBody().get("id").toString());
            return ResponseEntity.ok(responseData);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }


    @GetMapping("/ots-response")
    public ResponseEntity<String> paymentSuccess(@RequestParam Map<String, String> params) {
        String name = params.get("name");
        String email = params.get("email");
        String date = params.get("date");
        String message = params.get("message");
        Double enrollPrice = Double.valueOf(params.get("price"));

        Long courseId;
        try {
            courseId = Long.valueOf(params.get("courseId"));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid courseId");
        }

        Enrollment newEnrollment = new Enrollment();
        newEnrollment.setName(name);
        newEnrollment.setEmail(email);
        newEnrollment.setDate(date);
        newEnrollment.setMessage(message);
        newEnrollment.setEnrollPrice(enrollPrice);

        Course course = courseService.getCourseByIdEntity(courseId);
        newEnrollment.setCourse(course);

        enrollmentService.saveEnrollment(newEnrollment);

        String subject = "Enrollment Confirmation";
        String emailText = String.format("Dear %s,\n\nYou have successfully enrolled in the course %s.\n\nCourse Details:\nTitle: %s\nDescription: %s\nPrice: %s\nDate: %s\nMessage: %s\n\nThank you!",
                name, course.getTitle(), course.getTitle(), course.getDescription(), enrollPrice, date, message);
        emailService.sendSimpleEmail(email, subject, emailText);
        System.out.println("Api call means mail has been sent");

        return ResponseEntity.ok("Payment successful and enrollment saved");
    }

        /**
     * This api for Coupon (Discount price)
     * @param requestBody
     * @return
     */
    @PostMapping("/apply-coupon")
    public ResponseEntity<Map<String, Object>> applyCoupon(@RequestBody Map<String, String> requestBody) {
        Long courseId = Long.parseLong(requestBody.get("courseId"));
        String couponCode = requestBody.get("couponCode");

        // Fetch course price from database
        Course course = courseService.getCourseByIdEntity(courseId);

        double originalPrice = course.getPrice();  //100.00;  // Assume you get the course price from your database
        double discountedPrice = originalPrice;

        // Apply the discount if the coupon code is valid
        if (COUPONS.containsKey(couponCode)) {
            int discountPercentage = COUPONS.get(couponCode);
            discountedPrice = originalPrice - (originalPrice * discountPercentage / 100.0);
        }

        // Prepare the response
        Map<String, Object> response = new HashMap<>();
        response.put("discountedPrice", discountedPrice);
        response.put("validCoupon", COUPONS.containsKey(couponCode));  // Include if coupon was valid

        return ResponseEntity.ok(response);
    }

    private String encryptData(Map<String, Object> data, String key) {
        try {

            String   s = Base64.getEncoder().encodeToString(data.toString().getBytes());
            System.out.println(s);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
