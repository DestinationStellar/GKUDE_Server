package com.tsingle.gkude_server.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseUtil {
    OK(200, "OK!"),
    CREATED(201, "Created!"),
    ACCEPTED(202, "Accepted!"),

    BAD_REQUEST(400, "Bad request!"),
    UNAUTHORIZED(401, "Unauthorized!"),
    FORBIDDEN(403, "Forbidden!"),
    NOT_FOUND(404, "Not found!"),

    INTERNAL_SERVER_ERROR(500, "Internal server error!"),
    NOT_IMPLEMENTED(501, "Not implemented!"),
    BAD_GATE_WAY(502, "Bad gate way");

    private int status;
    private String message;
}


