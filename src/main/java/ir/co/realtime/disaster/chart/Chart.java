package ir.co.realtime.disaster.chart;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Chart<T extends Comparable> implements Serializable {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private List<T> data = new ArrayList <>();

    private List<String> lables = new ArrayList <>();

    private List<String> axes = new ArrayList <>();

    public void addItem(T item) {
        data.add(item);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List <T> getGetData() {
        return data;
    }

    public void setGetData(List <T> data) {
        this.data = data;
    }

    public List <String> getLables() {
        return lables;
    }

    public void setLables(List <String> lables) {
        this.lables = lables;
    }

    public List <String> getAxes() {
        return axes;
    }

    public void setAxes(List <String> axes) {
        this.axes = axes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    enum Type {
        LINE,
        PIٍE,
        COLUMN,
        BAR,
        AREA
    }
}
