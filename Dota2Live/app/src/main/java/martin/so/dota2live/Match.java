package martin.so.dota2live;

public class Match {

    private String radiantTeam;
    private String direTeam;
    private String date;
    private String radiantImageUrl;
    private String direImageUrl;

    public Match(String radiantTeam, String direTeam, String date, String radiantImageUrl, String direImageUrl) {
        this.radiantTeam = radiantTeam;
        this.direTeam = direTeam;
        this.date = date;
        this.radiantImageUrl = radiantImageUrl;
        this.direImageUrl = direImageUrl;
    }

    public String getRadiantTeam() {
        return radiantTeam;
    }

    public String getDireTeam() {
        return direTeam;
    }

    public String getDate() {
        return date;
    }

    public String getRadiantImageUrl() {
        return radiantImageUrl;
    }

    public String getDireImageUrl() {
        return direImageUrl;
    }
}
