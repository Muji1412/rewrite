package com.example.rewrite.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "REVIEW")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="REVIEW_ID")
    private int reviewId;  // 리뷰 ID

    @Column(name="CONTENT")
    private String content; //리뷰 내용

    @Column(name="REG_DATE")
    private LocalDateTime regDate; //리뷰 작성 일시

    @Column(name="RATING")
    private double rating;  // 평점

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UID", referencedColumnName = "UID")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROD_ID")
    private Product product;


}
