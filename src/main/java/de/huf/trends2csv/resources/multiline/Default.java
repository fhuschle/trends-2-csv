package de.huf.trends2csv.resources.multiline;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Default {

    public List<TimelineData> timelineData = new ArrayList<>();
}
