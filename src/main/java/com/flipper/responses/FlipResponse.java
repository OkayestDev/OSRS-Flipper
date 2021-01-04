package com.flipper.responses;

import java.util.List;

import com.flipper.models.Flip;

import lombok.Data;

@Data
public class FlipResponse {
    public List<Flip> flips;
    public Flip flip;
    public int totalProfit;
    public int averageProfit;
    public int maxProfit;
}
