package com.example.Validation;

import com.example.dbconnect.DbConnect;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Component
@AllArgsConstructor

public class UniqueUserNameValidator implements ConstraintValidator<UniqueUserName, String> {

//    @Autowired
//    private final UserRepository userRepository;

//    private ApplicationContext applicationContext;
//    @Override
//    public void initialize(UniqueUserName constraintAnnotation) {
//        this.userRepository = applicationContext.getBean(UserRepository.class);
//    }

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext context) {
        if (userName == null || userName.trim().isEmpty()) {
            return true; // `@NotBlank` will handle blank check separately
        }
//        return !userRepository.existsByUserName(userName);
////        return false;
        try{
        PreparedStatement ps=DbConnect.getConnection().prepareStatement("Select * from user where user_name=?");
                 ps.setString(1,userName);
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



