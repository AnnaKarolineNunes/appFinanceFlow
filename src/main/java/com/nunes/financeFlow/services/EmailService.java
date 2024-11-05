package com.nunes.financeFlow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.url}")  // Adicionando a URL do sistema como variável de ambiente
    private String appUrl;

    /**
     * Envia um e-mail de verificação para o usuário.
     *
     * @param to    O e-mail do destinatário
     * @param token O token de verificação a ser enviado
     */
    public void sendVerificationEmail(String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Verificação de E-mail - FinanceFlow");
            message.setText(buildVerificationEmailBody(token));

            // Exibindo o token no console para facilitar os testes
            System.out.println("Token de verificação gerado: " + token);

            mailSender.send(message);
            logger.info("E-mail de verificação enviado para: {}", to);
        } catch (Exception e) {
            logEmailError(to, e);
        }
    }

    /**
     * Constrói o corpo do e-mail de verificação, incluindo o link com o token.
     *
     * @param token O token de verificação a ser incluído no link
     * @return Corpo do e-mail de verificação
     */
    private String buildVerificationEmailBody(String token) {
        return "Por favor, verifique seu e-mail clicando no link abaixo:\n" +
                appUrl + "/auth/verify-email?token=" + token + "\n" +
                "Este link expira em 24 horas.";
    }

    /**
     * Centraliza os logs de erro de envio de e-mail para auditoria posterior.
     *
     * @param to  O e-mail do destinatário
     * @param e   A exceção que ocorreu durante o envio do e-mail
     */
    private void logEmailError(String to, Exception e) {
        logger.error("Erro ao enviar e-mail para {}: {}", to, e.getMessage());
        // Aqui, poderíamos também registrar esses logs em um banco de dados ou ferramenta de auditoria.
    }
}
