package com.jason.restapi.DTO;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDTO {

    private String profileId;

    private String email;

    private String name;

    private String password;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
