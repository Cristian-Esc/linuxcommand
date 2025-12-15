package net.esserdi.linuxcommand.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
<<<<<<< HEAD
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@Configuration
public class SecurityConfig {


  /**
   * AuthorizationManager que permite acceso únicamente desde localhost (IPv4 / IPv6)
   */
  private static AuthorizationManager<RequestAuthorizationContext> allowOnlyLocalhost() {
      return (authentication, context) -> {
          String ip = context.getRequest().getRemoteAddr();

          boolean isLocal =
                  "127.0.0.1".equals(ip) ||  // IPv4 localhost
                  "::1".equals(ip);          // IPv6 localhost

          // true = permitir, false = denegar
          return new AuthorizationDecision(isLocal);
      };
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

      http
          // Opcional si la app es REST o no usas formularios
          //.csrf(csrf -> csrf.disable())


          // Reglas modernas de autorización
          .authorizeHttpRequests(auth -> auth
              // Solo permitir acceso desde localhost
              .requestMatchers("/commands").access(allowOnlyLocalhost())
              .requestMatchers(ApiEndpoints.Commands.BASE).access(allowOnlyLocalhost())
              .requestMatchers(ApiEndpoints.Commands.BASE+ApiEndpoints.Commands.BY_ID).access(allowOnlyLocalhost())
              .requestMatchers(ApiEndpoints.Commands.BASE+ApiEndpoints.Commands.QR_CODE).access(allowOnlyLocalhost())
              .requestMatchers(ApiEndpoints.Commands.BASE+ApiEndpoints.Commands.SERVER_ADDRESS).access(allowOnlyLocalhost())

              // Todo lo demás es público
              .anyRequest().permitAll()
          )


          // Deshabilitar auth por formulario y basic (no los necesitas)
          .httpBasic(basic -> basic.disable())
          .formLogin(form -> form.disable());

      return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
      return new InMemoryUserDetailsManager(); // sin usuarios → no se genera password
  }

=======
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			// ...
			.csrf(csrf -> csrf.disable()) // Deshabilita CSRF
			.requiresChannel(channel -> channel
				.anyRequest().requiresSecure()
			);
		return http.build();
	}
>>>>>>> d4e1e4c (Guardar cambios antes de sincronizar)

}
