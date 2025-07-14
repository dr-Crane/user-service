package com.example.user.service.dto;

import com.example.user.service.dal.enums.Role;
import com.example.user.service.dal.enums.Status;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateUserDto extends CreateUserDto {

    private Role role;

    private Status status;

}
