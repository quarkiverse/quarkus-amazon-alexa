package io.quarkus.amazon.lambda.alexa;

import java.util.List;

import io.quarkus.arc.deployment.IgnoreSplitPackageBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.IndexDependencyBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageProxyDefinitionBuildItem;

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

}
