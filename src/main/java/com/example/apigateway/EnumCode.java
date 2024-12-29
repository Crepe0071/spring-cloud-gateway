package com.example.apigateway;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumCode {

    WAS_ROUTE("was-route"),
    WEB_SOCKET_ROUTE("web-socket-route");

    private final String value;
}
