package host.anzo.gradle.ecj

@Suppress("MayBeConstant")
public object ECJConstants {
    public val ECJ_CONFIGURATION_NAME: String = "ecj"

    public val DEFAULT_DEPENDENCY_GROUP: String = "org.eclipse.jdt"
    public val DEFAULT_DEPENDENCY_ARTIFACT: String = "ecj"
    public val DEFAULT_DEPENDENCY_VERSION: String = "3.38.0"

    public val MAIN: String = "org.eclipse.jdt.internal.compiler.batch.Main"

    /* The version for which a toolchain is requested if the project's toolchain is not compatible. */
    public val PREFERRED_JAVA_VERSION: Int = 22

    /* The version required to run ECJ. */
    public val REQUIRED_JAVA_VERSION: Int = 22
}