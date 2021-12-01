package myproject.eumfruit.service;

import lombok.RequiredArgsConstructor;
import myproject.eumfruit.constant.ItemKind;
import myproject.eumfruit.dto.BlogDto;
import myproject.eumfruit.dto.CafeDto;
import myproject.eumfruit.dto.ProductItemDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
    public List<BlogDto> getBlogList(String responseBody){

        List<BlogDto> results = new ArrayList<>();

        JSONObject obj;
        JSONParser parser = new JSONParser();

        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

        try {
            obj = (JSONObject) parser.parse(responseBody);
            System.out.println(obj);
            JSONArray objItems = (JSONArray) obj.get("items");

            for(int i = 0; i < objItems.size(); i++) {
                JSONObject objItem = (JSONObject) parser.parse(objItems.get(i).toString());
                String strDate = (String)objItem.get("postdate");
                Date formatDate = dtFormat.parse(strDate);
                String strNewDtFormat = simpleDateFormat.format(formatDate);

                String title = (String)objItem.get("title");
                title = title.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");   // 문자열 안에 태그없애기

                String description = (String)objItem.get("description");
                description = description.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");

                BlogDto blogDto = new BlogDto(strNewDtFormat,
                        title,
                        description,
                        (String)objItem.get("bloggername"),
                        (String)objItem.get("diplay"),
                        (String)objItem.get("link"),
                        (String)objItem.get("bloggerlink")
                );
                results.add(blogDto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public List<CafeDto> getCafeList(String responseBody){

        List<CafeDto> results = new ArrayList<>();

        JSONObject obj;
        JSONParser parser = new JSONParser();

        try {
            obj = (JSONObject) parser.parse(responseBody);
            JSONArray objItems = (JSONArray) obj.get("items");

            for(int i = 0; i < objItems.size(); i++) {
                JSONObject objItem = (JSONObject) parser.parse(objItems.get(i).toString());

                String title = (String)objItem.get("title");
                title = title.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");   // 문자열 안에 태그없애기

                String description = (String)objItem.get("description");
                description = description.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");

                CafeDto cafeDto = new CafeDto(
                        title,
                        description,
                        (String)objItem.get("cafename"),
                        (String)objItem.get("diplay"),
                        (String)objItem.get("link"),
                        (String)objItem.get("cafeurl")
                );
                results.add(cafeDto);
            }
//            for(int i =0; i< results.size(); i++) {
//                System.out.println(results.get(i).getCafeNm());
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}
