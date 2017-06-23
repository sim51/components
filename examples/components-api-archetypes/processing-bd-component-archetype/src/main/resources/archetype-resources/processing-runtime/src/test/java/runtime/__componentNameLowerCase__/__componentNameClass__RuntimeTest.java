package ${package}.runtime.${componentNameLowerCase};

import org.apache.avro.Schema;
import org.apache.avro.generic.IndexedRecord;
import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.values.PCollection;
import org.junit.Test;
import org.mockito.Mockito;
import org.talend.components.adapter.beam.BeamJobContext;
import org.talend.components.adapter.beam.coders.LazyAvroCoder;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.daikon.avro.GenericDataRecordHelper;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.properties.ValidationResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ${componentNameClass}RuntimeTest {

    private final ${componentNameClass}Runtime ${componentNameLowerCase}Runtime = new ${componentNameClass}Runtime();

    /**
     * Check {@link ${componentNameClass}Runtime#initialize(RuntimeContainer, Properties)}
     * returns //TODO
     */
    @Test
    public void testInitialize() {
        ValidationResult result = ${componentNameLowerCase}Runtime.initialize(null, null);
        assertEquals(ValidationResult.OK, result);
    }

    /**
     * Check {@link ${componentNameClass}Runtime#build(BeamJobContext)}
     * returns
     */
    @Test
    public void testBuild() {

        // Create pipeline
        PipelineOptions options = PipelineOptionsFactory.create();
        options.setRunner(DirectRunner.class);
        final Pipeline p = Pipeline.create(options);

        // Create PCollection for test
        Schema a = GenericDataRecordHelper.createSchemaFromObject("a", new Object[] { "a" });
        IndexedRecord irA = GenericDataRecordHelper.createRecord(a, new Object[] { "a" });
        IndexedRecord irB = GenericDataRecordHelper.createRecord(a, new Object[] { "b" });
        IndexedRecord irC = GenericDataRecordHelper.createRecord(a, new Object[] { "c" });
        /*
        *   Insert your test here
         */
    }
}
