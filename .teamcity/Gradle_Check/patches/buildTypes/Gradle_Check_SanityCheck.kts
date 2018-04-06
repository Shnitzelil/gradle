package Gradle_Check.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.GradleBuildStep
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with uuid = 'Gradle_Check_SanityCheck' (id = 'Gradle_Check_SanityCheck')
accordingly and delete the patch script.
*/
changeBuildType("Gradle_Check_SanityCheck") {
    expectSteps {
        gradle {
            name = "CLEAN_BUILD_SRC"
            tasks = "clean"
            buildFile = "build.gradle.kts"
            workingDir = "buildSrc"
            gradleParams = "-PmaxParallelForks=%maxParallelForks% -s --daemon --continue -I ./gradle/init-scripts/build-scan.init.gradle.kts -Djava7Home=%linux.jdk.for.gradle.compile%"
            useGradleWrapper = true
            gradleWrapperPath = ".."
        }
        gradle {
            name = "GRADLE_RUNNER"
            tasks = "clean compileAll sanityCheck"
            gradleParams = """-PmaxParallelForks=%maxParallelForks% -s --daemon --continue -I ./gradle/init-scripts/build-scan.init.gradle.kts -Djava7Home=%linux.jdk.for.gradle.compile% --build-cache "-Dgradle.cache.remote.url=%gradle.cache.remote.url%" "-Dgradle.cache.remote.username=%gradle.cache.remote.username%" "-Dgradle.cache.remote.password=%gradle.cache.remote.password%" -DenableCodeQuality=true"""
            useGradleWrapper = true
        }
        script {
            name = "CHECK_CLEAN_M2"
            executionMode = BuildStep.ExecutionMode.ALWAYS
            scriptContent = """
                REPO=%teamcity.agent.jvm.user.home%/.m2/repository
                if [ -e ${'$'}REPO ] ; then
                    tree ${'$'}REPO
                    rm -rf ${'$'}REPO
                    echo "${'$'}REPO was polluted during the build"
                    return 1
                else
                    echo "${'$'}REPO does not exist"
                fi
            """.trimIndent()
        }
        gradle {
            name = "VERIFY_TEST_FILES_CLEANUP"
            tasks = "verifyTestFilesCleanup"
            gradleParams = "-PmaxParallelForks=%maxParallelForks% -s --daemon --continue -I ./gradle/init-scripts/build-scan.init.gradle.kts -Djava7Home=%linux.jdk.for.gradle.compile%"
            useGradleWrapper = true
        }
        gradle {
            name = "TAG_BUILD"
            executionMode = BuildStep.ExecutionMode.ALWAYS
            tasks = "tagBuild"
            buildFile = "gradle/buildTagging.gradle"
            gradleParams = "-PmaxParallelForks=%maxParallelForks% -s --daemon --continue -I ./gradle/init-scripts/build-scan.init.gradle.kts -Djava7Home=%linux.jdk.for.gradle.compile% -PteamCityUsername=%teamcity.username.restbot% -PteamCityPassword=%teamcity.password.restbot% -PteamCityBuildId=%teamcity.build.id% -PgithubToken=%github.ci.oauth.token%"
            useGradleWrapper = true
        }
    }
    steps {
        update<GradleBuildStep>(1) {
            buildFile = ""
            param("org.jfrog.artifactory.selectedDeployableServer.defaultModuleVersionConfiguration", "GLOBAL")
        }
        update<GradleBuildStep>(3) {
            buildFile = ""
            param("org.jfrog.artifactory.selectedDeployableServer.defaultModuleVersionConfiguration", "GLOBAL")
        }
    }
}