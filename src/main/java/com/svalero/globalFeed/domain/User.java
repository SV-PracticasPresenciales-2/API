package com.svalero.globalFeed.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    @NotBlank(message = "The username cannot be empty")
    @NotNull(message = "The username is obligatory")
    private String username;
    @Column
    @NotBlank(message = "The password cannot be empty")
    @NotNull(message = "The password is obligatory")
    private String password;
    @Column
    @NotBlank(message = "The name cannot be empty")
    @NotNull(message = "The name is obligatory")
    private String name;
    @Column
    private String biography;
    @Column
    private LocalDate registerDate;
    @Column
    @NotBlank(message = "The phone number cannot be empty")
    @NotNull(message = "The phone number is obligatory")
    private Integer phoneNumber;
    @Column
    private boolean active = true;
    @ToString.Exclude
    @OneToMany(targetEntity = Post.class, mappedBy = "userPost")
    @JsonBackReference(value = "postList")
    private List<Post> postList;
    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}