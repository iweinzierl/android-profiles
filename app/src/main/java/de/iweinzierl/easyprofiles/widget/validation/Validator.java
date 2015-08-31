package de.iweinzierl.easyprofiles.widget.validation;

public interface Validator<T> {

    /**
     * Validates the given input.
     *
     * @param value
     *
     * @throws ValidationError when validation failed.
     */
    void validate(T value);
}
