package com.aly8246.stock.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnavailableCtl {
    private Boolean availableService;
    private Long serverId;
}
