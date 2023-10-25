package com.jrackham.util;

import android.widget.EditText;

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
     * @return true si encuentra al menos uno vacio, sino false
     */
    public static boolean validateEmptyFields(List<EditText> fields) {
        for (EditText field : fields) {
            CharSequence text = field.getText();
            if (text == null || text.toString().trim().isEmpty()) {
                return true;
            }
        }
        return false;
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
