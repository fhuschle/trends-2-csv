package de.huf.trends2csv.resources.explore.widget;

import lombok.Data;

@Data
public class Widget {

    private String lineAnnotationText;
    private String token;
    private String id;
    private String type;
    private String title;
    private String template;
    private String embedTemplate;
    private String version;
    private Request request;
    private boolean isLong;
    private boolean isCurated;

    public enum WidgetType {
        TIMESERIES
    }
}
