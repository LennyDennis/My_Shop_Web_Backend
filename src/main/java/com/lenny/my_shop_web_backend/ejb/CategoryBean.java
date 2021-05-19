/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.ejb;

import com.lenny.my_shop_web_backend.ejb_db.CategoryDatabaseBean;
import com.lenny.my_shop_web_backend.entities.Category;
import com.lenny.my_shop_web_backend.jpa.TransactionProvider;
import static com.lenny.my_shop_web_backend.utilities.ConstantVariables.*;
import com.lenny.my_shop_web_backend.utilities.JsonResponse;
import com.lenny.my_shop_web_backend.utilities.ValuesFromHashMap;
import java.util.Date;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author lenny
 */
@Stateless
public class CategoryBean {

    @EJB
    TransactionProvider provider;

    @EJB
    CategoryDatabaseBean categoryDatabaseBean;

    public JsonResponse addCategory(Category category) {
        JsonResponse response = new JsonResponse(ERROR_CODE, ERROR_MESSAGE);
        Date currentDate = new Date();
        try {
            if (category != null) {
                Category existingCategory = categoryDatabaseBean.getCategory_ByName(category.getName());
                if (existingCategory == null || existingCategory.getDeletionStatus() == DELETED) {
                    if (existingCategory.getDeletionStatus() == DELETED) {
                        existingCategory.setAddedDate(currentDate);
                        existingCategory.setDeletionStatus(NOT_DELETED);
                        if (provider.createEntity(existingCategory)) {
                            response.setResponseCode(SUCCESS_CODE);
                            response.setMessage("Category " + existingCategory.getName() + " has been created");
                        }
                    } else {
                        category.setAddedDate(currentDate);
                        category.setDeletionStatus(NOT_DELETED);
                        if (provider.createEntity(category)) {
                            response.setResponseCode(SUCCESS_CODE);
                            response.setMessage("Category " + category.getName() + " has been created");
                        }
                    }

                } else {
                    response.setMessage("Category " + category.getName() + " already exists");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return response;
        }
    }

    public JsonResponse editCategory(Integer categoryId, HashMap hashMap) {
        JsonResponse response = new JsonResponse(ERROR_CODE, ERROR_MESSAGE);
        try {
            if (hashMap != null) {
                String categoryName = ValuesFromHashMap.getStringValue_FromHashMap(hashMap, "name");
                Category existingCategory = categoryDatabaseBean.getCategory_ByName(categoryName);
                if (existingCategory == null) {
                    Category categoryToEdit = categoryDatabaseBean.getCategory_ById(categoryId);
                    if (categoryToEdit != null) {
                        String originalName = categoryToEdit.getName();
                        categoryToEdit.setName(categoryName);
                        if (provider.updateEntity(categoryToEdit)) {
                            response.setResponseCode(SUCCESS_CODE);
                            response.setMessage("Category name has been updated to " + categoryToEdit.getName());
                        }
                    }
                } else {
                    response.setMessage("Category with name " + categoryName + " already exists");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return response;
        }
    }

    public JsonResponse deleteCategory(Integer categoryId) {
        JsonResponse response = new JsonResponse(ERROR_CODE, ERROR_MESSAGE);
        try {
            if (categoryId != null) {
                Category categoryToDelete = categoryDatabaseBean.getCategory_ById(categoryId);
                if (categoryToDelete != null) {
                    if (categoryToDelete.getDeletionStatus() != DELETED) {
                        categoryToDelete.setDeletionStatus(DELETED);
                        if (provider.updateEntity(categoryToDelete)) {
                            response.setResponseCode(SUCCESS_CODE);
                            response.setMessage("Category " + categoryToDelete.getName() + " has been deleted");
                        }
                    } else {
                        response.setMessage("Category has already been deleted");
                    }
                } else {
                    response.setMessage("Category does not exist");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return response;
        }
    }

}