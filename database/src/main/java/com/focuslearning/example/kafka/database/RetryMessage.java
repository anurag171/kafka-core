package com.focuslearning.example.kafka.database;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "replay_message")
public class RetryMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String message;

    private LocalDateTime createdTime;

    private LocalDateTime expireTime;

    private Integer replay =1;

    private Integer status = RecordStatus.NEW.ordinal() ;


}
