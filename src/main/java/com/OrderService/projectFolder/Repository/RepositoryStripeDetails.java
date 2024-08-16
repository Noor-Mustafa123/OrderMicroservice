package com.OrderService.projectFolder.Repository;


import com.OrderService.projectFolder.Models.UserInfoForStripe;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface RepositoryStripeDetails extends JpaRepository<UserInfoForStripe, Integer> {

    List<UserInfoForStripe> findUserInfoForStripeByEmail (String email);

    List<UserInfoForStripe> findAll();
}
