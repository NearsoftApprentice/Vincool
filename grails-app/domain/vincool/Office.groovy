package vincool

class Office {

    enum OfficeCode {
        CDMX, CUU, HMO
    }

    OfficeCode officeCode
    String state
    String city
    String street
    Integer number
    String zipCode
    String phoneNumber

    static constraints = {
        officeCode blank: false, unique: true
        state blank: false
        city blank: false
        street blank: false
        number blank: false, min: 0
        zipCode blank: false
        phoneNumber blank: false
    }

    static mapping = {
        version false
    }
}
