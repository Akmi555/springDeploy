package org.blb.service.news;

import lombok.AllArgsConstructor;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.news.newsJsonModel.FetchNewsDataDTO;
import org.blb.exeption.RestException;
import org.blb.models.news.NewsDataEntity;
import org.blb.repository.news.NewsDataRepository;
import org.blb.service.util.newsMapping.NewsDataConverter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AddNewsDataService {
    private final FetchNewsApi fetchNewsApi;
    private final NewsDataRepository newsDataRepository;
    private final NewsDataConverter newsDataConverter;

    @Transactional
    public StandardResponseDto saveNewsFromFetchApi() {
        Map<String, FetchNewsDataDTO> newsFromFetch = fetchNewsApi.fetchDataFromApi();
        Optional<String> lastDateOpt = newsDataRepository.findLastDate();

        String lastDate = lastDateOpt.orElse("0000-00-00T00:00:00");

        for (FetchNewsDataDTO fetchNewsDataDTO : newsFromFetch.values()) {
            String newsDate = fetchNewsDataDTO.getDate();

            if (newsDate.compareTo(lastDate) > 0) {
                NewsDataEntity newsDataEntity = newsDataConverter.fromFetchApiToEntity(fetchNewsDataDTO);
                newsDataRepository.save(newsDataEntity);
            }
        }
        return new StandardResponseDto("All news loaded successfully");
    }
}
