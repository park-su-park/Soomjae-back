package com.parksupark.soomjae.server.common.validation.validator;

import com.parksupark.soomjae.server.common.validation.StarValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class StarValueValidator implements ConstraintValidator<StarValue, BigDecimal> {

    @Override
    public boolean isValid(BigDecimal star,
        ConstraintValidatorContext constraintValidatorContext) {
        // @NotNull 어노테이션으로 별도 검증 중이므로, 여기서는 유효하다고 처리
        if (star == null) {
            return true;
        }

        return star.multiply(new BigDecimal("2")).stripTrailingZeros().scale() <= 0;
    }
}
