package org.blb.security.service;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptPasswordUser implements CustomTaskChange {
    private final String tableName="user";
    private final String columnName="password";
    private String rawPassword;
    @Override
    public void execute(Database database) throws CustomChangeException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPassword = encoder.encode(rawPassword);
        String sql = String.format("UPDATE %s SET %s = '%s'", tableName, columnName, encryptedPassword);
       // database.getConnection().execute(sql);
    }

    @Override
    public String getConfirmationMessage() {
        return "";
    }

    @Override
    public void setUp() throws SetupException {

    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {

    }

    @Override
    public ValidationErrors validate(Database database) {
        return null;
    }
}
