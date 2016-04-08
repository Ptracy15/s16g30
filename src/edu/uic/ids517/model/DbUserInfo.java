package edu.uic.ids517.model;


public class DbUserInfo
{

    private String userName;
    private String password;
    private String dbms;
    private String dbmsHost;
    private String databaseSchema;

    public DbUserInfo()
    {
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getDbms()
    {
        return dbms;
    }

    public void setDbms(String dbms)
    {
        this.dbms = dbms;
    }

    public String getDbmsHost()
    {
        return dbmsHost;
    }

    public void setDbmsHost(String dbmsHost)
    {
        this.dbmsHost = dbmsHost;
    }

    public String getDatabaseSchema()
    {
        return databaseSchema;
    }

    public void setDatabaseSchema(String databaseSchema)
    {
        this.databaseSchema = databaseSchema;
    }
}
