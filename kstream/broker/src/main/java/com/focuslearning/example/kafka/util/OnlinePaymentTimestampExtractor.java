package com.focuslearning.example.kafka.util;

import com.focuslearning.example.kafka.message.OnlinePaymentMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

public class OnlinePaymentTimestampExtractor implements TimestampExtractor {

	@Override
	public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
		var onlinePaymentMessage = (OnlinePaymentMessage) record.value();

		return onlinePaymentMessage != null
				? LocalDateTimeUtil.toEpochTimestamp(onlinePaymentMessage.getPaymentDateTime())
				: record.timestamp();
	}

}
