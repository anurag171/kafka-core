package com.focuslearning.example.kafka.broker.stream.customer.preference;

import com.focuslearning.example.kafka.message.CustomerPreferenceAggregateMessage;
import com.focuslearning.example.kafka.message.CustomerPreferenceWishlistMessage;
import org.apache.kafka.streams.kstream.Aggregator;

public class CustomerPreferenceWishlistAggregator
		implements Aggregator<String, CustomerPreferenceWishlistMessage, CustomerPreferenceAggregateMessage> {

	@Override
	public CustomerPreferenceAggregateMessage apply(String key, CustomerPreferenceWishlistMessage value,
			CustomerPreferenceAggregateMessage aggregate) {
		aggregate.putWishlistItem(value.getItemName(), value.getWishlistDatetime());

		return aggregate;
	}

}
