package org.blb.service.mainPage;

import lombok.RequiredArgsConstructor;
import org.blb.DTO.blog.BlogsRequestDTO;
import org.blb.DTO.blog.blogs.BlogResponseDTO;
import org.blb.DTO.blog.blogs.BlogsResponseDTO;
import org.blb.DTO.mainPageDto.MpNewsResponse;
import org.blb.DTO.mainPageDto.MpRentResponse;
import org.blb.DTO.mainPageDto.MpResponseDTO;
import org.blb.DTO.mainPageDto.MpWeatherDTO;
import org.blb.DTO.weather.WeatherLatLonDTO;
import org.blb.models.news.NewsDataEntity;
import org.blb.repository.news.NewsDataRepository;
import org.blb.repository.rent.ProductRepository;
import org.blb.service.blog.BlogFindService;
import org.blb.service.weather.OutGeoLocationApi;
import org.blb.service.weather.OutWeatherApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {
    private final NewsDataRepository newsDataRepository;
    private final BlogFindService blogFindService;
    private final ProductRepository productRepository;
    private final OutWeatherApi outWeatherApi;
    private static Logger log = LoggerFactory.getLogger(MainPageService.class);
    public MpResponseDTO getData() {
        List<NewsDataEntity> news =
                newsDataRepository
                        .findAll(PageRequest.of(0, 200,
                                Sort.by(Sort.Order.asc("id")))).getContent();
        List<MpNewsResponse> innerNews = news.stream()
                .filter(item -> (item.getSectionName().equals("inland"))
                        && (item.getRegion().getId() > 1 && item.getRegion().getId() < 18) )
                .limit(15)
                .map(item -> new MpNewsResponse(item.getId(), item.getRegion().getRegionName(),
                        item.getSectionName(), item.getTitle(), item.getDate().substring(0, 10), item.getTitleImageSquare(),
                        item.getLikeCount(), item.getDislikeCount(), item.getCommentsCount())).toList();
        List<MpNewsResponse> sport = news.stream()
                .filter(item -> item.getSectionName().equals("sport")).limit(6)
                .map(item -> new MpNewsResponse(item.getId(), item.getRegion().getRegionName(),
                        item.getSectionName(), item.getTitle(), item.getDate().substring(0, 10), item.getTitleImageSquare(),
                        item.getLikeCount(), item.getDislikeCount(), item.getCommentsCount())).toList();
        List<MpNewsResponse> world = news.stream()
                .filter(item -> item.getSectionName().equals("ausland")).limit(4)
                .map(item -> new MpNewsResponse(item.getId(), item.getRegion().getRegionName(),
                        item.getSectionName(), item.getTitle(), item.getDate().substring(0, 10), item.getTitleImageSquare(),
                        item.getLikeCount(), item.getDislikeCount(), item.getCommentsCount())).toList();
        List<BlogResponseDTO> blogs = blogFindService.findAll(new BlogsRequestDTO(0, (long) 0), 3).getBlogs();

        List<MpRentResponse> rent = productRepository
                .findAll(PageRequest.of(0, 6,
                        Sort.by(Sort.Order.desc("id")))).getContent().stream()
                .map(item -> new MpRentResponse(item.getId(), item.getName(), item.getPrice(), item.getRegion().getRegionName()))
                .toList();
        return new MpResponseDTO(innerNews, sport, world, blogs, rent);
    }

    public MpWeatherDTO getWeather(WeatherLatLonDTO position) {
       log.info("GEO position - "+ position.getCity());
        return outWeatherApi.receivedFromWeatherMapApi(position);
//        return new MpWeatherDTO("Berlin", "16", "18", "15", "Überwiegend bewölkt",
//                "6.71", "93", "10d");
    }
}