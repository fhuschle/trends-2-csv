package de.huf.trends2csv.resources.multiline;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimelineData {

    private String time;
    private String formattedTime;
    private String formattedAxisTime;
    private int[] value;

}
