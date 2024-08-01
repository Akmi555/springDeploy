package org.blb.service.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.blb.DTO.news.newsJsonModel.FetchNewsDataDTO;
import org.blb.exeption.RestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class FetchNewsApi {

    private final RestTemplate restTemplate;

    public Map<String, FetchNewsDataDTO> fetchDataFromApi() {
        Map<String, FetchNewsDataDTO> allNews = new HashMap<>();

        allNews.putAll(fetchDataFromUrl("https://www.tagesschau.de/api2u/news", "general"));
        allNews.putAll(fetchDataFromUrl("https://www.tagesschau.de/api2u/news?ressort=sport", "sport"));
        allNews.putAll(fetchDataFromUrl("https://www.tagesschau.de/api2u/news?ressort=wirtschaft", "wirtschaft"));
        allNews.putAll(fetchDataFromUrl("https://www.tagesschau.de/api2u/news?ressort=wissen", "wissen"));

        if (allNews.isEmpty()) {
            throw new RestException(HttpStatus.NO_CONTENT, "Fetch response contains no data");
        }

        return allNews;
    }

    private Map<String, FetchNewsDataDTO> fetchDataFromUrl(String url, String apiType) {
        String json1Response = restTemplate.getForObject(url, String.class);
        if (json1Response == null) {
            throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching data from URL: " + url);
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, FetchNewsDataDTO> savedNews = new HashMap<>();

        JsonNode jsonResponse;
        try {
            jsonResponse = mapper.readTree(json1Response);
        } catch (IOException e) {
            throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing JSON response: " + e.getMessage());
        }

        JsonNode newsArray = jsonResponse.path("news");
        for (JsonNode item : newsArray) {

            boolean isLiveblog = false;
            JsonNode trackingArray = item.path("tracking");
            for (JsonNode trackingItem : trackingArray) {
                if ("LIVEBLOG".equals(trackingItem.path("ctp").asText())) {
                    isLiveblog = true;
                    break;
                }
            }

            Integer regionId = item.path("regionId").asInt();
            String sectionName = item.path("ressort").asText();
            JsonNode teaserImage = item.path("teaserImage");
            JsonNode imageVariants = teaserImage.path("imageVariants");
            String imageSquareUrl = imageVariants.path("1x1-840").asText();
            String imageWideUrl = imageVariants.path("16x9-960").asText();
            String detailsUrl = item.path("details").asText();

            // Check if detailsUrl is valid
            if (detailsUrl == null || detailsUrl.isEmpty()) {
                continue;
            }
            // Fetch and check content
            String content = fetchContentFromDetailsUrl(detailsUrl);

            if ("story".equals(item.path("type").asText())
                    && !isLiveblog
                    && !(sectionName.equals("investigativ"))
                    && !teaserImage.isMissingNode()
                    && imageVariants.has("1x1-840")
                    && !imageSquareUrl.isEmpty()
                    && imageVariants.has("16x9-960")
                    && !imageWideUrl.isEmpty()
                    && content != null && !content.isEmpty()
                    && (!(apiType.equals("general") && regionId == 0 && sectionName.isEmpty()))
            ) {
                FetchNewsDataDTO newsData = new FetchNewsDataDTO();
                // RegionId
                newsData.setRegionId((long) (regionId + 1));

                // Section
                if (regionId > 0) {
                    newsData.setSectionName("inland");
                } else {
                    if (apiType.equals("sport") && sectionName.isEmpty()) {
                        sectionName = "sport";
                    } else if (apiType.equals("wirtschaft") && sectionName.isEmpty()) {
                        sectionName = "wirtschaft";
                    } else if (apiType.equals("wissen") && sectionName.isEmpty()) {
                        sectionName = "wissen";
                    }
                    newsData.setSectionName(sectionName);
                }

                newsData.setTitle(item.path("title").asText());
                newsData.setDate(item.path("date").asText());
                newsData.setTitleImageSquare(imageSquareUrl);
                newsData.setTitleImageWide(imageWideUrl);
                newsData.setContent(content);

                // Add newsData to the map if the title is not already present
                savedNews.putIfAbsent(newsData.getTitle(), newsData);
            }
        }

        return savedNews;
    }

    private String fetchContentFromDetailsUrl(String detailsUrl) {
        String json2Response = restTemplate.getForObject(detailsUrl, String.class);
        if (json2Response == null) {
            throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching data from details URL: " + detailsUrl);
        }

        ObjectMapper mapper = new ObjectMapper();
        StringBuilder contentBuilder = new StringBuilder();

        try {
            JsonNode jsonResponse = mapper.readTree(json2Response);
            JsonNode contentArray = jsonResponse.path("content");

            for (JsonNode contentItem : contentArray) {
                if (contentItem.has("value")) {
                    String value = contentItem.path("value").asText();
                    // Perform replacements
                    value = value.replaceAll("/api2u", "");
                    value = value.replaceAll("\\.json", ".html");
                    value = value.replaceAll("type=\"intern\"", "type=\"extern\"");
                    contentBuilder.append("<div className=\"textValueNews\">").append(value).append("</div>").append(" ");
                }
                if (contentItem.has("quotation")) {
                    String quotationText = contentItem.path("quotation").path("text").asText();
                    // Perform replacements
                    quotationText = quotationText.replaceAll("/api2u", "");
                    quotationText = quotationText.replaceAll("\\.json", ".html");
                    quotationText = quotationText.replaceAll("type=\"intern\"", "type=\"extern\"");
                    contentBuilder.append("<div className=\"quotationNews\">").append(quotationText).append("</div>").append(" ");
                }
            }
        } catch (IOException e) {
            throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing details URL JSON: " + e.getMessage());
        }
        return contentBuilder.toString().trim();
    }
}