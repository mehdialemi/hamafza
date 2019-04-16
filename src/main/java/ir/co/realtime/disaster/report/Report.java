package ir.co.realtime.disaster.report;

import com.vividsolutions.jts.geom.Point;
import ir.co.realtime.disaster.auth.model.DateAudit;
import ir.co.realtime.disaster.file.Image;
import ir.co.realtime.disaster.file.Video;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
public class Report extends DateAudit {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String subject;

    private String message;

    private String tags;

    @OneToMany
    private Set<Image> images;

    @OneToMany
    private Set<Video> videos;

    @Enumerated
    private Source source;

    private Point point;

    private boolean confirmed;

    private int vote;

    private int warningThreshold;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getWarningThreshold() {
        return warningThreshold;
    }

    public void setWarningThreshold(int warningThreshold) {
        this.warningThreshold = warningThreshold;
    }

    public Set <Image> getImages() {
        return images;
    }

    public void setImages(Set <Image> images) {
        this.images = images;
    }

    public Set <Video> getVideos() {
        return videos;
    }

    public void setVideos(Set <Video> videos) {
        this.videos = videos;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public enum Source {
        GOVERNMENT,
        LOCAL
    }
}
