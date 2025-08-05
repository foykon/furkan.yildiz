package com.example.July5.validation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@Builder
public class UserModel {
    @Size(min = 3, message = "İsim en az 3 karakter olmalıdır")
    private String name;
    @Size(min = 3, message = "Soyisim en az 3 karakter olmalıdır")
    private String surname;
    @Min(value = 20, message = "Yaş en az 20 olmalıdır")
    private int age;
    @Email(message = "Geçersiz e-posta adresi")
    private String email;
}
