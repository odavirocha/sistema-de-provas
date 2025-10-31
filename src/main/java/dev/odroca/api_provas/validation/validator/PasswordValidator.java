package dev.odroca.api_provas.validation.validator;

import dev.odroca.api_provas.validation.annotation.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password == null) return false;

        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasDown = password.matches(".*[a-z].*");
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");
        boolean size = (password.length() >= 6 && password.length() <= 16);

        if (!hasUpper || !hasDown || !hasSpecial || !size) {

            context.disableDefaultConstraintViolation();
            
            if (!hasUpper)
                context.buildConstraintViolationWithTemplate("A senha deve ter pelo menos uma letra MAIÚSCULA!").addConstraintViolation();
        
            if (!hasDown)
                context.buildConstraintViolationWithTemplate("A senha deve ter pelo menos uma letra MINÚSCULA!").addConstraintViolation();
            
            if (!hasSpecial)
                context.buildConstraintViolationWithTemplate("A senha deve ter pelo menos um caractere especial!").addConstraintViolation();
            
            if (!size)
                context.buildConstraintViolationWithTemplate("A senha deve ter entre  6 e 16 caracteres!").addConstraintViolation();

            return false;
        }

        return true;
    }
    
}
