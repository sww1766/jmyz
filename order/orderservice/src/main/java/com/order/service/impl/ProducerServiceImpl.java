package com.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.order.mapper.ProductMapper;
import com.order.model.Product;
import com.order.service.ProducerService;
import com.order.utils.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author admin
 */
@Slf4j
@Service
public class ProducerServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProducerService {

    @Value("${demo.rocketmq.topic}")
    private String springTopic;

    @Resource
    private RocketMQTemplate rocketMqTemplate;

    @Override
    public ResponseData<String> producer(String string) {
        SendResult sendResult = rocketMqTemplate.syncSend(springTopic, string);
        log.info("syncSend to topic {} sendResult={}", springTopic, sendResult);
        return new ResponseData<>(string);
    }
}
