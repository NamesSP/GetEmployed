package com.example.Validation;

import com.example.dbconnect.DbConnect;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

//    @Autowired
//    private UserRepository userRepository;

//    @PersistenceContext
//    EntityManager em;

//    public  UniqueEmailValidator() {
//        // No-arg constructor required
//    }

//    @Autowired
//    public void setUserRepository(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//    private UserRepository userRepository;
//
//    @Autowired
//    private ApplicationContext applicationContext;
//
//    @Override
//    public void initialize(UniqueEmail constraintAnnotation) {
//        this.userRepository = applicationContext.getBean(UserRepository.class);
//    }
//
//    @Override
//    public boolean isValid(String email, ConstraintValidatorContext context) {
//        if (email == null || email.trim().isEmpty()) {
//            return true; // `@NotBlank` will handle blank check
//        }
//        return !userRepository.existsByEmail(email);
//    }
//@Override
//public void initialize(UniqueEmail constraintAnnotation) {
//    this.userRepository = applicationContext.getBean(UserRepository.class);
//    System.out.println("UniqueEmailValidator initialized, userRepository is " + (userRepository == null ? "null" : "NOT null"));
//}

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        //System.out.println("UniqueEmailValidator: isValid called, userRepository is " + (userRepository == null ? "null" : "NOT null"));
        if (email==null || email.trim().isEmpty()) {
            return true; // validation handled elsewhere
        }
//        return !userrepository.existsByEmail(email);

//        return true;
        try{
            PreparedStatement ps= DbConnect.getConnection().prepareStatement("Select * from user where email=?");
            ps.setString(1,email);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return false;
            }
        }catch (Exception e){
            return true;
        }
        return true;
    }
}



