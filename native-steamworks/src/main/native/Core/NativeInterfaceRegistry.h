#pragma once

#include "NativeInterface.h"

#include <memory>
#include <vector>

#define JNI_METHOD(Name, MethodSignature, ImplFunctionPointer) { (char*) (Name), (char*) (MethodSignature), (void*) (ImplFunctionPointer) }
#define JNI_METHODS_AND_COUNT(Methods) Methods, std::size(Methods)

class CNativeInterfaceRegistry
{
public:
    static void Add(std::unique_ptr<INativeInterface>&& NativeInterface);
    static bool RegisterAllInterfaces(JNIEnv* Environment);
private:
    inline static std::vector<std::unique_ptr<INativeInterface>> m_NativeInterfaces;
};
