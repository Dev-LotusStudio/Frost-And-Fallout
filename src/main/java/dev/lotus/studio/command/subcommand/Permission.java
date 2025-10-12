package dev.lotus.studio.command.subcommand;

public enum Permission {
    ITEM("frostandfallout.item"),
    RELOAD("frostandfallout.reload"),
    SAFE_ZONE("frostandfallout.safezone"),
    USE("frostandfallout.use");

    private final String node;

    Permission(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }
}
