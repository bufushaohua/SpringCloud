package com.cyc.springcloud.controller;

import com.cyc.springcloud.entities.CommonResult;
import com.cyc.springcloud.entities.Payment;
import com.cyc.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
public class PaymentController {

    @Resource
    public PaymentService paymentService;

    //浏览器对post请求不支持，可以使用postman软件进行测试
    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        log.info("****插入结果：" + result);

        if(result>0){
            return new CommonResult(200,"插入数据库成功",result);
        }else{
            return new CommonResult(444,"插入数据库失败",null);
        }
    }

    @GetMapping(value = "/payment/query/{id}")
    public CommonResult getPaymentById(@PathVariable("id") long id){
        Payment payment = paymentService.getPaymentById(id);
        log.info("*****查询结果" + payment);

        if(payment != null){
            return new CommonResult(200,"查询数据成功", payment);
        }else
        {
            return new CommonResult(444,"没有对应记录，查询ID" + id,null);
        }

    }

}
