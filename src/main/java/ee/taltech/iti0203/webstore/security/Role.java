package ee.taltech.iti0203.webstore.security;

public enum Role {
    USER, ADMIN;

    public boolean isAdmin(){
        return this == ADMIN;
    }

}
