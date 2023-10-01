package pub.carzy.validation;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import pub.carzy.validation.interfaces.ValidateChangeValue;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 */
@Configuration
public class ValidateBean {

    @Resource
    private List<ValidateChangeValue> changeValues;

    @Autowired
    public void setWebBindingInitializer(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        WebBindingInitializer initializer = requestMappingHandlerAdapter.getWebBindingInitializer();
        if (initializer != null) {
            ConfigurableWebBindingInitializer configurableWebBindingInitializer = new ConfigurableWebBindingInitializer() {
                @Override
                public void initBinder(WebDataBinder binder) {
                    super.initBinder(binder);
                    List<Validator> validators = new ArrayList<>();
                    validators.add(new Validator() {
                        @Override
                        public boolean supports(Class<?> clazz) {
                            return true;
                        }

                        @Override
                        public void validate(Object target, Errors errors) {
                            //进行扩展
                            changeValues.forEach(validateChangeValue -> validateChangeValue.change(target,errors));
                        }
                    });
                    validators.addAll(binder.getValidators());
                    binder.replaceValidators(validators.toArray(new Validator[]{}));
                }
            };
            BeanUtils.copyProperties(initializer, configurableWebBindingInitializer);
            requestMappingHandlerAdapter.setWebBindingInitializer(configurableWebBindingInitializer);
        }
    }
}
