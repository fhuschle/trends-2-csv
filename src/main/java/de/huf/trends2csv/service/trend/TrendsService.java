package de.huf.trends2csv.service.trend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.huf.trends2csv.resources.enums.CountryRegion;
import de.huf.trends2csv.resources.enums.Region;
import de.huf.trends2csv.resources.explore.Explore;
import de.huf.trends2csv.resources.explore.widget.Widget;
import de.huf.trends2csv.resources.multiline.Multiline;
import de.huf.trends2csv.resources.multiline.TimelineData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TrendsService {

    private static final String COOKIE = "Cookie";
    private static final String TRENDS_URL = "https://trends.google.de/trends/";
    private final Gson gson;
    private final RestTemplate restTemplate;

    public TrendsService(final RestTemplateBuilder restTemplateBuilder) {
        gson = new GsonBuilder().create();
        restTemplate = restTemplateBuilder.build();
    }

    public TrendResult getTrendsData(final String keyword,
                                     final CountryRegion countryRegion,
                                     final LocalDate startDate,
                                     final LocalDate endDate) {
        final TrendResult trendResult = new TrendResult(keyword, countryRegion);
        final List<TimelineData> timelineData = new ArrayList<>();
        trendResult.setData(timelineData);

        final HttpHeaders responseHeaders = restTemplate.headForHeaders(TRENDS_URL);
        final List<String> cookies = responseHeaders.get(HttpHeaders.SET_COOKIE);
        for (final String cookie : cookies) {
            responseHeaders.add(COOKIE, cookie);
        }

        final HttpEntity<String> httpEntity = new HttpEntity<>(responseHeaders);
        final ResponseEntity<String> exchange = performExplorerRequest(keyword, countryRegion, startDate, endDate, httpEntity);
        final Explore explore = gson.fromJson(exchange.getBody().replace(")]}'\n", ""), Explore.class);
        final Optional<Widget> first = explore.getWidgets().parallelStream().filter(widget -> widget.getId().equals(Widget.WidgetType.TIMESERIES.toString())).findFirst();

        if (first.isPresent()) {
            final Widget widget = first.get();
            final String resolution = widget.getRequest().getResolution();
            trendResult.setResolution(resolution);
            final ResponseEntity<String> jsonexchange = performMultilineRequest(keyword, countryRegion, startDate, endDate, httpEntity, resolution, widget.getToken());
            final Multiline multiline = gson.fromJson(jsonexchange.getBody().replace(")]}',\n", ""), Multiline.class);
            timelineData.addAll(multiline.getTimelineData());
        } else {
            log.error("cant read timeseries widget for token");
        }
        return trendResult;
    }

    private ResponseEntity<String> performExplorerRequest(final String keyword,
                                                          final CountryRegion countryRegion,
                                                          final LocalDate startDate,
                                                          final LocalDate endDate,
                                                          final HttpEntity<String> httpEntity) {

        final String url = String.format("%sapi/explore?hl=de&tz=-60&req={req}&tz=-60", TRENDS_URL);
        final String req = String.format("{\"comparisonItem\":[{\"keyword\":\"%s\",\"geo\":\"%s\",\"time\":\"%s %s\"}],\"category\":0,\"property\":\"\"}", keyword,
                                         countryRegion.getCode(), startDate, endDate);

        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, req);
    }

    private ResponseEntity<String> performMultilineRequest(final String keyword,
                                                           final CountryRegion countryRegion,
                                                           final LocalDate startDate,
                                                           final LocalDate endDate,
                                                           final HttpEntity<String> httpEntity,
                                                           final String resolution,
                                                           final String token) {
        final String jsonGeo;
        if (countryRegion instanceof Region) {
            jsonGeo = String.format("{\"region\":\"%s\"}", countryRegion.getCode());
        } else {
            jsonGeo = String.format("{\"country\":\"%s\"}", countryRegion.getCode());
        }

        final String jsonReq = String.format("{\"time\":\"%s %s\",\"resolution\":\"%s\",\"locale\":\"de\",\"comparisonItem\":"
                                                     + "[{\"geo\":%s,\"complexKeywordsRestriction\":{\"keyword\":[{\"type\":\"BROAD\",\"value\":\"%s\"}]}}],"
                                                     + "\"requestOptions\":{\"property\":\"\",\"backend\":\"IZG\",\"category\":0}}", startDate, endDate, resolution, jsonGeo,
                                             keyword);
        final String jsonUrl = String.format("%sapi/widgetdata/multiline/json?req={req}&token=%s&tz=-60", TRENDS_URL, token);
        return restTemplate.exchange(jsonUrl, HttpMethod.GET, httpEntity, String.class, jsonReq);
    }

}
