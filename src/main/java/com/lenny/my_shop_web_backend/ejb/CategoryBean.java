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

import com.lenny.my_shop_web_backend.utilities.ValuesFromHashMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

/**
 * @author lenny
 */
@SuppressWarnings("ALL")
@Stateless
public class CategoryBean {

    public static final Integer EMPTY_PRODUCT_LIST = 0;

    @EJB
    TransactionProvider provider;

    @EJB
    CategoryDatabaseBean categoryDatabaseBean;

    public Response addCategory(Category newCategory) {
        try {
            if (newCategory == null) {
                throw new BadRequestException("Category is empty");
            }
            String categoryName = newCategory.getName();
            String upperCaseName = categoryName.substring(0, 1).toUpperCase() + categoryName.substring(1);
            newCategory.setName(upperCaseName);
            Date currentDate = new Date();
            Category existingCategory = categoryDatabaseBean.getCategory_ByName(newCategory.getName());
            if (existingCategory != null && existingCategory.getDeletionStatus() == NOT_DELETED) {
                throw new BadRequestException("Category " + categoryName + " already exists");
            }
            if (existingCategory != null && existingCategory.getDeletionStatus() == DELETED) {
                existingCategory.setDeletionStatus(NOT_DELETED);
                existingCategory.setModifiedOn(currentDate);
                saveNewCategory(existingCategory);
            } else {
                newCategory.setDeletionStatus(NOT_DELETED);
                newCategory.setModifiedOn(currentDate);
                newCategory.setAddedDate(currentDate);
                saveNewCategory(newCategory);
            }
            HashMap<String, Object> res = new HashMap<>();
            res.put("Message", "Category " + categoryName + " has been created");
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured").build();
        }
    }

    private void saveNewCategory(Category category) {
        if (!provider.createEntity(category)) {
            throw new PersistenceException("Category has not been saved successfully");
        }
    }

    public Response editCategory(Integer categoryId, HashMap hashMap) {
        try {
            String categoryName = ValuesFromHashMap.getStringValue_FromHashMap(hashMap, "name");
            if (categoryName == null) {
                throw new BadRequestException("The field name is empty");
            }
            Category existingCategory = categoryDatabaseBean.getCategory_ByName(categoryName);
            if (existingCategory != null) {
                throw new BadRequestException("Category with name " + categoryName + " already exists");
            }
            Date currentDate = new Date();
            Category categoryToEdit = categoryDatabaseBean.getCategory_ById(categoryId);
            categoryToEdit.setName(categoryName);
            categoryToEdit.setModifiedOn(currentDate);
            if (!provider.updateEntity(categoryToEdit)) {
                throw new PersistenceException("Category was not updated successfully");
            }
            HashMap<String, Object> res = new HashMap<>();
            res.put("Message", "Category has been updated successfully");
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured").build();
        }
    }

    public Response deleteCategory(Integer categoryId) {
        try {
            if (categoryId == null) {
                throw new BadRequestException("Category Id is null");
            }
            Category categoryToDelete = categoryDatabaseBean.getCategory_ById(categoryId);
            if (categoryToDelete == null) {
                throw new BadRequestException("Category does not exist");
            }
            if (categoryToDelete.getDeletionStatus() == DELETED) {
                throw new BadRequestException("Category has already been deleted");
            }
            if (categoryToDelete.getProductList().size() != EMPTY_PRODUCT_LIST) {
                throw new BadRequestException("You can't delete category that has products");
            }
            categoryToDelete.setDeletionStatus(DELETED);
            if (!provider.updateEntity(categoryToDelete)) {
                throw new PersistenceException("Category was not deleted successfully. Try Again!");
            }
            HashMap<String, Object> res = new HashMap<>();
            res.put("Message", "Category " + categoryToDelete.getName() + " has been deleted");
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured").build();
        }
    }

    public Response getAllCategories() {
        try {
            List<Category> categories = categoryDatabaseBean.getAllCategories();
            if (categories == null) {
                throw new BadRequestException("No categories exist");
            }
            List allCategories = new ArrayList();
            for (Category category : categories) {
                HashMap categoryHashMap = new HashMap();
                categoryHashMap.put("categoryId", category.getId());
                categoryHashMap.put("categoryName", category.getName());
                categoryHashMap.put("dateAdded", category.getAddedDate());
                categoryHashMap.put("deletionStatis", category.getDeletionStatus());
                allCategories.add(categoryHashMap);
            }
            HashMap<String,Object> res = new HashMap();
            res.put("categories", allCategories);
            res.put("Message", "Fetched all categories");
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured").build();
        }
    }
}
