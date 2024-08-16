package com.OrderService.projectFolder.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "User_Details_Stripe")
public class UserInfoForStripe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderId;

    private String email;
    private String address;
    //    The @OneToMany annotation in Spring Data JPA is used to establish a one-to-many relationship between two entities. In your case, it seems like you want to establish a relationship where one user can have many products in their cart.
//    the mapped by property give the abillity of updating the foriegn keys of the relationship to the userObj
//    the cascade = CascadeType.ALL which means that any operation (including deletion) performed on a UserInfoForStripe entity will be cascaded to the associated EachProductInADC entities.
//    So, if you delete a UserInfoForStripe entity, all the associated EachProductInADC entities will be deleted as wel due to the cascade property being in the userinfoforstripe entity
// When you fetch a UserInfoForStripe entity from the database, JPA will immediately fetch all the associated EachProductInADC entities as well, instead of waiting until you access the items collection. This is done to avoid LazyInitializationException which occurs when you try to access a lazily-loaded collection outside of the Hibernate Session.
//  TO STOP INFINTE RECURSION:  The @JsonManagedReference annotation is the forward part of the relationship and @JsonBackReference is the back part of the relationship. Jackson will serialize the entity with @JsonManagedReference normally and will omit the field annotated with @JsonBackReference.
    @JsonManagedReference
    @OneToMany( mappedBy = "userObj" , cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<EachProductInADC> items ;




    public List<EachProductInADC> getItems() {
        return items;
    }

    public void setItems(List<EachProductInADC> items) {
        this.items = items;
    }

    public UserInfoForStripe() {

    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}

