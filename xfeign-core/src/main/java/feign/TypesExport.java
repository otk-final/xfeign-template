package feign;

import java.lang.reflect.Type;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/25 下午8:25
 * Created by huangxy
 */
public final class TypesExport {

    public static Type resolve(Type context, Class<?> contextRawType, Type toResolve) {
        return Types.resolve(context, contextRawType, toResolve);
    }
}
