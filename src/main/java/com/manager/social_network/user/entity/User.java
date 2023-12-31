package com.manager.social_network.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;
import java.sql.Date;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true)
    private String username;
    @Column(name = "full_name")
    private String fullName;
    @Column(unique = true)
    private String email;
    @JsonIgnore
    @Column
    private String password;
    @JsonIgnore
    @Column
    private String role;
    @Column
    private Date birthday;
    @Column
    private String job;
    @Column
    private String living;
    @JsonIgnore
    @Column(name = "delete_flag")
    private Integer deleteFlag;
    @Column(name = "create_at")
    private Instant createAt;

}