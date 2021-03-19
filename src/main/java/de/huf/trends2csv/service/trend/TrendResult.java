package de.huf.trends2csv.service.trend;

import de.huf.trends2csv.resources.enums.CountryRegion;
import de.huf.trends2csv.resources.multiline.TimelineData;
import lombok.Data;

import java.util.List;

@Data
public class TrendResult {

    private List<TimelineData> data;
    private String resolution;
    private final String keyword;
    private final CountryRegion countryRegion;
}
