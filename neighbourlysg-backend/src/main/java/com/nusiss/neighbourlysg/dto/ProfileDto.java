package com.nusiss.neighbourlysg.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private Long contactNumber;

}
