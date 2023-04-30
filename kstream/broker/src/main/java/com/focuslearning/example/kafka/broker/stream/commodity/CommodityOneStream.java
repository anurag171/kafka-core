package com.focuslearning.example.kafka.broker.stream.commodity;


import com.focuslearning.example.kafka.message.OrderMessage;
import com.focuslearning.example.kafka.message.OrderPatternMessage;
import com.focuslearning.example.kafka.message.OrderRewardMessage;
import com.focuslearning.example.kafka.util.CommodityStreamUtil;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;

//@Configuration
public class CommodityOneStream {

	@Bean
	public KStream<String, OrderMessage> kstreamCommodityTrading(StreamsBuilder builder) {
		var stringSerde = Serdes.String();
		var orderSerde = new JsonSerde<>(OrderMessage.class);
		var orderPatternSerde = new JsonSerde<>(OrderPatternMessage.class);
		var orderRewardSerde = new JsonSerde<>(OrderRewardMessage.class);

		var maskedCreditCardStream = builder.stream("t-commodity-order", Consumed.with(stringSerde, orderSerde))
				.mapValues(CommodityStreamUtil::maskCreditCard);

		var patternStream = maskedCreditCardStream.mapValues(CommodityStreamUtil::mapToOrderPattern);
		patternStream.to("t-commodity-pattern-one", Produced.with(stringSerde, orderPatternSerde));

		var rewardStream = maskedCreditCardStream.filter(CommodityStreamUtil.isLargeQuantity())
				.mapValues(CommodityStreamUtil::mapToOrderReward);
		rewardStream.to("t-commodity-reward-one", Produced.with(stringSerde, orderRewardSerde));
		
		maskedCreditCardStream.to("t-commodity-storage-one", Produced.with(stringSerde, orderSerde));
		
		return maskedCreditCardStream;
	}

}
