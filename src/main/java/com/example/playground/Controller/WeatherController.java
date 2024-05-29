package com.example.playground.Controller;

import com.example.playground.Model.vo.Weather_vo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    WeatherController(){

    }

    @GetMapping("/getweather")
    public String weather(Model model) throws IOException {
        LocalDateTime t = LocalDateTime.now().minusMinutes(30); // 현재 시각 30분전

        String apiurl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";
        String serviceKey = "6S4u3U%2FUibanctkZBHz%2BeDGDzroxvL1RTCNvDovGn7J7JZSG4LoJCWXiTJl4x1mkElXI%2B1OQZ%2FS%2BKb6B5wldaQ%3D%3D";
        String pageNo = "1";
        String numOfRows = "1000";
        String dataType = "JSON";
        String base_date = t.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String base_time = t.format(DateTimeFormatter.ofPattern("HHmm"));
        String nx = "99";
        String ny = "75";
        String basehour = base_time.substring(0,2);System.out.println("+++++basehour: "+ basehour);

        StringBuilder urlBuilder = new StringBuilder(apiurl); /*단기예보조회 URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(base_date, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(base_time, "UTF-8")); /*02시 발표(정시단위) */
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /*예보지점의 Y 좌표값*/

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        String temp = sb.toString();
        JSONParser jp = new JSONParser();
        JSONObject jo = null;
        try{
            jo = (JSONObject) jp.parse(temp);
        }catch(ParseException e){
            e.printStackTrace();
        }
        System.out.println(jo);
        JSONObject response = (JSONObject) jo.get("response");
        JSONObject body = (JSONObject)response.get("body");
        JSONObject items = (JSONObject)body.get("items");
        JSONArray item = (JSONArray)items.get("item");

        Weather_vo PTY_vo = new Weather_vo();
        Weather_vo T1H_vo = new Weather_vo();
        Weather_vo SKY_vo = new Weather_vo();
        Weather_vo REH_vo = new Weather_vo();

        for(int i=0;i<item.size();i=i+6)
        {
            JSONObject temp2 = (JSONObject) item.get(i);
            if( temp2.get("category").equals("PTY") )
            {
                PTY_vo.setCategory("PTY");
                PTY_vo.setObsrValue(temp2.get("fcstValue")+"");
            }
            if( temp2.get("category").equals("T1H"))
            {
                T1H_vo.setCategory("T1H");
                T1H_vo.setObsrValue(temp2.get("fcstValue")+"");
            }
            if( temp2.get("category").equals("SKY"))
            {
                SKY_vo.setCategory("SKY");
                SKY_vo.setObsrValue(temp2.get("fcstValue")+"");
            }
            if( temp2.get("category").equals("REH") )
            {
                REH_vo.setCategory("REH");
                REH_vo.setObsrValue(temp2.get("fcstValue")+"");
            }
        }

        System.out.println("하늘상태는 : "+ SKY_vo.getObsrValue());
        model.addAttribute("PTY",PTY_vo);
        model.addAttribute("T1H",T1H_vo);
        model.addAttribute("SKY",SKY_vo);
        model.addAttribute("REH",REH_vo);

        rd.close();
        conn.disconnect();
        String result= sb.toString();
        model.addAttribute("weather_dataset", result);
        return "weather/weather";
    }
}