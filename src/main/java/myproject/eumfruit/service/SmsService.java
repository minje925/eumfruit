package myproject.eumfruit.service;

import myproject.eumfruit.entity.Order;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Service
public class SmsService {

    public void sendSms(Order order) {
        String api_key = "NCSE5VLMN8FCBWUQ";
        String api_secret = "OTCKR851NVAQHRPP9S7XDOHV1KLHRBEP";

        Message coolsms = new Message(api_key, api_secret);
        HashMap<String, String> params = new HashMap<String, String>();

        String orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String content = "온라인 주문 알림!\n"+"주문시각 : "+orderDate+
                "\n성명 : "+order.getMember().getName()+
                "\n상품 : "+order.getOrderItems().get(0).getItem().getItemNm()+
                "\n수량 : "+order.getOrderItems().get(0).getCount()+"개"+
                "\n금액 : "+order.getOrderItems().get(0).getTotalPrice()+"원";
        //System.out.println("문자 : "+content);

        params.put("to", "01084562521");
        params.put("from", "01084562521");
        params.put("type", "LMS");  // SMS, LMS
        params.put("text", content);
        params.put("app_version", "test app 1.2");

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }
}
