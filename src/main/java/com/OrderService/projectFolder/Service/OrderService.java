package com.OrderService.projectFolder.Service;


import com.OrderService.projectFolder.Models.UserInfoForStripe;
import com.OrderService.projectFolder.Repository.RepositoryStripeDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    public RepositoryStripeDetails stripeRepoObj;

    public String processOrders() {
        List<UserInfoForStripe> listOfOrders = stripeRepoObj.findAll();
        ObjectMapper MapperObj = new ObjectMapper();
//        the mapper library maps the objects returned into a json string
        try {
            return MapperObj.writeValueAsString(listOfOrders);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

