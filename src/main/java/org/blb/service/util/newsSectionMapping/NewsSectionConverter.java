package org.blb.service.util.newsSectionMapping;

import lombok.AllArgsConstructor;
import org.blb.DTO.news.NewsSectionDTO;
import org.blb.models.news.NewsSection;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NewsSectionConverter {
    public NewsSectionDTO toDTO(NewsSection newsSection) {
        NewsSectionDTO DTO = new NewsSectionDTO();
        DTO.setId(newsSection.getId());
        DTO.setSectionName(newsSection.getSectionName());
        return DTO;
    }
}
