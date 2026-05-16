package com.moneyFlow.DAO;

import com.moneyFlow.model.UserModel;
import java.util.List;

public class UserDAO implements IGenericDAO<UserModel> {
    @Override
    public int create(UserModel entity) {
        return 0;
    }
    @Override
    public void update(UserModel entity) {
        
    }
    @Override
    public void delete(int id) {
        
    }
    @Override
    public UserModel findByID(int id) {
        return null;
    }
    @Override
    public List<UserModel> findAll() {
        return null;
    }
}