package com.tsingle.gkude_server.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EdukgResponse<T> {
    @NonNull
    private String code;
    @NonNull
    private String msg;
    @NonNull
    private T data;
    private String id;
}
