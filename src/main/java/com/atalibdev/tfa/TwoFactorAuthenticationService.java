package com.atalibdev.tfa;


import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
@Slf4j
public class TwoFactorAuthenticationService {
    public String generateTheNewSecrete() {
        return new DefaultSecretGenerator().generate();
    }
    public String generateQRCodeImageUrl(String secret) {
        QrData data = new QrData.Builder()
                .label("Atalibdev 2FA")
                .secret(secret)
                .issuer("Atalib-dev")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();
        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = new byte[0];
        try {
            imageData = generator.generate(data);
        } catch (QrGenerationException e) {
            log.error("Error while generating the data");
        }
        return getDataUriForImage(imageData, generator.getImageMimeType());
    }
    public boolean isOtpValid(String secret, String code) {
        TimeProvider provider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier codeVerifier = new DefaultCodeVerifier(codeGenerator, provider);
        return codeVerifier.isValidCode(secret, code);
    }
    public boolean isOptNotValid(String secret, String code) {
        return !this.isOtpValid(secret, code);
    }
}




















