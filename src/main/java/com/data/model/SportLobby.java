package com.data.model;

import java.util.List;

public class SportLobby {

    private String link;
    private String name ;
    private String sportName;
    private String tableId;
    private List<BetInfo> betInfoList;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public List<BetInfo> getBetInfoList() {
        return betInfoList;
    }

    public void setBetInfoList(List<BetInfo> betInfoList) {
        this.betInfoList = betInfoList;
    }

}
