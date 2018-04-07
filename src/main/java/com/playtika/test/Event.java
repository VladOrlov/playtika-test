package com.playtika.test;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder(builderMethodName = "of", buildMethodName = "create")
public class Event {

  private UUID id;

  private String player;

  private EventType type;

  private BigDecimal sum;

}
