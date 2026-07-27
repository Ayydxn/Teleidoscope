cmake_minimum_required(VERSION 3.20)
project(TeleidoscopeSteamworks CXX)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# ── Locate JNI ───────────────────────────────────────────────────────────────
find_package(JNI COMPONENTS JVM REQUIRED)

# ── Paths passed in from Gradle ──────────────────────────────────────────────
set(STEAMWORKS_INCLUDE "${STEAMWORKS_SDK}/public")

# ── Sources ──────────────────────────────────────────────────────────────────
file(GLOB_RECURSE SOURCES CONFIGURE_DEPENDS "*.cpp" "*.h")

add_library(TeleidoscopeSteamworks SHARED ${SOURCES})

target_include_directories(TeleidoscopeSteamworks PRIVATE
        ${CMAKE_SOURCE_DIR}
        ${JNI_INCLUDE_DIRS}
        ${STEAMWORKS_INCLUDE}
        include
)

target_link_libraries(TeleidoscopeSteamworks PRIVATE
        ${JNI_LIBRARIES}
)

# --- Output naming -------------------------------------------------------------
if(NOT PLATFORM)
    message(WARNING "PLATFORM was not set by the caller; native output naming will fall back "
            "to CMake's own host detection, which may not match what Gradle expects.")
endif()

if(PLATFORM STREQUAL "windows-x64")
    set_target_properties(TeleidoscopeSteamworks PROPERTIES
            OUTPUT_NAME "TeleidoscopeSteamworks-Win64"
            PREFIX ""
            SUFFIX ".dll"
    )
elseif(PLATFORM STREQUAL "macos-x64" OR PLATFORM STREQUAL "macos-arm64")
    set_target_properties(TeleidoscopeSteamworks PROPERTIES
            OUTPUT_NAME "TeleidoscopeSteamworks-Mac"
            PREFIX "lib"
            SUFFIX ".dylib"
    )
    if(PLATFORM STREQUAL "macos-arm64")
        set_target_properties(TeleidoscopeSteamworks PROPERTIES OSX_ARCHITECTURES "arm64")
    else()
        set_target_properties(TeleidoscopeSteamworks PROPERTIES OSX_ARCHITECTURES "x86_64")
    endif()
elseif(PLATFORM STREQUAL "linux-x64")
    set_target_properties(TeleidoscopeSteamworks PROPERTIES
            OUTPUT_NAME "TeleidoscopeSteamworks-Linux"
            PREFIX "lib"
            SUFFIX ".so"
    )
else()
    # PLATFORM missing/unrecognized — fall back to host detection so a local
    # `cmake --build` still produces something usable, per-platform naming
    # just isn't guaranteed to line up with what Gradle's compileNative expects.
    if(WIN32)
        set_target_properties(TeleidoscopeSteamworks PROPERTIES OUTPUT_NAME "TeleidoscopeSteamworks-Win64" PREFIX "" SUFFIX ".dll")
    elseif(APPLE)
        set_target_properties(TeleidoscopeSteamworks PROPERTIES OUTPUT_NAME "TeleidoscopeSteamworks-Mac" PREFIX "lib" SUFFIX ".dylib")
    else()
        set_target_properties(TeleidoscopeSteamworks PROPERTIES OUTPUT_NAME "TeleidoscopeSteamworks-Linux" PREFIX "lib" SUFFIX ".so")
    endif()
endif()
