package model.entities;

import model.enums.*;
import model.exceptions.*;

import java.util.Date;

public class License {

    private String type;
    private String id;
    private Date validTill;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getValidTill() {
        return validTill;
    }

    public void setValidTill(Date validTill) {
        this.validTill = validTill;
    }
}
