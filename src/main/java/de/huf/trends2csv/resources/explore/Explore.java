package de.huf.trends2csv.resources.explore;

import de.huf.trends2csv.resources.explore.widget.Widget;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Explore {

    private List<Widget> widgets = new ArrayList<>();

}
