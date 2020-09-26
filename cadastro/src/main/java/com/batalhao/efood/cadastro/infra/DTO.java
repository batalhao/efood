package com.batalhao.efood.cadastro.infra;

import javax.validation.ConstraintValidatorContext;

public interface DTO {

  default boolean isValid(ConstraintValidatorContext constraintValidatorContext) {
    return true;
  }

}
