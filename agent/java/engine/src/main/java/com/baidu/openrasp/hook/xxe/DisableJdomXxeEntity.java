/*
 * Copyright 2017-2019 Baidu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baidu.openrasp.hook.xxe;

import com.baidu.openrasp.HookHandler;
import com.baidu.openrasp.cloud.model.ErrorType;
import com.baidu.openrasp.cloud.utils.CloudUtils;
import com.baidu.openrasp.tool.Reflection;
import com.baidu.openrasp.tool.annotation.HookAnnotation;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.IOException;

/**
 * @description: 关闭jdom的XXE entity
 * @author: anyang
 * @create: 2019/04/30 15:10
 */
@HookAnnotation
public class DisableJdomXxeEntity extends DisableXxeEntity {
    @Override
    public boolean isClassMatched(String className) {
        return "org/jdom/input/SAXBuilder".equals(className)
                || "org/jdom2/input/SAXBuilder".equals(className);
    }

    @Override
    protected void hookMethod(CtClass ctClass) throws IOException, CannotCompileException, NotFoundException {
        String src = getInvokeStaticSrc(DisableJdomXxeEntity.class, "setFeature", "$0", Object.class);
        insertBefore(ctClass, "build", null, src);
    }

    public static void setFeature(Object builder) {
        if (HookHandler.requestCache.get() != null) {
            String action = getAction();
            if (BLOCK_XXE_DISABLE_ENTITY.equals(action) && getStatus("java_jdom")) {
                try {
                    Reflection.invokeMethod(builder, "setFeature", new Class[]{String.class, boolean.class}, FEATURE, true);
                } catch (Exception e) {
                    String message = "Jdom close xxe entity failed";
                    int errorCode = ErrorType.HOOK_ERROR.getCode();
                    HookHandler.LOGGER.warn(CloudUtils.getExceptionObject(message, errorCode), e);
                }
            }
        }
    }

}
