package com.focuslearning.example.kafka.broker.stream.customer.preference;

import com.focuslearning.example.kafka.message.CustomerPreferenceAggregateMessage;
import com.focuslearning.example.kafka.message.CustomerPreferenceShoppingCartMessage;
import org.apache.kafka.streams.kstream.Aggregator;

public class CustomerPreferenceShoppingCartAggregator
		implements Aggregator<String, CustomerPreferenceShoppingCartMessage, CustomerPreferenceAggregateMessage> {

	@Override
	public CustomerPreferenceAggregateMessage apply(String key, CustomerPreferenceShoppingCartMessage value,
			CustomerPreferenceAggregateMessage aggregate) {
		aggregate.putShoppingCartItem(value.getItemName(), value.getCartDatetime());

		return aggregate;
	}

}
