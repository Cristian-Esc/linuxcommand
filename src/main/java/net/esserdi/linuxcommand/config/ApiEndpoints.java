package net.esserdi.linuxcommand.config;

public final class ApiEndpoints {

  public static final class Commands {
    public static final String BASE = "/api/commands";
    public static final String BY_ID = "/{id}";
    public static final String EXECUTE = "/execute/{url}";
    public static final String QR_CODE = "/{id}/qr";
    public static final String SERVER_ADDRESS = "/server-address";

    private Commands() {}
}

private ApiEndpoints() {}

}
