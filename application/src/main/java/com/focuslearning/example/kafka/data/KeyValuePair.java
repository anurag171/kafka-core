package com.focuslearning.example.kafka.data;

import com.focuslearning.example.kafka.message.SubscriptionOfferMessage;
import com.focuslearning.example.kafka.message.SubscriptionPurchaseMessage;
import com.focuslearning.example.kafka.message.SubscriptionUserMessage;

public record KeyValuePair(String key, Object value) {

    private SubscriptionOfferMessage joiner(SubscriptionPurchaseMessage purchase, SubscriptionUserMessage user) {
        var result = new SubscriptionOfferMessage();

        result.setUsername(purchase.getUsername());
        result.setSubscriptionNumber(purchase.getSubscriptionNumber());
        result.setDuration(user.getDuration());

        return result;
    }

}
