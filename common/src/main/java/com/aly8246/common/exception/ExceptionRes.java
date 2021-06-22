package com.aly8246.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionRes {
    private String service;
    private String uri;
    private String errorMsg;
}
