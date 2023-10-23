package com.jrackham.util;

import com.google.android.material.textfield.TextInputEditText;
import com.jrackham.model.Product;
import com.jrackham.persistence.realm.model.CategoryRealm;

import java.util.List;
import java.util.stream.Collectors;

public class UtilValidation {
    public static boolean areCheckBoxesSelectables(List<Product> products) {
        boolean selectable = false;
        List<Boolean> collect = products.stream().map(Product::isSelectable).collect(Collectors.toList());
        for (boolean b : collect) {
            if (b) {
                selectable = true;
                break;
            }
        }
        return selectable;
    }

    public static boolean areCheckBoxesSelected(List<Product> products) {
        boolean selected = false;
        List<Boolean> collect = products.stream().map(Product::isSelected).collect(Collectors.toList());
        for (boolean b : collect) {
            if (b) {
                selected = true;
                break;
            }
        }
        return selected;
    }

    /**
     * validacion de campos de texto vacios
     * @param fields lista de campos a validar
     * @return false si encuentra al menos uno vacio, sino true
     */
    public static boolean validateEmptyFields(List<TextInputEditText> fields) {
        for (TextInputEditText field : fields) {
            CharSequence text = field.getText();
            if (text == null || text.toString().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * method to validate the new category to be inserted
     * @param categories current categories
     * @param newCategory category name to insert
     * @return true if category exists else false
     */
    public static boolean validateExistCategory(List<CategoryRealm> categories, String newCategory) {
        List<String> collect = categories.stream().map(CategoryRealm::getName).collect(Collectors.toList());
        for (String category : collect) {
            if (category.equalsIgnoreCase(newCategory)) {
                return true;
            }
        }
        return false;
    }
}
