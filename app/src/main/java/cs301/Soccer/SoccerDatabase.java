package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Soccer player database -- presently, all dummied up
 *
 * @author *** put your name here ***
 * @version *** put date of completion here ***
 *
 */
public class SoccerDatabase implements SoccerDB {

    // dummied up variable; you will need to change this
    private Hashtable<String, SoccerPlayer> database = new Hashtable<>();

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {
        String player = firstName + " ## " + lastName;
        if(database.get(player) != null){
            return false;
        }
        else{
            SoccerPlayer sp = new SoccerPlayer(firstName, lastName, uniformNumber, teamName);
                database.put(player, sp);
                return true;
        }
    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {
        String player = firstName + " ## " + lastName;
        if(database.get(player) != null) {
            database.remove(player);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {
        String player = firstName + " ## " + lastName;
        return database.get(player);
    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {
        String player = firstName + " ## " + lastName;
        if(database.get(player) != null) {
            database.get(player).bumpGoals();
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {
        String player = firstName + " ## " + lastName;
        if(database.get(player) != null) {
            database.get(player).bumpYellowCards();
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {
        String player = firstName + " ## " + lastName;
        if(database.get(player) != null) {
            database.get(player).bumpRedCards();
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {
        int pl = 0;
        if(teamName == null){
            return database.size();
        }
        else{
            Enumeration<String> e = database.keys();
            while(e.hasMoreElements()){
                String key = e.nextElement();
                if(database.get(key).getTeamName().equals(teamName)){
                    pl++;
                }
            }
            return pl;
        }
    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int idx, String teamName) {
        SoccerPlayer currPlayer = null;
        int i = 0;
        int totalPlayers = numPlayers(teamName);
        Enumeration<String> e = database.keys();
        if(idx < totalPlayers){
            while(e.hasMoreElements()){
                String key = e.nextElement();
                if(teamName == null){
                    if(i == idx){
                        currPlayer = database.get(key);
                    }
                    i++;
                }
                else{
                    if(database.get(key).getTeamName().equals(teamName)){
                        if (i == idx){
                            currPlayer = database.get(key);
                        }
                        i++;
                    }
                }
            }
        }
        return currPlayer;
    }

    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @Override
    public boolean readData(File file) {
        Scanner sc;
        try{
            sc = new Scanner(file);
        }
        catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
            return false;
        }

        while(sc.hasNextLine()){
            String fN = sc.nextLine();
            String lN = sc.nextLine();
            String tN = sc.nextLine();
            int jnum = sc.nextInt();
            int goal = sc.nextInt();
            int r_card = sc.nextInt();
            int y_card = sc.nextInt();

            String player = fN + "#" + lN;
            if(database.containsKey(player)){
                database.remove(player);
            }

            SoccerPlayer p1 = new SoccerPlayer(fN, lN, jnum, tN);

            for(int i = 0; i < goal; i++){
                p1.bumpGoals();
            }

            for(int i = 0; i < r_card; i++){
                p1.bumpRedCards();
            }

            for(int i = 0; i < y_card; i++){
                p1.bumpYellowCards();
            }
            database.put(player, p1);
            String u = sc.nextLine();
        }

        sc.close();
        return true;
    }


    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @Override
    public boolean writeData(File file) {
        PrintWriter pw;

        try {
            pw = new PrintWriter(file);
        }
        catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
            return false;
        }
        for(SoccerPlayer player : database.values()){
            pw.println(player.getFirstName());
            pw.println(player.getLastName());
            pw.println(player.getTeamName());
            pw.println(Integer.toString(player.getUniform()));
            pw.println(Integer.toString(player.getGoals()));
            pw.println(Integer.toString(player.getRedCards()));
            pw.println(Integer.toString(player.getYellowCards()));
        }
        pw.close();
        return true;
    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
        Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {
        HashSet<String> x = new HashSet<>();
        for(SoccerPlayer player : database.values()){
            if(!x.contains(player.getTeamName())){
                x.add(player.getTeamName());
            }
        }
        return x;
    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}
