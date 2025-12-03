package com.service.pasteleriamilsabores.models.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.service.pasteleriamilsabores.models.UserType;

@Converter(autoApply = false)
public class UserTypeConverter implements AttributeConverter<UserType, String> {

    @Override
    public String convertToDatabaseColumn(UserType attribute) {
        if (attribute == null) return null;
        return attribute.getValue();
    }

    @Override
    public UserType convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return UserType.fromString(dbData);
    }
}
