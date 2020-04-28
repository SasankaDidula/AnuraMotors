package com.anura.anuramotors.data.model;

public class vehicle {
   private String assTitle;
   private String VNO;
   private String TYPE;
   private String COLOUR;
   private String DETAILS;

    public vehicle() {
    }

    public vehicle(String assTitle, String VNO, String TYPE, String COLOUR, String DETAILS) {
        this.assTitle = assTitle;
        this.VNO = VNO;
        this.TYPE = TYPE;
        this.COLOUR = COLOUR;
        this.DETAILS = DETAILS;
    }

    public String getAssTitle() {
        return assTitle;
    }

    public void setAssTitle(String assTitle) {
        this.assTitle = assTitle;
    }

    public String getVNO() {
        return VNO;
    }

    public void setVNO(String VNO) {
        this.VNO = VNO;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getCOLOUR() {
        return COLOUR;
    }

    public void setCOLOUR(String COLOUR) {
        this.COLOUR = COLOUR;
    }

    public String getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(String DETAILS) {
        this.DETAILS = DETAILS;
    }
}
