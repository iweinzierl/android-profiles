package de.iweinzierl.easyprofiles.widget.validation;

public class NotNullValidator<T> implements Validator<T> {

    @Override
    public void validate(T value) {
        if (value == null) {
            throw new ValidationError("Value is null");
        }
    }
}
