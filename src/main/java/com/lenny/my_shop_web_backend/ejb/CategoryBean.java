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
import java.util.Date;
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
                if (existingCategory == null) {
                    category.setAddedDate(currentDate);
                    category.setDeletionStatus(NOT_DELETED);
                    if (provider.createEntity(category)) {
                        response.setResponseCode(SUCCESS_CODE);
                        response.setMessage("Category " + category.getName() + " has been created");
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

}
