package com.lyj.portfolio.board;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class BoardForm {
    @NotBlank
    @Length(max = 50)
    private String title;

    @NotBlank
    private String description;

}
