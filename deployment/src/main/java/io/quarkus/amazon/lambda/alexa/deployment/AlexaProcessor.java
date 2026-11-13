package io.quarkus.amazon.lambda.alexa.deployment;

import java.util.List;

import io.quarkus.arc.deployment.IgnoreSplitPackageBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.IndexDependencyBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageProxyDefinitionBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.deployment.builditem.nativeimage.RuntimeInitializedClassBuildItem;

public class AlexaProcessor {

    private static final String FEATURE = "amazon-alexa";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void addDependencies(BuildProducer<IndexDependencyBuildItem> indexDependency) {
        indexDependency.produce(new IndexDependencyBuildItem("com.amazon.alexa", "ask-sdk"));
        indexDependency.produce(new IndexDependencyBuildItem("com.amazon.alexa", "ask-sdk-runtime"));
        indexDependency.produce(new IndexDependencyBuildItem("com.amazon.alexa", "ask-sdk-model"));
        indexDependency.produce(new IndexDependencyBuildItem("com.amazon.alexa", "ask-sdk-lambda-support"));
        indexDependency.produce(new IndexDependencyBuildItem("com.amazon.alexa", "ask-sdk-servlet-support"));
        indexDependency.produce(new IndexDependencyBuildItem("com.amazon.alexa", "ask-sdk-dynamodb-persistence-adapter"));
        indexDependency.produce(new IndexDependencyBuildItem("com.amazon.alexa", "ask-sdk-apache-client"));
        indexDependency.produce(new IndexDependencyBuildItem("com.amazon.alexa", "ask-sdk-model-runtime"));
    }

    @BuildStep
    NativeImageProxyDefinitionBuildItem httpProxies() {
        return new NativeImageProxyDefinitionBuildItem("org.apache.http.conn.HttpClientConnectionManager",
                "org.apache.http.pool.ConnPoolControl", "com.amazonaws.http.conn.Wrapped");
    }

    @BuildStep
    IgnoreSplitPackageBuildItem ignoreSplitPackages() {
        return new IgnoreSplitPackageBuildItem(
                List.of("com.amazon.ask.builder", "com.amazon.ask.model.services", "com.amazon.ask"));
    }

    @BuildStep
    void runtimeInitializedClasses(BuildProducer<RuntimeInitializedClassBuildItem> runtimeInitializedClasses) {
        runtimeInitializedClasses
                .produce(new RuntimeInitializedClassBuildItem(com.amazonaws.retry.PredefinedBackoffStrategies.class.getName()));
        runtimeInitializedClasses
                .produce(new RuntimeInitializedClassBuildItem(com.amazonaws.retry.PredefinedRetryPolicies.class.getName()));
        runtimeInitializedClasses
                .produce(new RuntimeInitializedClassBuildItem(com.amazonaws.ClientConfiguration.class.getName()));
    }

    @BuildStep
    void reflection(BuildProducer<ReflectiveClassBuildItem> reflectiveClasses) {
        reflectiveClasses.produce(
                ReflectiveClassBuildItem.builder(com.amazonaws.auth.AWS4Signer.class.getName()).methods().fields().build());

        reflectiveClasses.produce(
                ReflectiveClassBuildItem.builder(com.amazonaws.partitions.model.CredentialScope.class.getName()).methods()
                        .fields().build());
        reflectiveClasses
                .produce(ReflectiveClassBuildItem.builder(com.amazonaws.partitions.model.Endpoint.class.getName()).methods()
                        .fields().build());
        reflectiveClasses
                .produce(ReflectiveClassBuildItem.builder(com.amazonaws.partitions.model.Partition.class.getName()).methods()
                        .fields().build());
        reflectiveClasses
                .produce(ReflectiveClassBuildItem.builder(com.amazonaws.partitions.model.Partitions.class.getName()).methods()
                        .fields().build());
        reflectiveClasses
                .produce(ReflectiveClassBuildItem.builder(com.amazonaws.partitions.model.Region.class.getName()).methods()
                        .fields().build());
        reflectiveClasses
                .produce(ReflectiveClassBuildItem.builder(com.amazonaws.partitions.model.Service.class.getName()).methods()
                        .fields().build());
        reflectiveClasses
                .produce(ReflectiveClassBuildItem
                        .builder(com.amazonaws.services.dynamodbv2.model.ResourceInUseException.class.getName()).methods()
                        .fields().build());
        reflectiveClasses
                .produce(ReflectiveClassBuildItem
                        .builder(com.fasterxml.jackson.databind.exc.InvalidDefinitionException.class.getName()).methods()
                        .fields().build());
    }

    @BuildStep
    void resources(BuildProducer<NativeImageResourceBuildItem> nativeImageResources) {
        nativeImageResources.produce(new NativeImageResourceBuildItem("com/amazonaws/partitions/endpoints.json"));
    }
}
