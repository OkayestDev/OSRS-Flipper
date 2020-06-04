package com.flipper.model;

import lombok.Data;

import java.util.List;

@Data
public class AccountData {
    private List<Transaction> buys;
    private List<Transaction> sells;
}
