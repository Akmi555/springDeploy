package org.blb.service.news;

import lombok.AllArgsConstructor;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.exeption.RestException;
import org.blb.repository.news.NewsDataRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteNewsDataService {
    private final NewsDataRepository newsDataRepository;

    public StandardResponseDto deleteNewsDataById(Long newsId) {
        if (newsId == null || newsId <= 0) {
            throw new RestException(HttpStatus.BAD_REQUEST, "Invalid ID: " + newsId);
        }

        if (newsDataRepository.existsById(newsId)) {
            newsDataRepository.deleteById(newsId);
            return new StandardResponseDto("News with ID = " + newsId + " deleted successfully");
        } else {
            throw new RestException(HttpStatus.NOT_FOUND, "News with ID = " + newsId + " not found");
        }
    }
}
