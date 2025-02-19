package org.paygateway.controller;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.paygateway.exceptions.OrderException;
import org.paygateway.model.Order;
import org.paygateway.pojo.ApiResponse;
import org.paygateway.pojo.PaymentLinkResponse;
import org.paygateway.repository.OrderRepository;
import org.paygateway.repositoryimpl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String secret;

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/payments/{orderId}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink
            (@PathVariable Long orderId, @RequestHeader("authorization") String jwt) throws OrderException, RazorpayException {
        Order order = orderService.findOrderById(orderId);

        try {
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, secret);
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", order.getTotalDiscountedPrice() * 100);
            paymentLinkRequest.put("currency", "INR");

            JSONObject customer = new JSONObject();
            customer.put("name", order.getUser().getFirstName());
            customer.put("email", order.getUser().getEmail());
            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("sms", true);
            notify.put("email", true);

            paymentLinkRequest.put("notify", notify);

            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);
            String paymentLinkId = paymentLink.get("id");
            String paymentLinkUrl = paymentLink.get("short_url");
            PaymentLinkResponse response = new PaymentLinkResponse(paymentLinkId, paymentLinkUrl);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            throw new RazorpayException(e.getMessage());
        }
    }

    @GetMapping("/payments")
    public ResponseEntity<ApiResponse> redirect(@RequestParam(name = "payment_id") String paymentId,
                                                @RequestParam(name = "order_id") Long orderId) throws OrderException, RazorpayException {
        Order order = orderService.findOrderById(orderId);
        System.out.println("Order "+order);
        RazorpayClient razorpayClient = new RazorpayClient(apiKey, secret);

        try {
            Payment payment = razorpayClient.payments.fetch("pay_"+paymentId);
            System.out.println("PAYMENT "+payment);
            if (payment.get("status").equals("captured")) {
                if(!payment.get("amount").equals(order.getTotalDiscountedPrice()*100)) throw new RazorpayException("Order Amount and Razorpay transaction amount is not valid");
                order.getPaymentDetails().setPaymentId(paymentId);
                order.getPaymentDetails().setStatus("COMPLETED");
                order.setOrderStatus("PLACED");
                order.getPaymentDetails().setStatus("CAPTURED");
                order.getPaymentDetails().setRazorpayPaymentStatus("SUCCESS");
                order.getPaymentDetails().setRazorpayPaymentId(payment.get("id"));
                order.getPaymentDetails().setRazorpayPaymentLinkReferenceId(payment.get("order_id"));

                // TODO: Success payment link is pending to save in DB
                //order.getPaymentDetails().setRazorpayPaymentLinkReferenceId();
                order.getPaymentDetails().setPaymentMethod(payment.get("method"));

                orderRepository.save(order);
            }
            ApiResponse apiResponse = new ApiResponse("Your Order Placed", true);
            return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            throw new RazorpayException(e.getMessage());
        }
    }

}
