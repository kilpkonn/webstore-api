package ee.taltech.iti0203.webstore.security;

public enum Role {
    USER, ADMIN, UNVERIFIED;

    public String toSpringRole() {
        return "ROLE_" + this.name();
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
