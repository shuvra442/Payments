//package com.demo.coursesellApp.controller;
//
//import com.demo.coursesellApp.model.Course;
//import com.demo.coursesellApp.model.Enrollment;
//import com.demo.coursesellApp.service.CourseService;
//import com.demo.coursesellApp.service.EmailService;
//import com.demo.coursesellApp.service.EnrollmentService;
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.checkout.Session;
//import com.stripe.param.checkout.SessionCreateParams;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * CODE:shuvra
// * version: 1.0
// * status: working
// */
//
//@RestController
//@RequestMapping("/api/payments")
//@CrossOrigin(origins = "*")
//public class StripeController {
//
//
//    @Value("${stripe.api.key}")
//    private String stripeApiKey;
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
//    @PostConstruct
//    public void init() {
//        Stripe.apiKey = stripeApiKey;
//    }
//
//    // A simple in-memory map of coupon codes to discounts (in percentage)
//    private static final Map<String, Integer> COUPONS = new HashMap<>();
//
//    static {
//        COUPONS.put("SAVE10", 10);
//        COUPONS.put("SAVE20", 20);
//        COUPONS.put("SAVE30", 30);
//    }
//
//    /**
//     * This method help us to send request to the stripe
//     * @param payload
//     * @return
//     */
//    @PostMapping("/create-checkout-session")
//    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody Map<String, Object> payload) {
////        Double price = Double.valueOf(payload.get("price").toString()) * 100;
//        Double price = Double.valueOf(payload.get("price").toString());
//        Double priceInPaise = price * 100;
//        String name = payload.get("name").toString();
//        String email = payload.get("email").toString();
//        String date = payload.get("date").toString();
//        String message = payload.get("message").toString();
//        String courseId = payload.get("courseId").toString();
//
//        SessionCreateParams params = SessionCreateParams.builder()
//                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
//                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.ACSS_DEBIT)
//                .setMode(SessionCreateParams.Mode.PAYMENT)
//                .setSuccessUrl(String.format("http://localhost:8081/success?name=%s&email=%s&date=%s&message=%s&price=%s&courseId=%s",
//                        name, email, date, message, price, courseId))
//                .setCancelUrl("http://localhost:8080/api/payments/cancel")
//                .addLineItem(
//                        SessionCreateParams.LineItem.builder()
//                                .setPriceData(
//                                        SessionCreateParams.LineItem.PriceData.builder()
//                                                .setCurrency("inr") //usd
//                                                .setProductData(
//                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                                                                .setName("Course Payment")
//                                                                .build()
//                                                )
//                                                .setUnitAmount(priceInPaise.longValue())
//                                                .build()
//                                )
//                                .setQuantity(1L)
//                                .build()
//                )
//                .build();
//
//        try {
//            Session session = Session.create(params);
//            Map<String, String> responseData = new HashMap<>();
//            responseData.put("id", session.getId());
//            return ResponseEntity.ok(responseData);
//        } catch (StripeException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body(null);
//        }
//    }
//
//    /**
//     * After successfully payment this api call where save the details.
//     * @param params
//     * @return
//     */
//    @GetMapping("/success")
//    public ResponseEntity<String> paymentSuccess(@RequestParam Map<String, String> params) {
//        // Extract enrollment details from the request parameters
//        String name = params.get("name");
//        String email = params.get("email");
//        String date = params.get("date");
//        String message = params.get("message");
//        Double enrollPrice = Double.valueOf(params.get("price"));
//        Long courseId = null;
//        try {
//            courseId = Long.valueOf(params.get("courseId"));
//        } catch (NumberFormatException e) {
//            return ResponseEntity.status(400).body("Invalid courseId");
//        }
//
//        // Create and save the enrollment
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
//        // Send email notification
//        String subject = "Enrollment Confirmation";
//        String emailText = String.format("Dear %s,\n\nYou have successfully enrolled in the course %s.\n\nCourse Details:\nTitle: %s\nDescription: %s\nPrice: %s\nDate: %s\nMessage: %s\n\nThank you!",
//                name, course.getTitle(), course.getTitle(), course.getDescription(), enrollPrice, date, message);
//        emailService.sendSimpleEmail(email, subject, emailText);
//        System.out.println("Api call means mail has been sent");
//
//        return ResponseEntity.ok("Payment successful and enrollment saved");
//    }
//
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