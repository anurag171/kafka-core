package com.focuslearning.example.kafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.focuslearning.example.kafka.producer.KafkaProducer;
import com.focuslearning.example.kafka.streams.PromotionUpperCaseSteam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;



@RestController
public class CommodityController {
	
//	@Autowired
//	RestTemplate restTemplate;
	
	@Autowired
	KafkaProducer kafkaProducer;

	/*@Autowired
	PromotionUpperCaseSteam promotionUpperCaseSteam;*/
	
	String response = "{\"data\":{\"success\":true,\"timestamp\":1678041000,\"date\":\"2023-03-05\",\"base\":\"USD\",\"rates\":{\"AED\":3.672955,\"AFN\":89.37602602457984,\"ALL\":108.39350000000049,\"AMD\":388.499,\"ANG\":1.796355,\"AOA\":506.5000000000112,\"ARS\":198.33665633333314,\"AWG\":1.8025,\"AZN\":1.7035187065531514,\"BAM\":1.841154,\"BBD\":2,\"BDT\":107.0233833204588,\"BGN\":1.8395075,\"BHD\":0.376702,\"BIF\":2077.75,\"BMD\":1,\"BND\":1.3453325617551335,\"BOB\":6.921396423485428,\"BRL\":5.195816666666667,\"BSD\":1,\"BTN\":81.84111122922955,\"BWP\":13.2353155,\"BYN\":2.5247604999999007,\"BYR\":25247.60000000108,\"BZD\":2.0163524962029205,\"CAD\":1.3593133333333334,\"CDF\":2045,\"CHF\":0.936438270387958,\"CLF\":0.0291071999986258,\"CLP\":802.4800000000215,\"CNY\":6.9076,\"COP\":4795.072403333334,\"CRC\":557.438735666678,\"CUC\":1,\"CVE\":103.9955,\"CZK\":22.080049497158026,\"DJF\":177.76896642769594,\"DKK\":6.9990011266300325,\"DOP\":55.32416666666662,\"DZD\":136.47926666666655,\"EGP\":30.783762950227498,\"ETB\":53.74373190125368,\"FJD\":2.215487201476874,\"FKP\":0.830584540514671,\"GBP\":0.8305573333333333,\"GEL\":2.5925,\"GHS\":12.710433333333333,\"GIP\":0.8305843641873827,\"GMD\":61.09124874675143,\"GNF\":8802.48929563869,\"GTQ\":7.813698974100719,\"GYD\":211.06723871462523,\"HKD\":7.849618333333333,\"HNL\":24.659899999999976,\"HRK\":7.085289999999999,\"HTG\":151.18545513690168,\"HUF\":356.1496995930784,\"IDR\":15284.283333333333,\"ILS\":3.6692148527682353,\"INR\":81.7158494944718,\"IQD\":1460.5,\"ISK\":140.94999999999962,\"JMD\":153.31925922305547,\"JOD\":0.7093333333333333,\"JPY\":135.8503354251715,\"KES\":127.83333333333333,\"KGS\":87.42000000000017,\"KHR\":4057.2400000002513,\"KMF\":463.0679415000006,\"KRW\":1295.7557839869119,\"KWD\":0.3069633333333333,\"KYD\":0.8334194910222283,\"KZT\":433.72944133332754,\"LAK\":16875.118743932002,\"LBP\":15076.927164,\"LKR\":340.7157483333355,\"LRD\":159.44897669205088,\"LSL\":18.1569,\"LYD\":4.823749676081887,\"MAD\":10.38454979753576,\"MDL\":18.804891,\"MGA\":4317.526666666107,\"MKD\":58.0915165,\"MMK\":2100.670965,\"MNT\":3470.28357977513,\"MOP\":8.086391318493547,\"MRO\":356.5819999999986,\"MUR\":46.314373948672255,\"MVR\":15.3524,\"MWK\":1018.7500000000414,\"MXN\":17.967266666666667,\"MYR\":4.476,\"MZN\":63.86497632384554,\"NAD\":18.1569,\"NGN\":460.06333333333333,\"NIO\":36.39609996675462,\"NOK\":10.40111170159231,\"NPR\":131.33960679555415,\"NZD\":1.6068429936240698,\"OMR\":0.3847037932852271,\"PAB\":1,\"PEN\":3.7848499255878223,\"PGK\":3.5227150000000007,\"PHP\":55.00073499999988,\"PKR\":277.4500000000002,\"PLN\":4.42973927668441,\"PYG\":7229.863089102822,\"QAR\":3.6410000000000005,\"RON\":4.631759972019275,\"RSD\":110.23680333333375,\"RUB\":75.499995,\"RWF\":1092.624985699578,\"SAR\":3.75167,\"SBD\":8.266720499999968,\"SCR\":14.01357970188504,\"SEK\":10.46665,\"SHP\":0.8305844448975744,\"SLL\":18707.5,\"SOS\":568.6374667516402,\"SRD\":33.613250000000015,\"STD\":23044.83743851032,\"SVC\":8.750830421181165,\"SZL\":18.156890548496715,\"THB\":34.5561765,\"TJS\":10.4706835,\"TMT\":3.5025,\"TND\":3.1352499800653852,\"TOP\":2.3575549605417474,\"TRY\":18.754845,\"TTD\":6.780705816981301,\"TWD\":30.514599832220245,\"TZS\":2340,\"UAH\":36.93938106545118,\"UGX\":3710.509683333333,\"UYU\":39.11767037863586,\"UZS\":11380,\"VES\":24.286401666666666,\"VND\":23726.249934115895,\"VUV\":115.592,\"WST\":2.71116,\"XAF\":620.1548756666494,\"XAG\":0.047023074376431,\"XAU\":0.00053864799353622,\"XCD\":2.70127439820085,\"XOF\":617.2815423333175,\"XPD\":0.00073421439060206,\"XPF\":112.2532627779424,\"XPT\":0.0010214504596527,\"YER\":250.2833509999999,\"ZAR\":18.14570833333333,\"ZMK\":5252.550000000104,\"ZMW\":20.001666333333333,\"JEP\":0.8305844448975744,\"GGP\":0.8305842686898819,\"IMP\":0.8305842686898819,\"CNH\":6.903722902387548,\"EEK\":14.713800000000008,\"LTL\":3.24694,\"LVL\":0.660902,\"VEF\":2428640.0000769002,\"SGD\":1.3445833333333332,\"AUD\":1.4787033333333333,\"USD\":1,\"BTC\":4.45479838585e-5,\"BCH\":0.0080025608194622,\"ETH\":0.0006371983662234,\"LTC\":0.0110625587698435,\"LINK\":0.1438848920863309,\"XRP\":2.6962925222856664,\"XLM\":12.012444892909054,\"UNI\":0.1580028440511929,\"ADA\":2.9647198339756895,\"MTL\":0.8605851979345955,\"ERN\":0.5012531328320802,\"XRH\":9.8039215686275e-5,\"RUTH\":0.0013333333333333,\"XCU\":3.9321700663554,\"ALU\":13.409769016729,\"NI\":4.0983606557377e-5,\"ZNC\":0.00032372936225316,\"TIN\":3.5714793026687e-5,\"LCO\":0.065789473684211,\"IRD\":0.00021739130434783,\"BRENTOIL\":0.011650937900501,\"WTIOIL\":0.012550200803213,\"WHEAT\":0.0034439673401689,\"COFFEE\":0.56236643797098,\"COTTON\":1.1904761904762,\"SUGAR\":4.7824007651841,\"CORN\":0.15497869043007,\"SOYBEAN\":0.065316786414108,\"RICE\":0.058038305281486,\"CPO\":0.0046919381733021,\"NG\":0.33233632436025,\"ETHANOL\":0.462748727441,\"RUBBER\":0.60975609756098,\"LUMBER\":0.002710027100271,\"ROBUSTA\":0.00046296296296296,\"COCOA\":0.00036258158085569,\"ZWH23\":0.0014352350197345,\"OAT\":0.0030211480362538,\"COAL\":0.005343307507347,\"LHOG\":1.1827321111768,\"LCAT\":0.60448528078341,\"CANO\":0.0011806375442739,\"KCH23\":0.0055432372505543,\"KCK23\":0.0056116722783389,\"KCN23\":0.0056242969628796,\"KCU23\":0.0056802044873615,\"KCZ23\":0.0057454754380925,\"RMN23\":0.0004644681839294,\"RMU23\":0.00046948356807512,\"RMX23\":0.00047778308647874,\"RMH23\":0.00046382189239332,\"RMK23\":0.00046189376443418,\"SBK23\":0.047801147227533,\"EUR\":0.94060015933767,\"GFU22\":0.0052623270009998,\"OJU22\":0.0043936731107206,\"ZRU22\":0.057464659234571,\"HOU22\":0.34428148454176,\"RBU22\":0.36283153731722,\"LGOU22\":0.0011638056444574,\"XRX22\":0.0016485306557716,\"ZLH23\":0.016501650165017,\"ZMH23\":0.0020060180541625,\"TGJ23\":0.022185246810871,\"ZSH23\":0.00065316786414108,\"UK-EL\":0.0066090342431235,\"UK-NG\":0.0073306031185643,\"POTATOES\":0.03265972775478},\"unit\":\"per barrel for Oil, per ounce for Metals. Per 10 metric tons for Crude Palm Oil, Per MMBtu for Natural gas, Per Gallon for Ethanol. Per metric ton, per lb or per bushel for Agriculture\"}}";
	
	@GetMapping("/commodities")
	public String getCommmodities()  {
		
		//ResponseEntity<String> cEntity= restTemplate.getForEntity(URI.create("https://commodities-api.com/api/latest?access_key=8zyhd3moqqxb05f4pa8a8n2j20p9hrlqig99r4xxwjz53ejrhfndw4frho8y"),String.class);
		
		//System.out.println(cEntity.getBody());
		kafkaProducer.sendMessage("world_commodities_rates", response);
		return response;
	}

	@PostMapping("/api/promotion")
	public ResponseEntity<String> promotionCodes(@RequestBody String promotionCodeJson) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode =objectMapper.readTree(promotionCodeJson);
		String promotionCode = String.valueOf(jsonNode.get("promotionCode"));
		//ResponseEntity<String> cEntity= restTemplate.getForEntity(URI.create("https://commodities-api.com/api/latest?access_key=8zyhd3moqqxb05f4pa8a8n2j20p9hrlqig99r4xxwjz53ejrhfndw4frho8y"),String.class);

		//System.out.println(cEntity.getBody());
		kafkaProducer.sendMessage("t-commodity-promotion", promotionCode);
		return ResponseEntity.ok("OK");
	}
	
	

}
