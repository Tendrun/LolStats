package com.backendwebsite.lolstats.DTOs;

import java.util.List;

public class MatchDTO {
    private String _id;
    private List<String> puuids;
    private List<String> names;
    private String timestamp;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<String> getPuuids() {
        return puuids;
    }

    public void setPuuids(List<String> puuids) {
        this.puuids = puuids;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
