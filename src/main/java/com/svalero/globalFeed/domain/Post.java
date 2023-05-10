package com.svalero.globalFeed.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column
    private String message;
    @Column
    private LocalDateTime postDate;
    @Column
    private Integer likes;
    @ManyToOne
    @JoinColumn(name = "postUser")
    private User userPost;
}
