package org.springframework.web.servlet.mvc.method.annotation;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.manyun.comm.api.model.LoginPhoneForm;
import com.manyun.common.core.annotation.RequestBodyRsa;
import com.manyun.common.core.domain.CodeStatus;
import com.manyun.common.core.domain.RequestTimeRate;
import com.manyun.common.core.exception.ServiceException;
import com.manyun.common.security.config.GlobalRsaConfig;
import com.manyun.common.security.domain.dto.CommDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.ValidationAnnotationUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/*
 * 加解密参数解析器过程
 *
 * @author yanwei
 * @create 2022-09-05
 */
@Slf4j
@Component
public class RsaRequestBodyProcessor implements HandlerMethodArgumentResolver, EmbeddedValueResolverAware {

    private GlobalRsaConfig globalRsaConfig;
    private StringValueResolver resolver;

    public RsaRequestBodyProcessor(GlobalRsaConfig globalRsaConfig) {
        this.globalRsaConfig = globalRsaConfig;
    }

    private boolean isOpen(MethodParameter parameter) {
       //     RequestBodyRsa requestBodyRsa =   parameter.getParameterAnnotation( RequestBodyRsa.class);
/*        DependencyDescriptor dd1 = new DependencyDescriptor(requestBodyRsa.getClass().getField("enable"), false);
        String value = new ContextAnnotationAutowireCandidateResolver().getSuggestedValue(dd1).toString();
        String resolvePlaceholders = SpringUtil.getApplicationContext().*/
        return parameter.hasParameterAnnotation(RequestBodyRsa.class)  &&  Boolean.parseBoolean(resolver.resolveStringValue(parameter.getParameterAnnotation(RequestBodyRsa.class).enable()));
    }

    @SneakyThrows
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBodyRsa.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
       // 解析过程
        if (!mavContainer.isRequestHandled()) {
            mavContainer.setRequestHandled(true);
            Object oblData = null;
            oblData = jsr303Check(isOpen(parameter) ? doDyd(parameter, webRequest) : doDefaultDyd(parameter, webRequest), parameter, mavContainer, webRequest, binderFactory);
            if (oblData instanceof RequestTimeRate){
                RequestTimeRate requestTimeRate = (RequestTimeRate) oblData;
                Assert.isTrue(requestTimeRate.getRequestTime() != 0,"请求时间有误！");
                long between = DateTime.of(requestTimeRate.getRequestTime()).between(new Date(), DateUnit.SECOND);
                Assert.isTrue(between <= 2,"有效期已过,请重新获取时间戳!有效时间为{}秒.",2);
            }
            return oblData;
        }
        return null;
    }


    private Object doDefaultDyd(MethodParameter parameter, NativeWebRequest webRequest) throws IOException {
        return JSONUtil.toBean(IOUtils.toString(webRequest.getNativeRequest(HttpServletRequest.class).getInputStream(), StandardCharsets.UTF_8), parameter.getParameter().getType());
    }

    private Object doDyd(MethodParameter parameter,  NativeWebRequest webRequest) throws IOException {
        postCheck(parameter);
            String data =  checkRsaDyd(webRequest);
            String decryptStr = null;
            try {
                decryptStr = SecureUtil.rsa(globalRsaConfig.getPrivateKey(),globalRsaConfig.getPublicKey() ).decryptStr(data, KeyType.PrivateKey, StandardCharsets.UTF_8);
                return  JSONUtil.toBean(decryptStr, parameter.getParameter().getType());
        }catch (Exception e){
            throw  new ServiceException(globalRsaConfig.getErrorMsg(), CodeStatus.ILLEGAL_PARAMETER.getCode());
        }
    }


    private String checkRsaDyd(NativeWebRequest webRequest) throws IOException {
        long requestTime = globalRsaConfig.getRequestTime().getSeconds();
        HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String body = IOUtils.toString(nativeRequest.getInputStream(), StandardCharsets.UTF_8);
        CommDto commDto = JSONUtil.toBean(body, CommDto.class);
        Assert.isTrue(StrUtil.isNotBlank(
                        commDto.getData())
                        && StrUtil.isNotBlank(commDto.getProjectName()) && Objects.nonNull(commDto.getTime())
                        && Arrays.stream(globalRsaConfig.getProjectNames()).filter(item -> item.equals(commDto.getProjectName())).count() >= 1,
                "格式有误,请核实!");
        long between = DateTime.of(commDto.getTime()).between(new Date(), DateUnit.SECOND);
        Assert.isTrue(between <= requestTime,"有效期已过,请重新获取时间戳!有效时间为{}秒.",requestTime);
        return commDto.getData();
    }

    /**
     * 校验
     * @param parameter
     */
    private void postCheck(MethodParameter parameter) {
        Map<String, Object> annotationAttributes = AnnotationUtils.getAnnotationAttributes(parameter.getMethodAnnotation(RequestMapping.class));
        RequestMethod[] requestMethods = (RequestMethod[]) annotationAttributes.get("method");
        Assert.isTrue(Arrays.stream(requestMethods).filter(item -> RequestMethod.POST.name().equals(item.name()) ).count() >=1,"not find RequestMethod.POST! ");

    }

    private Object jsr303Check(Object targetRequestBody,MethodParameter parameter, ModelAndViewContainer mavContainer,NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Assert.isTrue(Objects.nonNull(targetRequestBody),"body is null ?");
        String name = Conventions.getVariableNameForParameter(parameter);
        if (binderFactory != null) {
            WebDataBinder binder = binderFactory.createBinder(webRequest, targetRequestBody, name);
            if (targetRequestBody != null) {
                validateIfApplicable(binder, parameter);
                if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(parameter)) {
                    throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
                }
            }
            if (mavContainer != null) {
                mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
            }
        }
        return targetRequestBody;
    }

    private void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        Annotation[] annotations = parameter.getParameterAnnotations();
        for (Annotation ann : annotations) {
            Object[] validationHints = ValidationAnnotationUtils.determineValidationHints(ann);
            if (validationHints != null) {
                binder.validate(validationHints);
                break;
            }
        }
    }

    private boolean isBindExceptionRequired(MethodParameter parameter) {
        int i = parameter.getParameterIndex();
        Class<?>[] paramTypes = parameter.getExecutable().getParameterTypes();
        boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
        return !hasBindingResult;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
       this.resolver = resolver;
    }
}
