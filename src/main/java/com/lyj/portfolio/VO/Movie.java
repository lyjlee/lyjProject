package com.lyj.portfolio.VO;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class Movie {

    private String subject;
    private String img;
    private String genre;
    private int avg_score;
    private int score;
    private String comment;
    private String user_id;
    private Timestamp submittedAt;

}
