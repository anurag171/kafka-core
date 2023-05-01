package com.focuslearning.example.kafka.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.focuslearning.example.kafka.PaymentData;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PaymentDataRepositoryImpl implements PaymentDataRepository{

    private final JdbcTemplate jdbcTemplate;
    final ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    private void init(){
        mapper.registerModule(new JavaTimeModule());
    }


    public PaymentDataRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public int save(PaymentData paymentData) {

        try {
            return jdbcTemplate.update("INSERT INTO replay_message (paymentid,message,createdTime,expireTime,replay,status,country)" +
                    " VALUES (?,?,?,?,?,?,?) ",new Object[]{paymentData.getPaymentid(),mapper.writeValueAsString(paymentData), paymentData.getCreatedtime(),
                    paymentData.getExpiretime(),paymentData.getRetryTimes()+1,RecordStatus.NEW.getAction(),paymentData.getCountry()});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int update(PaymentData paymentData) {

        return jdbcTemplate.update(
                "update replay_message set STATUS = ?,replay=replay+1 where paymentid = ? and STATUS in ( ?,?)",
                RecordStatus.REPLAY.getAction(),paymentData.getPaymentid(),RecordStatus.REPLAY.getAction(),RecordStatus.NEW.getAction());
    }

    @Override
    public Optional<PaymentData> findById(String id) {
        return  jdbcTemplate.query("SELECT message from replay_message WHERE paymentid=?",
                BeanPropertyRowMapper.newInstance(PaymentData.class), id).stream().findFirst();

    }

    @Override
    public int deleteById(String id) {
        return 0;
    }

    @Override
    public List<PaymentData> findAll() {
        return  jdbcTemplate.query("SELECT * from replay_message where status in ('R','N')",
                BeanPropertyRowMapper.newInstance(PaymentData.class));
    }

    @Override
    public List<Object[]> findAllByLimit(int n) {
        List<PaymentData> list = new ArrayList<>();
        return  jdbcTemplate.query("SELECT message,paymentid as paymentid from replay_message WHERE status=?",
                BeanPropertyRowMapper.newInstance(PaymentData.class), "N").stream().map(paymentData -> {
                    Object[] object = new Object[2];
                    object[0]=paymentData.getPaymentid();
                    object[1]=paymentData.getMessage();
                    return object;
        }).collect(Collectors.toList());

    }

    @Override
    public List<PaymentData> findByPublished(boolean published) {
        return null;
    }


    public List<PaymentData> findByTitleContaining(String title) {
        return null;
    }

    @Override
    public int deleteAll() {
        return 0;
    }

    @Override
    public int updateById(String whereClause) {
        return jdbcTemplate.update("UPDATE replay_message SET status='E' where paymentid in " + whereClause);
    }
}
