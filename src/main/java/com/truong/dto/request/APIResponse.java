package com.truong.dto.request;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> {
  @Builder.Default
  int code=1000;
  String mesage;
  T result;
}
