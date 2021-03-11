package com.flipper.responses;

import java.util.List;

import com.flipper.models.Flip;

import lombok.Data;

@Data
public class FlipResponse {
    public Boolean error;
    public String message;
    public List<Flip> flips;
    public Flip flip;
    public double totalProfit;
    public double averageProfit;
    public double maxProfit;
}
