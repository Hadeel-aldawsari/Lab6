package com.example.lab6.Model;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Employee {
    @NotEmpty(message = "ID Cannot be null")
    @Size(min=3,message = "ID Length must be more than 2 characters")
    private String ID;

    @NotEmpty(message = "name Cannot be null")
    @Size(min=5,message = " Name Length must be more than 4 characters")
    @Pattern(regexp ="^[a-zA-Z]*$" ) //only characters
    private String name;

    @Email(message = "Email not valid")
    private String email;


    @Pattern(regexp = "^05\\d+$", message = "Phone number should start with 05 ")
    @Size(min=10,max = 10,message = "Phone number should consists of  10 digits")
    private String phoneNumber;

    @NotNull(message = "Age Cannot be null")
    @Min(value = 26,message = "Age should be more than 26")
    @Positive(message = "Age should be positive")
    private int age;

    @NotEmpty(message = "Position Cannot be null")
    @Pattern(regexp = "^(supervisor|coordinator)$", message = "Position should be  supervisor or coordinator")
    private String position;

    @AssertFalse(message = "initially value of onLeave should be false.")
    private boolean onLeave;

    @NotNull(message = "hire Date Cannot be null")
    @PastOrPresent(message = "hire Date should be a date in the present or the past")
    private Date hireDate;

    @NotNull(message = "Annual Leave Cannot be null")
    @Positive(message = "Annual Leave should be positive number")
    private int AnnualLeave;
}
