package com.focuslearning.example.kafka.broker.serde;

import com.focuslearning.example.kafka.message.PromotionMessage;

public class PromotionSerde extends CustomJsonSerde<PromotionMessage> {

	public PromotionSerde() {
		super(new CustomJsonSerializer<PromotionMessage>(),
				new CustomJsonDeserializer<PromotionMessage>(PromotionMessage.class));
	}

}
