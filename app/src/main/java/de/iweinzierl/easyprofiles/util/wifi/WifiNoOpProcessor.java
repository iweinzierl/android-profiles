package de.iweinzierl.easyprofiles.util.wifi;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.domain.WifiBasedTrigger;

public class WifiNoOpProcessor extends WifiProcessor {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(WifiNoOpProcessor.class.getName());

    public WifiNoOpProcessor() {
        super(null, null);
    }

    @Override
    public void process() {
        LOG.info("skip processing for NoOp processor");
    }

    @Override
    public boolean matches(WifiBasedTrigger trigger) {
        LOG.info("always return false for NoOp processor");
        return false;
    }

    @Override
    protected void enable(WifiBasedTrigger trigger) {
        LOG.info("skip enabling trigger for NoOp processor");
    }
}
