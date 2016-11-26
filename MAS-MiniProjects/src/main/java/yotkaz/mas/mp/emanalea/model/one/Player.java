package yotkaz.mas.mp.emanalea.model.one;

import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * Created on 03.04.16.
 */
public class Player extends Rival {

    private static Duration necessaryExperience = Duration.ofDays(30);

    private String email;
    private String phone;
    private Address address;
    private String info;
    private Set<String> pseudonyms = new HashSet<>();
    private Date registrationDate;
    private Set<Team> teams = new HashSet<>();
    private Set<Team> teamsAsLeader = new HashSet<>();

    public Player(String name, String email, Date registrationDate) {
        super(name);
        setEmail(email);
        setRegistrationDate(registrationDate);
    }

    public static Duration getNecessaryExperience() {
        return necessaryExperience;
    }

    public static void setNecessaryExperience(Duration necessaryExperience) {
        Player.necessaryExperience = necessaryExperience;
    }

    public String getEmail() {
        return email;
    }

    public Player setEmail(String email) {
        this.email = email;
        return this;
    }

    public Optional<String> getPhone() {
        return Optional.ofNullable(phone);
    }

    public Player setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Optional<String> getInfo() {
        return Optional.ofNullable(info);
    }

    public Player setInfo(String info) {
        this.info = info;
        return this;
    }

    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }

    public Player setAddress(Address address) {
        this.address = address;
        return this;
    }

    public Set<Team> getTeams() {
        return teams;
    }


    public Set<Team> getTeamsAsLeader() {
        return teamsAsLeader;
    }

    public Player setTeamsAsLeader(Set<Team> teamsAsLeader) {
        this.teamsAsLeader = teamsAsLeader;
        return this;
    }

    public Set<String> getPseudonyms() {
        return pseudonyms;
    }

    public Player setPseudonyms(String... pseudonyms) {
        HashSet<String> newPseudonyms = new HashSet<>();
        newPseudonyms.addAll(Arrays.asList(pseudonyms));
        setPseudonyms(newPseudonyms);
        return this;
    }

    public Player setPseudonyms(Set<String> pseudonyms) {
        this.pseudonyms = pseudonyms;
        return this;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public Player setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public Duration getExperience() {
        return Duration.between(getRegistrationDate().toInstant(), Instant.now());
    }

    public boolean hasExperience() {
        return getNecessaryExperience().getSeconds() < getExperience().getSeconds();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append(getName());

        if (!getPseudonyms().isEmpty()) {
            sb
                    .append(" (")
                    .append(StringUtils.join(getPseudonyms(), ','))
                    .append((")"));
        }

        getAddress().ifPresent(address -> sb
                .append(" from ")
                .append(address.getCity())
                .append(", ")
                .append(address.getCountry()));

        getPhone().ifPresent(phone -> sb
                .append(" <")
                .append(phone)
                .append(">"));
        sb
                .append(" [")
                .append(getExperience().toDays())
                .append(" exp]");

        return sb.toString();
    }
}
