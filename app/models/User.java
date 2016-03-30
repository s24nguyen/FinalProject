package models;

import org.springframework.ui.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * Created by Steven on 3/29/2016.
 */
@Entity
public class User extends Model {
    @Id
    @GeneratedValue
    public Long id;
    @Constraints.Required
    public String first_name;
    @Constraints.Required
    public String last_name;
    @Constraints.Required
    public String email;
    @Constraints.Required
    private String password;
    public String getPassword() { return password;}
    public void setPassword(String password) {this.password = password;}
}