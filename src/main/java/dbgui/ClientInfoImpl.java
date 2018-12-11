package dbgui;

import javax.swing.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class ClientInfoImpl implements ClientInfo {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private String SIN;
    private String name;
    private String lastName;
    private Date birthDate;

    private Connection con;

    ClientInfoImpl(Connection con) {
        this.con = con;
    }

    @Override
    public boolean setName(String name) {
        boolean isSuccess;
        String sql = "UPDATE clientinfo SET name=? WHERE SIN=?";

        // Name must be alphabetical
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            if (!name.matches("[A-Za-z]+")) {
                throw new Exception("Name contains forbidden symbols!");
            } else {
                statement.setString(1, name);
                statement.setString(2, SIN);
                statement.executeUpdate();

                this.name = name;
                isSuccess = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error --> Cannot Update Name!");
            isSuccess = false;
        }
        return isSuccess;
    }

    @Override
    public boolean setLastName(String lastName) {
        boolean isSuccess;
        String sql = "UPDATE clientinfo SET lastname=? WHERE SIN=?";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            if (!lastName.matches("[A-Za-z]+")) {
                throw new Exception("Last name contains forbidden symbols!");
            } else {
                statement.setString(1, lastName);
                statement.setString(2, SIN);
                statement.executeUpdate();

                this.lastName = lastName;
                isSuccess = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error --> Cannot Update Last Name!");
            isSuccess = false;
        }
        return isSuccess;
    }

    @Override
    public boolean setBirthDate(String birthDate) {
        Date oldDate = this.birthDate;

        boolean isSuccess;
        String sql = "UPDATE clientinfo SET birthdate=? WHERE SIN=?";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            java.util.Date parsed = format.parse(birthDate);
            this.birthDate = new java.sql.Date(parsed.getTime());

            statement.setDate(1, this.birthDate);
            statement.setString(2, SIN);
            statement.executeUpdate();
            isSuccess = true;
        } catch (Exception e) {
            this.birthDate = oldDate;
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error --> Cannot Update Birth Date!");
            isSuccess = false;
        }
        return isSuccess;
    }

    @Override
    public boolean setBirthDate(Date birthDate) {
        boolean isSuccess;
        String sql = "UPDATE clientinfo SET birthdate=? WHERE SIN=?";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setDate(1, birthDate);
            statement.setString(2, SIN);
            statement.executeUpdate();

            this.birthDate = birthDate;
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error --> Cannot Update Birth Date!");
            isSuccess = false;
        }
        return isSuccess;
    }

    @Override
    public boolean setSIN(String SIN) {
        if (SIN.equals(this.SIN)) {
            return true;
        }

        boolean isSuccess;
        String sql = "UPDATE bankdb SET SIN=? WHERE SIN=?";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            if (!SIN.matches("[0-9]{7}")) {
                throw new Exception();
            } else {
                statement.setString(1, SIN);
                statement.setString(2, this.SIN);
                statement.executeUpdate();

                this.SIN = SIN;
                isSuccess = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error --> Cannot Update SIN!");
            isSuccess = false;
        }
        return isSuccess;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public Date getBirthDate() {
        return birthDate;
    }

    @Override
    public String getSIN() {
        return SIN;
    }

    @Override
    public void setSIN_NotLoadToDB(String SIN) {
        this.SIN = SIN;
    }
}
