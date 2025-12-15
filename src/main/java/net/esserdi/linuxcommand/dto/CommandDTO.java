package net.esserdi.linuxcommand.dto;

public record CommandDTO(
    Long id,
    String title,
    String description,
    String command,
    boolean active,
    String url,
    String run) {

}
