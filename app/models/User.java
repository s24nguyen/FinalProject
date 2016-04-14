package models;

import com.avaje.ebean.*;
import play.data.validation.Constraints;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import play.*;

import javax.annotation.Generated;
import javax.persistence.*;
import javax.validation.Constraint;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


@Entity
public class User extends Model{
    @Id
    @GeneratedValue
    public Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Constraints.Required
    public String first_name;

    public String getFirst_name() {
        return first_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    @Constraints.Required
    public String last_name;
    public String getLast_name() {
        return last_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    @Constraints.Required
    public String gamerTag;
    public String getGamerTag() {
        return gamerTag;
    }
    public void setGamerTag(String gamerTag) {
        this.gamerTag = gamerTag;
    }

    @Constraints.Required
    public String platform;

    public String getPlatform() {
        return platform;
    }
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Constraints.Required
    public String dobMonth;

    public String getDobMonth() {
        return dobMonth;
    }
    public void setDobMonth(String dobMonth) {
        this.dobMonth = dobMonth;
    }

    @Constraints.Required
    public String dobDay;

    public String getDobDay() {
        return dobDay;
    }
    public void setDobDay(String dobDay) {
        this.dobDay = dobDay;
    }

    @Constraints.Required
    public String dobYear;

    public String getDobYear() {
        return dobYear;
    }
    public void setDobYear(String dobYear) {
        this.dobYear = dobYear;
    }

    @Constraints.Required
    public String address;

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    @Constraints.Required
    public String city;

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    @Constraints.Required
    public String state;

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    @Constraints.Required
    @Constraints.Email
    public String email;

    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Constraints.Required
    public String password;

    public String getPassword() {
        return password;
    }
    public void setPassword(String password)
    {
        this.password = User.getSha256(password);
    }


    public String icon_URL;

    public String getIcon_URL() {
        return icon_URL;
    }
    public void setIcon_URL(String icon_URL) {
        this.icon_URL = icon_URL;
    }

    public String favGame;

    public String getFavGame() {
        return favGame;
    }
    public void setFavGame(String favGame) {
        this.favGame = favGame;
    }

    public String gameTeam;

    public String getGameTeam() {
        return gameTeam;
    }
    public void setGameTeam(String gameTeam) {
        this.gameTeam = gameTeam;
    }

    @Column(length = 156466)
    public String playerBio;

    public String getPlayerBio() {
        return playerBio;
    }
    public void setPlayerBio(String playerBio) {
        this.playerBio = playerBio;
    }

    private String token;
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }



    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Tournament> tournaments;

    public List<Tournament> getTournaments() {
        return tournaments;
    }




    // Methods
    //public static Finder<Long,User> find = new Finder<>(Long.class, User.class);

    public static User findByEmail(String email)
    {
        return Ebean.find(User.class)
                .where()
                .like("email", email)
                .findUnique();
    }

    public static User gamerTagLogin(String gamerTag)
    {
        return Ebean.find(User.class)
                .where()
                .like("gamerTag", gamerTag)
                .findUnique();
    }


    /**
     * Return a page of computer
     *
     * @param page     Page to display
     * @param pageSize Number of computers per page
     * @param sortBy   Computer property used for sorting
     * @param order    Sort order (either or asc or desc)
     * @param filter   Filter applied on the name column
     */
    public static PagedList<User> page(int page, int pageSize, String sortBy,
                                  String order, String filter) {
        return Ebean.find(User.class)
                .where()
                .ilike("first_name", "%" + filter + "%")
                .orderBy(sortBy + " " + order)
                .fetch("company")
                .findPagedList(pageSize*(page-1)+1, pageSize*page);
    }

    // Transient field
    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = User.getSha256(confirm_password);
    }

    // Transient field
    @Transient
    String confirm_password;

    // Get SHA password
    public static String getSha256(String input) {
        MessageDigest mDigest = null;
        try {
            mDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }


}
