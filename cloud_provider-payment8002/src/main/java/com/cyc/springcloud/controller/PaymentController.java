package com.cyc.springcloud.controller;

import com.cyc.springcloud.entities.CommonResult;
import com.cyc.springcloud.entities.Payment;
import com.cyc.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @Resource
    public PaymentService paymentService;

    @Resource
    private DiscoveryClient discoveryClient;

    //浏览器对post请求不支持，可以使用postman软件进行测试
    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        log.info("****插入结果：" + result);

        if(result>0){
            return new CommonResult(200,"插入数据库成功,serverPort:"+serverPort,result);
        }else{
            return new CommonResult(444,"插入数据库失败",null);
        }
    }

    @GetMapping(value = "/payment/query/{id}")
    public CommonResult getPaymentById(@PathVariable("id") long id){
        Payment payment = paymentService.getPaymentById(id);
        log.info("*****查询结果" + payment);

        if(payment != null){
            return new CommonResult(200,"查询数据成功,serverPort:"+serverPort, payment);
        }else
        {
            return new CommonResult(444,"没有对应记录，查询ID" + id,null);
        }

    }

    @GetMapping(value = "/payment/discovery")
    public Object discovery(){
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            log.info("******Services:" + service);
        }
        //服务名不区分大小写，但不能写下划线
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-service");
        for (ServiceInstance instance : instances) {
            log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
        }
        return this.discoveryClient;
    }

}
