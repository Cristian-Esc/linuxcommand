package net.esserdi.linuxcommand.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import net.esserdi.linuxcommand.model.LinuxCommand;

@Repository
public interface LinuxCommandRepository extends JpaRepository<LinuxCommand, Long> {
	Optional<LinuxCommand> findByUrl(String url);
}
