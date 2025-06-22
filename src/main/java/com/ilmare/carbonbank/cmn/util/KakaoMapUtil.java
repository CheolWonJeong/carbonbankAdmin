package com.ilmare.carbonbank.cmn.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KakaoMapUtil {

    private static final String KAKAO_API_KEY = "7684b69926ddc0db177cfbb36c774a88";

    public static void getCoordinatesFromAddress(String address) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + java.net.URLEncoder.encode(address, "UTF-8");
            HttpGet request = new HttpGet(url);
            request.addHeader("Authorization", "KakaoAK " + KAKAO_API_KEY);

            try (CloseableHttpResponse response = client.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(json);

                JsonNode documents = root.path("documents");
                if (documents.isArray() && documents.size() > 0) {
                    JsonNode location = documents.get(0);
                    String lat = location.path("y").asText();
                    String lng = location.path("x").asText();

                    System.out.println("위도: " + lat);
                    System.out.println("경도: " + lng);
                } else {
                    System.out.println("주소 검색 결과가 없습니다.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 테스트용 main
    public static void main(String[] args) {
        getCoordinatesFromAddress("경기도 부천시 원미구 송내대로74번길 22");
    }
}
