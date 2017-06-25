package com.example.tzadmin.nfc_reader_writer.Models;

import android.support.annotation.Nullable;
import com.example.tzadmin.nfc_reader_writer.Utils;

import java.util.Collection;

/**
 * Created by velor on 6/23/17.
 */

public class MoneyLogs extends BaseModel {
    @MAnnotation(PrimaryKey = true)
    public Integer id;
    @MAnnotation
    public Integer userid;
    @MAnnotation
    public Integer money;
    @MAnnotation
    public Type type;
    @MAnnotation
    public String description;

    @Override
    public String GetTableName() {
        return "tbMoneyLogs";
    }

    @Nullable
    public User getUser() {
        User u = new User(); //TODO this object is useless. Maybe it created for selectAllByParams() ?
        u.id = userid;

        Collection<User> users = (Collection<User>) new User().selectAllByParams();

        return (users == null || users.size() == 0) ? null : Utils.getFirst(users);
    }

    public enum Type {
        ADD_MONEY("Add"),
        REMOVE_MONEY("Remove");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
