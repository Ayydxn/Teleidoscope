#include "JNITest.h"
#include "Core/NativeInterfaceRegistry.h"

#include <iostream>

const char* CJNITest::GetJavaClassName() const
{
    return "me/ayydxn/teleidoscope/steamworks/JNITest";
}

std::vector<JNINativeMethod> CJNITest::GetNativeMethods() const
{
    return
    {
        JNI_METHOD("ping", "()V", CJNITest::Ping_Native)
    };
}

/*
 * Class:     me_ayydxn_teleidoscope_steamworks_JNITest
 * Method:    ping
 * Signature: ()V
 */
void CJNITest::Ping_Native(JNIEnv*, jclass)
{
    std::cout << "Pinged Steamworks!" << std::endl;
}
