package org.qtum.wallet.utils.migration_manager;


public enum  KeystoreMigrationResult {
    NOT_NEED ("NOT_NEED"), ERROR ("ERROR"), SUCCESS ("SUCCESS");

    private final String name;

    private KeystoreMigrationResult(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
