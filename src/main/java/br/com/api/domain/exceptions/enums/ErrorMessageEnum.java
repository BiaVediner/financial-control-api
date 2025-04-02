package br.com.api.domain.exceptions.enums;

import lombok.Getter;

@Getter
public enum ErrorMessageEnum {
    ENTRY_INVALID_VALUE("ENTRY001", "Cannot process entry because value is not valid"),
    ENTRY_INVALID_SUBCATEGORY_ID("ENTRY002", "Cannot process entry because subcategoryId is not valid"),
    ENTRY_NOT_FOUND("ENTRY003", "Entry not found"),
    ENTRY_START_DATE_FILTER("ENTRY004", "Cannot filter by date because start date is invalid"),
    ENTRY_END_DATE_FILTER("ENTRY005", "Cannot filter by date because end date is invalid"),
    ENTRY_ID_FILTER("ENTRY006", "Cannot filter because id is invalid"),

    SUBCATEGORY_INVALID_NAME("SUBCATEGORY001", "Cannot process subcategory because name is not valid"),
    SUBCATEGORY_INVALID_CATEGORY_ID("SUBCATEGORY002", "Cannot process subcategory because categoryId is not valid"),
    SUBCATEGORY_NOT_FOUND("SUBCATEGORY003", "Subcategory not found"),
    SUBCATEGORY_REGISTER_CONSTRAIN("SUBCATEGORY004", "Cannot register subcategory name because already exists"),
    SUBCATEGORY_DELETE_CONSTRAIN("SUBCATEGORY005", "Cannot register subcategory because it violates a constrain"),

    CATEGORY_INVALID_NAME("CATEGORY001", "Cannot process category because name is not valid"),
    CATEGORY_NOT_FOUND("CATEGORY002", "Category not found"),
    CATEGORY_REGISTER_CONSTRAIN("CATEGORY003", "Cannot register category name because already exists");

    private final String code;
    private final String message;

    ErrorMessageEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
