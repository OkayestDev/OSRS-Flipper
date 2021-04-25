package com.flipper.responses;

import java.util.List;

import com.flipper.models.Alch;

import lombok.Data;

@Data
public class AlchResponse {
    public Boolean error;
    public String message;
    public List<Alch> alchs;
    public Alch alch;
    public String totalProfit;
    public double averageProfit;
    public double maxProfit;
}
