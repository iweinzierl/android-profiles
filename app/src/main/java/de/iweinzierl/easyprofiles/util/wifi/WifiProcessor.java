package de.iweinzierl.easyprofiles.util.wifi;

import android.content.Context;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.orm.SugarRecord;

import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.domain.WifiBasedTrigger;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.TriggerType;

public abstract class WifiProcessor {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(WifiProcessor.class.getName());

    protected final Context context;
    protected final String ssid;

    public WifiProcessor(Context context, String ssid) {
        this.context = context;
        this.ssid = ssid;
    }

    protected abstract boolean matches(WifiBasedTrigger trigger);

    protected abstract void enable(WifiBasedTrigger trigger);

    public void process() {
        Collection<WifiBasedTrigger> triggers = Collections2.filter(
                getEnabledWifiTriggers(),
                new Predicate<WifiBasedTrigger>() {
                    @Override
                    public boolean apply(WifiBasedTrigger input) {
                        return matches(input);
                    }
                });

        if (triggers == null || triggers.isEmpty()) {
            LOG.debug("No wifi trigger found for ssid '{}'", ssid);
        } else if (triggers.size() > 1) {
            LOG.warn("Found more than one wifi trigger for ssid '{}': {}", ssid, triggers.size());
            enable(triggers.iterator().next());
        } else {
            enable(triggers.iterator().next());
        }
    }

    protected Collection<WifiBasedTrigger> getEnabledWifiTriggers() {
        List<PersistentTrigger> triggers = SugarRecord.find(
                PersistentTrigger.class,
                "enabled = 1 and type = ?",
                TriggerType.WIFI.name());

        return Collections2.transform(triggers, new Function<PersistentTrigger, WifiBasedTrigger>() {
            @Override
            public WifiBasedTrigger apply(PersistentTrigger input) {
                WifiBasedTrigger wifiTrigger = new WifiBasedTrigger();
                wifiTrigger.apply(input);

                return wifiTrigger;
            }
        });
    }

    protected boolean areSsidEqual(String ssidA, String ssidB) {
        String testSsidA = ssidA.replaceAll("\"", "");
        String testSsidB = ssidB.replaceAll("\"", "");

        return Objects.equals(testSsidA, testSsidB);
    }
}
