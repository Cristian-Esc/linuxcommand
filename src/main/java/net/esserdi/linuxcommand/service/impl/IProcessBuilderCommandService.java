package net.esserdi.linuxcommand.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import net.esserdi.linuxcommand.service.SystemCommandService;

@Service
public class IProcessBuilderCommandService implements SystemCommandService {


    @Async
    @Override
    public CompletableFuture<String> execute(String command) throws IOException {
        var process = startProcess(command);
        CompletableFuture<String> processFuture = new CompletableFuture<>();
        handleTimeout(process, processFuture, 5);
        handleProcessTimeout(process, 5);
        return processFuture;
    }

    private Process startProcess(String command) throws IOException {
        return new ProcessBuilder(List.of("bash", "-c", command)).redirectErrorStream(true).start();
    }

    private void handleTimeout(Process process, CompletableFuture<String> processFuture, int timeoutSeconds) {
        CompletableFuture.runAsync(() -> {
            try {
                if (process.waitFor(timeoutSeconds, TimeUnit.SECONDS)) {
                    processFuture.complete(process.exitValue() == 0
                            ? "Comando ejecutado exitosamente."
                            : "Comando falló.");
                } else {
                    // Si no termina en el tiempo esperado
                    processFuture.complete("Comando ejecutado exitosamente. Ejecución en segundo plano.");
                }
            } catch (InterruptedException e) {
                processFuture.completeExceptionally(e);
            }
        });
    }

    private void handleProcessTimeout(Process process, int timeoutMinutes) {
        CompletableFuture.runAsync(() -> {
            try {
                if (!process.waitFor(timeoutMinutes, TimeUnit.MINUTES)) {
                    process.destroy();
                }
            } catch (InterruptedException e) {
                process.destroy();
            }
        });
    }

}
