#pragma once

#include "Core/NativeInterface.h"

class CSteamAPI : public INativeInterface
{
public:
    ~CSteamAPI() override = default;
    
    const char* GetJavaClassName() const override;
    std::vector<JNINativeMethod> GetNativeMethods() const override;
private:
    static jint JNICALL SteamAPI_InitEx_Native(JNIEnv* Environment, jclass, jobject OutErrorMessage);
    static void JNICALL SteamAPI_RunCallbacks_Native(JNIEnv*, jclass);
    static void JNICALL SteamAPI_Shutdown_Native(JNIEnv*, jclass);
    
    template<typename InitializationFunction>
    static jint RunSteamInitialization(JNIEnv* Environment, jobject OutErrorMessage, InitializationFunction InitFunction);
};
