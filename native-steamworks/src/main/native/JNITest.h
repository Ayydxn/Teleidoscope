#pragma once

#include "Core/NativeInterface.h"

class CJNITest : public INativeInterface
{
public:
    ~CJNITest() override = default;

    const char* GetJavaClassName() const override;
    std::vector<JNINativeMethod> GetNativeMethods() const override;
private:
    static void JNICALL Ping_Native(JNIEnv* Environment, jclass);
};
