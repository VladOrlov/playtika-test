package com.playtika.test;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.util.Optional;

@Data(staticConstructor = "create")
@Log4j2
public class EventProducer {

  public Event createEvent() {
    try {
      Thread.sleep(1000);
      return Event.of()
          .player("John Snow")
          .type(EventType.GET_CARD)
          .sum(BigDecimal.valueOf(0))
          .create();
    } catch (InterruptedException e) {
      log.error(e::getMessage);
    }
    return null;
  }
}
