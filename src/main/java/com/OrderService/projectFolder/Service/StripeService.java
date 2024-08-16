package com.OrderService.projectFolder.Service;


import com.OrderService.projectFolder.Models.EachProductInADC;
import com.OrderService.projectFolder.Models.UserInfoForStripe;
import com.OrderService.projectFolder.Repository.RepositoryStripeDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Autowired
    public RepositoryStripeDetails stripeRepoObj;



    public void saveUserInfoOnOrder (UserInfoForStripe infoObj) {
//forEach loop
        for(EachProductInADC item: infoObj.getItems()) {
            item.setUserObj(infoObj);
        }
        stripeRepoObj.save(infoObj);
        System.out.println("payment sucessfull user data sucessfully saved to the database");
    }


}
