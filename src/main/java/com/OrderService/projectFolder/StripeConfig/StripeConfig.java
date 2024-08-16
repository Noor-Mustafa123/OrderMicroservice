package com.OrderService.projectFolder.StripeConfig;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    @Value("${stripe.api.key}")
    public String stripeApiKey;
//    method stripe key

    @PostConstruct
    public void initialization(){
// this will set the property at the running of hte api call
        Stripe.apiKey= stripeApiKey;
    }

}
