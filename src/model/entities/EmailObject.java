package model.entities;

public class EmailObject {

    private String toEmail = "";
    private String toName = "";
    private String subject = "";
    private String matter = "";

    public EmailObject(String toEmail, String toName, String subject, String matter) {
        this.toEmail = toEmail;
        this.toName = toName;
        this.subject = subject;
        this.matter = matter;
    }

    // Accessor Methods Starts Here
    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMatter() {
        return matter;
    }

    public void setMatter(String matter) {
        this.matter = matter;
    }

    @Override
    public String toString() {
        return "EmailObject{" +
                "toEmail='" + toEmail + '\'' +
                ", toName='" + toName + '\'' +
                ", subject='" + subject + '\'' +
                ", matter='" + matter + '\'' +
                '}';
    }
}
