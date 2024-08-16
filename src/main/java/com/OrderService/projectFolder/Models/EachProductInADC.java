package com.OrderService.projectFolder.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "List_ITEMS_Objects")
public class EachProductInADC {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String itemName;
    private Long itemQuantity;
    private String productId;
    //    The @JsonManagedReference annotation is the forward part of the relationship and @JsonBackReference is the back part of the relationship. Jackson will serialize the entity with @JsonManagedReference normally and will omit the field annotated with @JsonBackReference.
    @JsonBackReference
    @ManyToOne
    private UserInfoForStripe userObj;

    public EachProductInADC( String itemName, Long itemQuantity, String productId, UserInfoForStripe userObj) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.productId = productId;
        this.userObj = userObj;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserInfoForStripe getUserObj() {
        return userObj;
    }

    public void setUserObj(UserInfoForStripe userObj) {
        this.userObj = userObj;
    }

    public EachProductInADC() {
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Long itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}


