package yotkaz.mas.mp.emanalea.model.one;

import yotkaz.mas.mp.emanalea.base.Extent;

import java.util.Optional;

/**
 * Created on 08.04.16.
 */
public class Address extends Extent {

    private String country;
    private String city;
    private String street;
    private String buildingNumber;
    private String apartmentNumber;
    private String postCode;

    public Address(String country, String city, String street, String buildingNumber) {
        setCountry(country);
        setCity(city);
        setStreet(street);
        setBuildingNumber(buildingNumber);
    }

    public String getCountry() {
        return country;
    }

    public Address setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Address setCity(String city) {
        this.city = city;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public Address setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public Address setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
        return this;
    }

    public Optional<String> getApartmentNumber() {
        return Optional.of(apartmentNumber);
    }

    public Address setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
        return this;
    }

    public Optional<String> getPostCode() {
        return Optional.of(postCode);
    }

    public Address setPostCode(String postCode) {
        this.postCode = postCode;
        return this;
    }

}