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
public class Tournament extends Model {

    @Id
    @GeneratedValue
    public Long id;

    public String tournament_name;
    public String game;
    public String location;
    public String date;
    public String tOrganizer;
    public String platform;
    public String tournamentDetail;
    public String tournamentPicture;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private User owner;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "tournament", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Pool> pools;

    public List<Pool> getPools() {
        return pools;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTournament_name() {
        return tournament_name;
    }

    public void setTournament_name(String tournament_name) {
        this.tournament_name = tournament_name;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String gettOrganizer() {
        return tOrganizer;
    }

    public void settOrganizer(String tOrganizer) {
        this.tOrganizer = tOrganizer;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getTournamentDetail() {
        return tournamentDetail;
    }

    public void setTournamentDetail(String tournamentDetail) {
        this.tournamentDetail = tournamentDetail;
    }

    public String getTournamentPicture() {
        return tournamentPicture;
    }

    public void setTournamentPicture(String tournamentPicture) {
        this.tournamentPicture = tournamentPicture;
    }
}