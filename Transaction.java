public class Transaction {

    String Currency;
    String Amount;
    String AUTH_MODE;
    String FX_RATE;
    String CARD_ABSENT;
    String BCURR;
    String SG;
    String WHAT_TRX;
    String NEXT_AMOUNT;
    String BRAND;
    String FIN_CURR;
    String NEXT_CURR;
    String NEXT_CH;
    String PROC_CLASS;
    String CPID;

    public Transaction() {
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getAUTH_MODE() {
        return AUTH_MODE;
    }

    public void setAUTH_MODE(String AUTH_MODE) {
        this.AUTH_MODE = AUTH_MODE;
    }

    public String getFX_RATE() {
        return FX_RATE;
    }

    public void setFX_RATE(String FX_RATE) {
        this.FX_RATE = FX_RATE;
    }

    public String getCARD_ABSENT() {
        return CARD_ABSENT;
    }

    public void setCARD_ABSENT(String CARD_ABSENT) {
        this.CARD_ABSENT = CARD_ABSENT;
    }

    public String getBCURR() {
        return BCURR;
    }

    public void setBCURR(String BCURR) {
        this.BCURR = BCURR;
    }

    public String getSG() {
        return SG;
    }

    public void setSG(String SG) {
        this.SG = SG;
    }

    public String getWHAT_TRX() {
        return WHAT_TRX;
    }

    public void setWHAT_TRX(String WHAT_TRX) {
        this.WHAT_TRX = WHAT_TRX;
    }

    public String getNEXT_AMOUNT() {
        return NEXT_AMOUNT;
    }

    public void setNEXT_AMOUNT(String NEXT_AMOUNT) {
        this.NEXT_AMOUNT = NEXT_AMOUNT;
    }

    public String getBRAND() {
        return BRAND;
    }

    public void setBRAND(String BRAND) {
        this.BRAND = BRAND;
    }

    public String getFIN_CURR() {
        return FIN_CURR;
    }

    public void setFIN_CURR(String FIN_CURR) {
        this.FIN_CURR = FIN_CURR;
    }

    public String getNEXT_CURR() {
        return NEXT_CURR;
    }

    public void setNEXT_CURR(String NEXT_CURR) {
        this.NEXT_CURR = NEXT_CURR;
    }

    public String getNEXT_CH() {
        return NEXT_CH;
    }

    public void setNEXT_CH(String NEXT_CH) {
        this.NEXT_CH = NEXT_CH;
    }

    public String getPROC_CLASS() {
        return PROC_CLASS;
    }

    public void setPROC_CLASS(String PROC_CLASS) {
        this.PROC_CLASS = PROC_CLASS;
    }

    public String getCPID() {
        return CPID;
    }

    public void setCPID(String CPID) {
        this.CPID = CPID;
    }
}
