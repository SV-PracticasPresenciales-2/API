package com.svalero.globalFeed.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column
    @NotBlank(message = "The message cannot be empty")
    @NotNull(message = "The message is obligatory")
    private String message;
    @Column
    private LocalDateTime postDate;
    @Column
    private Integer likes;
    @ManyToOne
    @JoinColumn(name = "postUser")
    private User userPost;
}
