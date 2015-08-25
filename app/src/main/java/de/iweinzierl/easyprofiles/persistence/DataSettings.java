package de.iweinzierl.easyprofiles.persistence;

import com.orm.SugarRecord;

public class DataSettings extends SugarRecord<DataSettings> {

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
}
