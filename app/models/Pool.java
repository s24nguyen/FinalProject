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
public class Pool extends Model {

    @Id
    @GeneratedValue
    public Long id;

    public String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private Tournament tournament;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pool", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Entrant> entrants;

    public List<Entrant> getEntrants() {
        return entrants;
    }

}