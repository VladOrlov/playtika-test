package com.playtika.test;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

@Log4j2
@Data(staticConstructor = "create")
public class EventHandler {

  public Event processEvent(Event event){
    try {
      log.info(() -> "************************PROCESSING EVENT**************************");
      log.info(() -> event);
      event.setId(UUID.randomUUID());
      Thread.sleep(1000);
      log.info(() -> "*******************EVENT PROCESSING FINISHED**********************");
      log.info(() -> event);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return event;
  }

}
