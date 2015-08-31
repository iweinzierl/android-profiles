package de.iweinzierl.easyprofiles.widget.validation;

import com.google.common.base.Strings;

public class NotEmptyStringValidator implements Validator<String> {

    @Override
    public void validate(String value) {
        if (Strings.isNullOrEmpty(value)) {
            throw new ValidationError("Value is null or empty!");
        }
    }
}
