package com.delivery.app.domain.review.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class CreateReviewRequest {

    @NotNull
    private Long orderId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    @NotBlank
    @Size(max = 500)
    private String content;
}
