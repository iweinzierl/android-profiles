package de.iweinzierl.easyprofiles.persistence;

import com.orm.SugarRecord;

public class ExtraSettings extends SugarRecord<ExtraSettings> {

    private boolean gpsEnabled;
    private boolean bluetoothEnabled;
    private boolean nfcEnabled;

    public ExtraSettings() {
        this.gpsEnabled = false;
        this.bluetoothEnabled = false;
        this.nfcEnabled = false;
    }

    public ExtraSettings(boolean gpsEnabled, boolean bluetoothEnabled, boolean nfcEnabled) {
        this.gpsEnabled = gpsEnabled;
        this.bluetoothEnabled = bluetoothEnabled;
        this.nfcEnabled = nfcEnabled;
    }

    public boolean isGpsEnabled() {
        return gpsEnabled;
    }

    public void setGpsEnabled(boolean gpsEnabled) {
        this.gpsEnabled = gpsEnabled;
    }

    public boolean isBluetoothEnabled() {
        return bluetoothEnabled;
    }

    public void setBluetoothEnabled(boolean bluetoothEnabled) {
        this.bluetoothEnabled = bluetoothEnabled;
    }

    public boolean isNfcEnabled() {
        return nfcEnabled;
    }

    public void setNfcEnabled(boolean nfcEnabled) {
        this.nfcEnabled = nfcEnabled;
    }
}
