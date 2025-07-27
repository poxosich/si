package com.example.si.dto.user;

import com.example.si.entity.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDtoRequest {

    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String token;
    private Boolean active;
    @Enumerated(value = EnumType.STRING)
    private Role role;
}
