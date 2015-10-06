package de.iweinzierl.easyprofiles.persistence;

import com.google.common.base.MoreObjects;
import com.orm.dsl.Table;

@Table
public class DataSettings {

    private Long id;

    private boolean dataSyncEnabled;
    private boolean dataEnabled;

    public DataSettings() {
        this.dataSyncEnabled = true;
        this.dataEnabled = true;
    }

    public DataSettings(boolean dataSyncEnabled, boolean dataEnabled) {
        this.dataSyncEnabled = dataSyncEnabled;
        this.dataEnabled = dataEnabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDataSyncEnabled() {
        return dataSyncEnabled;
    }

    public void setDataSyncEnabled(boolean dataSyncEnabled) {
        this.dataSyncEnabled = dataSyncEnabled;
    }

    public boolean isDataEnabled() {
        return dataEnabled;
    }

    public void setDataEnabled(boolean dataEnabled) {
        this.dataEnabled = dataEnabled;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("dataSyncEnabled", dataSyncEnabled)
                .add("dataEnabled", dataEnabled)
                .toString();
    }
}
