public class SourceDetails {

    String SIC;
    String Country;
    String City;
    String MerchantName;
    String MerchantID;

    public SourceDetails(){

    }
    public SourceDetails(String SIC, String country, String city, String merchantName, String merchantID) {
        this.SIC = SIC;
        Country = country;
        City = city;
        MerchantName = merchantName;
        MerchantID = merchantID;
    }

    public String getSIC() {
        return SIC;
    }

    public void setSIC(String SIC) {
        this.SIC = SIC;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getMerchantName() {
        return MerchantName;
    }

    public void setMerchantName(String merchantName) {
        MerchantName = merchantName;
    }

    public String getMerchantID() {
        return MerchantID;
    }

    public void setMerchantID(String merchantID) {
        MerchantID = merchantID;
    }
}
