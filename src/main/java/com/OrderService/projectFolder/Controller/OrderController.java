package com.OrderService.projectFolder.Controller;

import com.OrderService.projectFolder.Models.EachProductInADC;
import com.OrderService.projectFolder.Models.UserInfoForStripe;
import com.OrderService.projectFolder.Service.OrderService;
import com.OrderService.projectFolder.Service.StripeService;
import com.OrderService.projectFolder.StripeConfig.StripeConfig;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/UserData")
@Controller
public class OrderController {


    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private UserInfoForStripe userInfo;

    private static final String signingSecret = "whsec_6B7trDNjC8hcBwCxEPoghEKGA0Aaytla";

    @Autowired
    public StripeService stripeService;
    @Autowired
    public OrderService orderService;

    @Autowired
    public StripeConfig stripeConfig;


    @PostMapping("/Stripe/Authenticate")
    public ResponseEntity<String> stripeMethod(@RequestBody UserInfoForStripe userInfo) {
    stripeConfig.initialization();
        //     creating object to add parameter
        System.out.println("The Stripe Authenticate method is hit");

// The SessionCreateParams.builder() method is a static factory method that returns an instance of the Builder class, which is a static inner class of SessionCreateParams.
// FIXME: NOTE: this is a static factory method which is used to create another class "NOT" a factory object creational design pattern
//  In Java, a static factory method is a static method that returns an instance of the class it's defined in or an instance of another class. The key characteristic of a static factory method is that it's a static method that creates and returns an object.
        SessionCreateParams.Builder parameters = SessionCreateParams.builder()
//                these parameter are getting values from static methods
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://fyp-university.000webhostapp.com/index.html")
                .setCancelUrl("https://fyp-university.000webhostapp.com/index.html");
//                use a foreach loop to iterate over each object in the list


//        FIXME: there is a error in the product total on the stripe page it does not add the quantity of the items in the total meaning that it does not add more items in the total just single product price
        for (EachProductInADC item : userInfo.getItems()) {
            logger.info("This is the item name {} and amount for the product to be saved {}", item.getItemName(), item.getItemQuantity());
            parameters.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setPrice(item.getProductId())
                            .setQuantity(item.getItemQuantity())
                            .build());
        }
        // FIXME: Note: this is a static factory method which is used to create another class "NOT" a factory object creational design
//In Java, a static factory method is a static method that returns an instance of the class it's defined in or an instance of another class. The key characteristic of a static factory method is that it's a static method that creates and returns an object.
        SessionCreateParams newParametersObj = parameters.build();
        try {
//        The Session.create(params) call is used to create a new session with the specified parameters. This call communicates with the Stripe API and returns a Session object.
            Session sessionObj = Session.create(newParametersObj);

//        I will only save the userData in the database only if  the payment intent is completed
//
            this.userInfo = userInfo;
            System.out.println("User Data Recieved");
//         The Session object is then converted to a JSON string using the toJson() method.
            return ResponseEntity.ok(sessionObj.toJson());
        } catch (StripeException e) {
            logger.error("An error occurred: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    //    I have activated the endpoint as a webhook
//    now i make a controleller method that when i locally trigger an event in case of that specific event that method dosee that specific thing
    @PostMapping("/webhook")
    public void paymentSuccessEventHandler(@RequestBody String payload, HttpServletRequest request) {
        String headerSignature = request.getHeader("Stripe-Signature");
        Event event = null;
        try {
            event = Webhook.constructEvent(payload, headerSignature, signingSecret);
//            System.out.println(event);
        } catch (SignatureVerificationException e) {
            logger.error("this is the authentication error in the webhook event creating ", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("this is the exception occurred while creating an event ", e);
        };
//TODO: replace the runtime exceptions with the real response entity expetions so that if real the end user can see them


        try {
//            THREE EVENTS are taking place so only one is accepted other ones are ignored
            if (event.getType().equals("payment_intent.succeeded")) {
                stripeService.saveUserInfoOnOrder(userInfo);
                PaymentIntent PaymentIntent = (PaymentIntent) event.getData().getObject();
                System.out.println(PaymentIntent.toString());
                System.out.println("User Data Saved");
            } else {
                System.out.println("Other events ignored, Event type:" +event.getType());
            }
        } catch (Exception e) {
            logger.error("AN exception occurred in the if else statement", e);
        }

//        then get the object from the session?
//        then save the data
//        why are we returning a response entity and to whom?

    }


    @GetMapping("/OrderDetails")
    public ResponseEntity<String> OrderDetailsMethod() {
        System.out.println("The order details controller method has been hit sucessfully");
        String ordersJson =  orderService.processOrders();
//   FIXME: the items value i am getting is empty find the reason for that
        logger.info("This is the object that we are getting to send back to the client {}", ordersJson);
        return ResponseEntity.ok(ordersJson);
    }




}
