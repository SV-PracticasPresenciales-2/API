package com.svalero.globalFeed.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    @NotBlank(message = "The message cannot be empty")
    @NotNull(message = "The message is obligatory")
    private String message;
    @NotBlank(message = "The userPost cannot be empty")
    @NotNull(message = "The userPost is obligatory")
    private long userPost;

}
