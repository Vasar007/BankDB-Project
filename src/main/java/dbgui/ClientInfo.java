package dbgui;


import java.sql.Date;

public interface ClientInfo {
    boolean setName(String name);
    boolean setLastName(String lastName);
    boolean setBirthDate(String birthDate);
    boolean setBirthDate(Date birthDate);
    boolean setSIN(String SIN);

    void setSIN_NotLoadToDB(String SIN);

    String getName();
    String getLastName();
    Date getBirthDate();
    String getSIN();
}
