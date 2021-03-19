package de.huf.trends2csv.resources.multiline;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class Multiline {

    @SerializedName(value = "default")
    private Default aDefault;

    public List<TimelineData> getTimelineData() {
        return aDefault.getTimelineData();
    }
}
