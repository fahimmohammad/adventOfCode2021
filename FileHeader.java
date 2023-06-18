public class FileHeader {
    String FileLabel;
    String FormatVersion;
    String Sender;
    String CreationDate;
    String CreationTime;
    String FileSeqNumber;
    String Receiver;

    public FileHeader() {
    }

    public String getFileLabel() {
        return FileLabel;
    }

    public void setFileLabel(String fileLabel) {
        FileLabel = fileLabel;
    }

    public String getFormatVersion() {
        return FormatVersion;
    }

    public void setFormatVersion(String formatVersion) {
        FormatVersion = formatVersion;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }

    public String getCreationTime() {
        return CreationTime;
    }

    public void setCreationTime(String creationTime) {
        CreationTime = creationTime;
    }

    public String getFileSeqNumber() {
        return FileSeqNumber;
    }

    public void setFileSeqNumber(String fileSeqNumber) {
        FileSeqNumber = fileSeqNumber;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }
}
