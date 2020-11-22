package org.zadorozhn.task.model;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Posting {
    private long matDoc;
    private int item;
    private Instant docDate;
    private Instant postingDate;
    private String materialDescription;
    private int Quantity;
    private String bun;
    private double amountLc;
    private String crcy;
    private String userName;
    private boolean isAuthorizedDelivery;
}
