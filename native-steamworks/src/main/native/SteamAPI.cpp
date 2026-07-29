#include "SteamAPI.h"
#include "Core/NativeInterfaceRegistry.h"

#include <steam/steam_api.h>

const char* CSteamAPI::GetJavaClassName() const
{
    return "me/ayydxn/teleidoscope/steamworks/SteamAPI$NativeMethods";
}

std::vector<JNINativeMethod> CSteamAPI::GetNativeMethods() const
{
    return
    {
        JNI_METHOD("nSteamAPI_InitEx", "(Ljava/lang/StringBuilder;)I", CSteamAPI::SteamAPI_InitEx_Native),
        JNI_METHOD("nSteamAPI_RunCallbacks", "()V", CSteamAPI::SteamAPI_RunCallbacks_Native),
        JNI_METHOD("nSteamAPI_Shutdown", "()V", CSteamAPI::SteamAPI_Shutdown_Native)
    };
}

/*
 * Class:     me_ayydxn_teleidoscope_steamworks_SteamAPI_NativeMethods
 * Method:    nSteamAPI_InitEx
 * Signature: (Ljava/lang/StringBuilder;)I
 */
jint CSteamAPI::SteamAPI_InitEx_Native(JNIEnv* Environment, jclass, const jobject OutErrorMessage)
{
    return RunSteamInitialization(Environment, OutErrorMessage, [](SteamErrMsg* ErrorMessage)
    {
        return SteamAPI_InitEx(ErrorMessage);
    });
}

/*
 * Class:     me_ayydxn_teleidoscope_steamworks_SteamAPI_NativeMethods
 * Method:    nSteamAPI_RunCallbacks
 * Signature: ()V
 */
void CSteamAPI::SteamAPI_RunCallbacks_Native(JNIEnv*, jclass)
{
    SteamAPI_RunCallbacks();
}

/*
 * Class:     me_ayydxn_teleidoscope_steamworks_SteamAPI_NativeMethods
 * Method:    nSteamAPI_Shutdown
 * Signature: ()V
 */
void CSteamAPI::SteamAPI_Shutdown_Native(JNIEnv*, jclass)
{
    SteamAPI_Shutdown();
}

template<typename InitializationFunction>
jint CSteamAPI::RunSteamInitialization(JNIEnv* Environment, jobject OutErrorMessage, InitializationFunction InitFunction)
{
    SteamErrMsg ErrorMessage = {};
    const ESteamAPIInitResult SteamInitResult = InitFunction(&ErrorMessage);

    if (SteamInitResult != k_ESteamAPIInitResult_OK && OutErrorMessage != nullptr)
    {
        const jclass StringBuilderClass = Environment->GetObjectClass(OutErrorMessage);
        const jmethodID AppendMethod = Environment->GetMethodID(StringBuilderClass, "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");

        if (AppendMethod != nullptr)
        {
            const jstring JavaErrorMessage = Environment->NewStringUTF(ErrorMessage);
            
            Environment->CallObjectMethod(OutErrorMessage, AppendMethod, JavaErrorMessage);
            Environment->DeleteLocalRef(JavaErrorMessage);
        }

        Environment->DeleteLocalRef(StringBuilderClass);
    }

    return SteamInitResult;
}
