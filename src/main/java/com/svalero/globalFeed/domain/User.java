package com.svalero.globalFeed.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String name;
    @Column
    private String biography;
    @Column
    private LocalDate registerDate;
    @Column
    private Integer phoneNumber;
    @ToString.Exclude
    @OneToMany(targetEntity = Post.class, mappedBy = "userPost")
    @JsonBackReference(value = "postList")
    private List<Post> postList;

}
