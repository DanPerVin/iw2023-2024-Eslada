package es.uca.iw.eslada.api;
import jakarta.validation.constraints.*;
public class CustomerLineRequest {
    //TODO: plantear cambio de customerlinerequest a solo customer
    @NotBlank
    @Size(min = 0, max = 20)
    private String name;

    @NotBlank
    @Size(min = 0, max = 20)
    private String surname;

    @NotNull
    @Size(min = 0, max = 20)
    private String carrier;

    @NotBlank
    private String phoneNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

