package com.example.si.dto.user;

import com.example.si.entity.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDtoResponse {
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String token;
    private Boolean active;
    @Enumerated(value = EnumType.STRING)
    private Role role;
}
