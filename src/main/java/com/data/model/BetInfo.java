package com.data.model;

import java.util.Map;

public class BetInfo {
    private Long date;
    private String homeTeam;
    private String visitTeam;
    private Map<String, CoeffInfo> coefficients;

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getVisitTeam() {
        return visitTeam;
    }

    public void setVisitTeam(String visitTeam) {
        this.visitTeam = visitTeam;
    }

    public Map<String, CoeffInfo> getCoefficients() {
        return coefficients;
    }

    public void setCoefficients(Map<String, CoeffInfo> coefficients) {
        this.coefficients = coefficients;
    }
}
