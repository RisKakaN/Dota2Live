package martin.so.dota2live;

public class Match {

    private String radiantTeam;
    private String direTeam;

    public Match(String radiantTeam, String direTeam) {
        this.radiantTeam = radiantTeam;
        this.direTeam = direTeam;
    }

    public String getRadiantTeam() {
        return radiantTeam;
    }

    public String getDireTeam() {
        return direTeam;
    }
}
