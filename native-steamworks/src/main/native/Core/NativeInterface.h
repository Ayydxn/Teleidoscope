#pragma once

#include <jni.h>

#include <vector>

class INativeInterface
{
public:
    virtual ~INativeInterface() = default;

    virtual const char* GetJavaClassName() const = 0;
    virtual std::vector<JNINativeMethod> GetNativeMethods() const = 0;
};
