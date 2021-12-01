package myproject.eumfruit.Controller;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.Base64.*;

@Controller
@RequiredArgsConstructor
public class BandController {

    private String clientId = "348253770"; //애플리케이션 클라이언트 아이디값"
    private String clientSecret = "Ro04pOyD55qsMKobqXcp3H9EnoUd3Xo3"; // 시크릿키
    private String accessToken = "ZQAAAfKjqDWdF7BIiFJtCzPJG7BiYz5wYTMPZ3L-wMC5-IOF3A3nopj-yib4nfG-9D542e_gNfoMYgmrksKZIwoZz8tbxRkNakG4pkeWokQqjmUh";
    private String redirectURI = "http://www.eumfruit.shop";
    private String authorizeURL = "https://auth.band.us/oauth2/authorize?response_type=code&client_id=348253770&redirect_uri=http://www.eumfruit.shop";

    private String bandKey = "AAD-5MuetkQTdnC-XIW7XxEt"; // 이음청과 밴드키
    @GetMapping(value="/band")
    public String band(Model model) {

        String encodeText = clientId+":"+clientSecret;  // :으로 묶어 인코딩
        String text = null;

        //System.out.println("인코딩 전 텍스트 : "+encodeText);
        Encoder encoder = Base64.getEncoder();
        byte[] targetBytes = encodeText.getBytes();
        byte[] encodeBytes = encoder.encode(targetBytes);
        text = encodeBytes.toString();
        //System.out.println("인코딩 텍스트 : "+text);

        //String apiURL = "https://openapi.band.us/v2.1/band/posts?band_key="+bandKey+"&access_token="+accessToken+"&ko_KR";    // json 결과
        String apiURL = "https://openapi.band.us/v2/band/posts?band_key=AAD-5MuetkQTdnC-XIW7XxEt&access_token=ZQAAAfKjqDWdF7BIiFJtCzPJG7BiYz5wYTMPZ3L-wMC5-IOF3A3nopj-yib4nfG-9D542e_gNfoMYgmrksKZIwoZz8tbxRkNakG4pkeWokQqjmUh&ko_KR";
        //String apiURL2 = "https://openapi.band.us/v2/band/post?band_key=AAD-5MuetkQTdnC-XIW7XxEt&access_token=ZQAAAfKjqDWdF7BIiFJtCzPJG7BiYz5wYTMPZ3L-wMC5-IOF3A3nopj-yib4nfG-9D542e_gNfoMYgmrksKZIwoZz8tbxRkNakG4pkeWokQqjmUh&ko_KR&post_key=AAC9jVtc97KIC0XnsLk3Nidd";

        Map<String, String> requestHeaders = new HashMap<>();
        String responseBody = get(apiURL,requestHeaders);

        List<String> data = new ArrayList<>();
        data = parsing(responseBody);

        String content = data.get(0);
        data.remove(0);
        content = content.replace("\n","<br>"); // html에서 \n을 적용하기 위해 변환
        //System.out.println(content);
        model.addAttribute("content", content);
        model.addAttribute("data", data);

        return "/blog/band";
    }

    public List<String> parsing(String responseBody) {
        JSONObject obj;
        JSONParser parser = new JSONParser();
        List<String> results = new ArrayList<>();
        try {
            obj = (JSONObject) parser.parse(responseBody);  // 요청
            JSONObject result_data = (JSONObject)obj.get("result_data");
            JSONArray items = (JSONArray)result_data.get("items");

            obj = (JSONObject) parser.parse(items.get(0).toString());   // 최근 첫번째글 가져오기

            String content = (String)obj.get("content");
            results.add(content);
            //System.out.println(content);
            JSONArray imgItems = (JSONArray)obj.get("photos");
            for(int i = 0; i<items.size(); i++) {
                // 이미지 url파싱 후 리스트에 넣기
                JSONObject imgItem = (JSONObject) parser.parse(imgItems.get(i).toString());
                String strImg = (String)imgItem.get("url");
                results.add(strImg);
               // System.out.println(strImg);
            }

        } catch (Exception e) {
            System.out.println("실패!");
        }
        return results;
    }
    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}
