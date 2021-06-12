package com.example.demo.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StaffAccountDTO {
    @NotBlank
    private String phone;

    @NotBlank
    private String password;

    @NotBlank
    private String role;

    private String district;

}
