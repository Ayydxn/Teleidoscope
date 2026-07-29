#include "SteamAPI.h"
#include "Core/NativeInterfaceRegistry.h"

#include <jni.h>

extern "C" JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void*)
{
    JNIEnv* Environment = nullptr;
    if (vm->GetEnv(reinterpret_cast<void**>(&Environment), JNI_VERSION_10) != JNI_OK)
        return JNI_ERR;
    
    CNativeInterfaceRegistry::Add(std::make_unique<CSteamAPI>());

    if (!CNativeInterfaceRegistry::RegisterAllInterfaces(Environment))
        return JNI_ERR;

    return JNI_VERSION_10;
}

extern "C" JNIEXPORT void JNI_OnUnload(JavaVM* vm, void*)
{
    JNIEnv* Environment = nullptr;
    if (vm->GetEnv(reinterpret_cast<void**>(&Environment), JNI_VERSION_10) != JNI_OK)
        return;

    // TODO: (Ayydxn) In the future once we start storing global/persistent JNI state like class IDs, clean that stuff up here.
}
