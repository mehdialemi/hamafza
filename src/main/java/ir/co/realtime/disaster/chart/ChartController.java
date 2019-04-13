package ir.co.realtime.disaster.chart;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController("/api/chart")
public class ChartController {

    @GetMapping("cat/1")
    public List<Chart> cat1() {
        Chart<Integer> chart = new Chart <>();
        chart.setTitle("chart 1");
        chart.addItem(1);
        chart.addItem(2);
        chart.addItem(4);

        return Arrays.asList(chart);
    }
}
