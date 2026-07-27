#include "NativeInterfaceRegistry.h"

void CNativeInterfaceRegistry::Add(std::unique_ptr<INativeInterface>&& NativeInterface)
{
    const auto Iterator = std::ranges::find_if(m_NativeInterfaces, [&NativeInterface](const auto& NativeInterfacePointer)
    {
        return NativeInterfacePointer.get() == NativeInterface.get();
    });

    // Only add the interface instance if it was not found
    if (Iterator == m_NativeInterfaces.end())
        m_NativeInterfaces.push_back(std::move(NativeInterface));
}

bool CNativeInterfaceRegistry::RegisterAllInterfaces(JNIEnv* Environment)
{
    for (const auto& NativeInterface: m_NativeInterfaces)
    {
        const jclass JavaClass = Environment->FindClass(NativeInterface->GetJavaClassName());
        if (!JavaClass)
            return false;

        const std::vector<JNINativeMethod> Methods = NativeInterface->GetNativeMethods();
        if (Environment->RegisterNatives(JavaClass, Methods.data(), Methods.size()) != 0)
            return false;
    }

    return true;
}
