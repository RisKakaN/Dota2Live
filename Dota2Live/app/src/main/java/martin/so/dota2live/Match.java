package martin.so.dota2live;

public class Match {

    private String radiantTeam;
    private String direTeam;
    private long startTime;
    private String radiantImageUrl;
    private String direImageUrl;

    public Match(String radiantTeam, String direTeam, long startTime, String radiantImageUrl, String direImageUrl) {
        this.radiantTeam = radiantTeam;
        this.direTeam = direTeam;
        this.startTime = startTime;
        this.radiantImageUrl = radiantImageUrl;
        this.direImageUrl = direImageUrl;
    }

    public String getRadiantTeam() {
        return radiantTeam;
    }

    public String getDireTeam() {
        return direTeam;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getRadiantImageUrl() {
        return radiantImageUrl;
    }

    public String getDireImageUrl() {
        return direImageUrl;
    }
}
