package host.anzo.gradle.ecj

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

public abstract class ECJExtension @Inject constructor(objects: ObjectFactory) {
    public val compilerGroupId: Property<String> = objects.property(String::class.java).convention(ECJConstants.DEFAULT_DEPENDENCY_GROUP)
    public val compilerArtifactId: Property<String> = objects.property(String::class.java).convention(ECJConstants.DEFAULT_DEPENDENCY_ARTIFACT)
    public val compilerVersion: Property<String> = objects.property(String::class.java).convention(ECJConstants.DEFAULT_DEPENDENCY_VERSION)

    init {
        compilerGroupId.finalizeValueOnRead()
        compilerArtifactId.finalizeValueOnRead()
        compilerVersion.finalizeValueOnRead()
    }
}