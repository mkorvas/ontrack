package net.nemerosa.ontrack.model.security;

public interface SecurityService {

    void checkGlobalFunction(Class<? extends GlobalFunction> fn);

    boolean isGlobalFunctionGranted(Class<? extends GlobalFunction> fn);

    Account getCurrentAccount();

}
