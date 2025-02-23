package net.esserdi.linuxcommand.service;

import java.util.concurrent.CompletableFuture;

public interface SystemCommandService {
	CompletableFuture<String> execute(String command) throws Exception;
}
