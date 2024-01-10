package es.uca.iw.eslada.api;

public class CustomerLineWrapper {
    private CustomerLine customerLine;
    private Linea linea;

    public CustomerLine getCustomerLine() {
        return customerLine;
    }

    public void setCustomerLine(CustomerLine customerLine) {
        this.customerLine = customerLine;
    }

    public Linea getLinea() {
        return linea;
    }

    public void setLinea(Linea linea) {

        this.linea = linea;
    }
    @Override
    public String toString() {
        return "CustomerLineWrapper{" +
                "name='" + customerLine.getName() + '\'' +
                ", surname='" + customerLine.getSurname() + '\'' +
                ", carrier='" + customerLine.getCarrier() + '\'' +
                ", id='" + customerLine.getId() + '\'' +
                ", phoneNumber='" + customerLine.getPhoneNumber() + '\'' +
                '}';
    }
}
