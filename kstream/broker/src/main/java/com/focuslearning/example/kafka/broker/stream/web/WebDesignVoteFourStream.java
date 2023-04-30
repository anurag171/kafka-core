package com.focuslearning.example.kafka.broker.stream.web;

import com.focuslearning.example.kafka.message.WebColorVoteMessage;
import com.focuslearning.example.kafka.message.WebDesignVoteMessage;
import com.focuslearning.example.kafka.message.WebLayoutVoteMessage;
import com.focuslearning.example.kafka.streams.util.WebColorVoteTimestampExtractor;
import com.focuslearning.example.kafka.util.WebLayoutVoteTimestampExtractor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;



//@Configuration
public class WebDesignVoteFourStream {

	@Bean
	public KStream<String, WebDesignVoteMessage> kstreamWebDesignVote(StreamsBuilder builder) {
		var stringSerde = Serdes.String();
		var colorSerde = new JsonSerde<>(WebColorVoteMessage.class);
		var layoutSerde = new JsonSerde<>(WebLayoutVoteMessage.class);
		var designSerde = new JsonSerde<>(WebDesignVoteMessage.class);

		var colorTable = builder
				.stream("t-commodity-web-vote-color",
						Consumed.with(stringSerde, colorSerde, new WebColorVoteTimestampExtractor(), null))
				.mapValues(v -> v.getColor()).toTable();

		var layoutTable = builder
				.stream("t-commodity-web-vote-layout",
						Consumed.with(stringSerde, layoutSerde, new WebLayoutVoteTimestampExtractor(), null))
				.mapValues(v -> v.getLayout()).toTable();

		var joinTable = colorTable.outerJoin(layoutTable, this::voteJoiner,
				Materialized.with(stringSerde, designSerde));

		joinTable.toStream().to("t-commodity-web-vote-four-result");

		joinTable.groupBy((username, votedDesign) -> KeyValue.pair(votedDesign.getColor(), votedDesign.getColor()))
				.count().toStream().print(Printed.<String, Long>toSysOut().withLabel("Vote four - color"));

		joinTable.groupBy((username, votedDesign) -> KeyValue.pair(votedDesign.getLayout(), votedDesign.getLayout()))
				.count().toStream().print(Printed.<String, Long>toSysOut().withLabel("Vote four - layout"));

		return joinTable.toStream();
	}

	private WebDesignVoteMessage voteJoiner(String color, String layout) {
		var result = new WebDesignVoteMessage();

		result.setColor(color);
		result.setLayout(layout);

		return result;
	}
}
