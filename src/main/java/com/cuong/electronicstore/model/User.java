package com.cuong.electronicstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractEntity<Long> implements Serializable {

    @Column(unique = true, nullable = false)
    private String keycloakId;

    private String username;

    @Email
    private String email;

    @Size(min = 10, max = 10)
    @Column(unique = true)
    private String phone;

    private String address;
}
