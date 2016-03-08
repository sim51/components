// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.components.api.service.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingResult;
import org.apache.maven.model.building.ModelSource;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectModelResolver;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.collection.DependencySelector;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.internal.impl.DefaultRemoteRepositoryManager;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.util.graph.selector.AndDependencySelector;
import org.eclipse.aether.util.graph.selector.ExclusionDependencySelector;
import org.eclipse.aether.util.graph.selector.OptionalDependencySelector;
import org.eclipse.aether.util.graph.selector.ScopeDependencySelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.Constants;
import org.talend.components.api.TopLevelDefinition;
import org.talend.components.api.component.ComponentDefinition;
import org.talend.components.api.component.ComponentImageType;
import org.talend.components.api.exception.ComponentException;
import org.talend.components.api.exception.error.ComponentsApiErrorCode;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.service.ComponentService;
import org.talend.components.api.wizard.ComponentWizard;
import org.talend.components.api.wizard.ComponentWizardDefinition;
import org.talend.components.api.wizard.WizardImageType;
import org.talend.daikon.exception.ExceptionContext;
import org.talend.daikon.properties.service.PropertiesServiceImpl;

/**
 * Main Component Service implementation that is not related to any framework (neither OSGI, nor Spring) it uses a
 * ComponentRegistry implementation that will be provided by framework specific Service classes
 */
public class ComponentServiceImpl extends PropertiesServiceImpl<ComponentProperties>implements ComponentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentServiceImpl.class);

    private Map<Artifact, Set<Dependency>> dependenciesCache = new HashMap<>();

    private ComponentRegistry componentRegistry;

    private ModelBuilder modelBuilder;

    public ComponentServiceImpl(ComponentRegistry componentRegistry) {
        this.componentRegistry = componentRegistry;
    }

    @Override
    public Set<String> getAllComponentNames() {
        // remove the components# internal prefix to return the simple name
        Collection<String> componentsInternalNames = componentRegistry.getComponents().keySet();
        Set<String> compNames = new HashSet<>(componentsInternalNames.size());
        for (String name : componentsInternalNames) {
            compNames.add(name.substring(Constants.COMPONENT_BEAN_PREFIX.length()));
        }
        return compNames;
    }

    @Override
    public Set<ComponentDefinition> getAllComponents() {
        return new HashSet<>(componentRegistry.getComponents().values());
    }

    @Override
    public Set<ComponentWizardDefinition> getTopLevelComponentWizards() {
        Set<ComponentWizardDefinition> defs = new HashSet<>();
        for (ComponentWizardDefinition def : componentRegistry.getComponentWizards().values()) {
            if (def.isTopLevel()) {
                defs.add(def);
            }
        }
        return defs;
    }

    @Override
    public ComponentProperties getComponentProperties(String name) {
        ComponentDefinition compDef = getComponentDefinition(name);
        ComponentProperties properties = compDef.createProperties();
        return properties;
    }

    @Override
    public ComponentDefinition getComponentDefinition(String name) {
        final String beanName = Constants.COMPONENT_BEAN_PREFIX + name;
        ComponentDefinition compDef = componentRegistry.getComponents().get(beanName);
        if (compDef == null) {
            throw new ComponentException(ComponentsApiErrorCode.WRONG_COMPONENT_NAME, ExceptionContext.build().put("name", name)); //$NON-NLS-1$
        } // else got the def so use it
        return compDef;
    }

    @Override
    public ComponentWizard getComponentWizard(String name, String location) {
        final String beanName = Constants.COMPONENT_WIZARD_BEAN_PREFIX + name;
        ComponentWizardDefinition wizardDefinition = componentRegistry.getComponentWizards().get(beanName);
        if (wizardDefinition == null) {
            throw new ComponentException(ComponentsApiErrorCode.WRONG_WIZARD_NAME, ExceptionContext.build().put("name", name)); //$NON-NLS-1$
        }
        ComponentWizard wizard = wizardDefinition.createWizard(location);
        return wizard;
    }

    @Override
    public List<ComponentWizard> getComponentWizardsForProperties(ComponentProperties properties, String location) {
        List<ComponentWizard> wizards = new ArrayList<>();
        for (ComponentWizardDefinition wizardDefinition : componentRegistry.getComponentWizards().values()) {
            if (wizardDefinition.supportsProperties(properties.getClass())) {
                ComponentWizard wizard = wizardDefinition.createWizard(properties, location);
                wizards.add(wizard);
            }
        }
        return wizards;
    }

    @Override
    public List<ComponentDefinition> getPossibleComponents(ComponentProperties... properties) {
        List<ComponentDefinition> returnList = new ArrayList<>();
        for (ComponentDefinition cd : componentRegistry.getComponents().values()) {
            if (cd.supportsProperties(properties)) {
                returnList.add(cd);
            }
        }
        return returnList;
    }

    @Override
    public InputStream getWizardPngImage(String wizardName, WizardImageType imageType) {
        ComponentWizardDefinition wizardDefinition = componentRegistry.getComponentWizards()
                .get(Constants.COMPONENT_WIZARD_BEAN_PREFIX + wizardName);
        if (wizardDefinition != null) {
            return getImageStream(wizardDefinition, wizardDefinition.getPngImagePath(imageType));
        } else {
            throw new ComponentException(ComponentsApiErrorCode.WRONG_WIZARD_NAME,
                    ExceptionContext.build().put("name", wizardName)); //$NON-NLS-1$
        }

    }

    @Override
    public InputStream getComponentPngImage(String componentName, ComponentImageType imageType) {
        ComponentDefinition componentDefinition = componentRegistry.getComponents()
                .get(Constants.COMPONENT_BEAN_PREFIX + componentName);
        if (componentDefinition != null) {
            return getImageStream(componentDefinition, componentDefinition.getPngImagePath(imageType));
        } else {
            throw new ComponentException(ComponentsApiErrorCode.WRONG_COMPONENT_NAME,
                    ExceptionContext.build().put("name", componentName)); //$NON-NLS-1$
        }
    }

    /**
     * get the image stream or null
     * 
     * @param definition, must not be null
     * @return the stream or null if no image was defined for th component or the path is wrong
     */
    private InputStream getImageStream(TopLevelDefinition definition, String pngIconPath) {
        InputStream result = null;
        if (pngIconPath != null && !"".equals(pngIconPath)) { //$NON-NLS-1$
            InputStream resourceAsStream = definition.getClass().getResourceAsStream(pngIconPath);
            if (resourceAsStream == null) {// no resource found so this is an component error, so log it and return
                                           // null
                LOGGER.error("Failed to load the Wizard icon [" + definition.getName() + "," + pngIconPath + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            } else {
                result = resourceAsStream;
            }
        } else {// no path provided so will return null but log it.
            LOGGER.warn("The defintion of [" + definition.getName() + "] did not specify any icon"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return result;
    }

    @Override
    public Set<String> getMavenUriDependencies(String componentName) {
        ComponentDefinition componentDef = getComponentDefinition(componentName);
        String mavenGroupId = componentDef.getMavenGroupId();
        String mavenArtifactId = componentDef.getMavenArtifactId();
        try {
            return getDesignTimeDependencies(mavenGroupId, mavenArtifactId, componentDef.getClass().getClassLoader());
        } catch (IOException e) {
            throw new ComponentException(ComponentsApiErrorCode.COMPUTE_DEPENDENCIES_FAILED, e, ExceptionContext.withBuilder()
                    .put("path", computeDesignDependenciesPath(mavenGroupId, mavenArtifactId)).build());
        }
    }

    /**
     * this will locate the file META-INF/mavenGroupId/mavenArtifactId/depenencies.properties and parse it to extract
     * the design time dependencies of the component.
     * 
     * @param mavenGroupId group id of the component to locate the dep file
     * @param mavenArtifactId artifact id of the component to locate the dep file.
     * @param classLoader
     * @throws IOException if reading the file failed.
     */
    private Set<String> getDesignTimeDependencies(String mavenGroupId, String mavenArtifactId, ClassLoader classLoader)
            throws IOException {
        String depPath = computeDesignDependenciesPath(mavenGroupId, mavenArtifactId);
        if (classLoader == null) {
            classLoader = this.getClass().getClassLoader();
        }
        InputStream depStream = classLoader.getResourceAsStream(depPath);
        if (depStream == null) {
            throw new ComponentException(ComponentsApiErrorCode.COMPUTE_DEPENDENCIES_FAILED,
                    ExceptionContext.withBuilder().put("path", depPath).build());
        } // else we found it so parse it now
        try {
            return parseDependencies(depStream);
        } finally {
            depStream.close();
        }
    }

    /**
     * DOC sgandon Comment method "computeDesignDepenenciesPath".
     * 
     * @param mavenGroupId
     * @param mavenArtifactId
     * @return
     */
    public String computeDesignDependenciesPath(String mavenGroupId, String mavenArtifactId) {
        return "META-INF/maven/" + mavenGroupId + "/" + mavenArtifactId + "/dependencies.properties";
    }

    /**
     * reads a stream following the service mix depends plugin format
     * (http://svn.apache.org/viewvc/servicemix/maven-plugins/tags/depends-maven-plugin-1.3.1/). example file
     * 
     * <pre>
    * {@code
    # Project dependencies generated by the Apache ServiceMix Maven Plugin
    # Generated at: Mon Mar 07 17:15:11 CET 2016
    
    groupId = org.talend.components
    artifactId = file-input
    version = 0.1.0.SNAPSHOT
    org.talend.components/file-input/version = 0.1.0.SNAPSHOT
    
    # dependencies
    
    aopalliance/aopalliance/version = 1.0
    aopalliance/aopalliance/type = jar
    aopalliance/aopalliance/scope = compile
    * }
     * </pre>
     *
     * @param depStream of the dependencies file
     * @return a list of maven url strings
     * @throws IOException if read fails.
     */
    private Set<String> parseDependencies(InputStream depStream) throws IOException {
        Set<String> mvnUris = new HashSet<>();
        Properties properties = new Properties();
        properties.load(depStream);
        Set<Entry<Object, Object>> entrySet = properties.entrySet();
        for (Entry<Object, Object> entry : entrySet) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (key.endsWith("/version")) {
                int indexOfGpSeparator = key.indexOf('/');
                String groupId = key.substring(0, indexOfGpSeparator);
                String artifactId = key.substring(indexOfGpSeparator + 1, key.indexOf('/', indexOfGpSeparator + 1));
                StringBuilder mvnUri = new StringBuilder("mvn:").append(groupId).append("/").append(artifactId).append("/")
                        .append(value);
                String type = properties.getProperty(groupId + "/" + artifactId + "/type");
                if (type != null) {
                    mvnUri.append("/").append(type);
                    String scope = properties.getProperty(groupId + "/" + artifactId + "/scope");
                    if (scope != null && "test".contains(scope)) {
                        // test depenencies so ignors it
                        continue;
                    } // else not a test so good, keep going.
                } // else no type defined so this is the own artifact
                mvnUris.add(mvnUri.toString());
            }
        }
        return mvnUris;
    }

    /**
     * DOC sgandon Comment method "computeDependenciesFromPom".
     * 
     * @param mavenPomStream
     * @param componentDef will only be used to for its name in case of errors.
     * @return
     * @throws org.eclipse.aether.resolution.DependencyResolutionException
     * @throws DependencyCollectionException
     * @throws XmlPullParserException
     * @throws IOException
     * @throws ModelBuildingException
     * @throws Exception
     */
    Set<String> computeDependenciesFromPom(InputStream mavenPomStream, String... excludedScopes)
            throws DependencyCollectionException, org.eclipse.aether.resolution.DependencyResolutionException, IOException,
            XmlPullParserException, ModelBuildingException {
        MavenBooter booter = new MavenBooter();
        // FIXME we may not have to load the model and resolve it
        MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();
        mavenXpp3Reader.setAddDefaultEntities(false);
        Model pomModel = mavenXpp3Reader.read(mavenPomStream);

        // Model pomModel = loadPom(mavenPomStream, booter, Collections.EMPTY_LIST);

        // List<org.apache.maven.model.Dependency> dependencies = pomModel.getDependencies();
        MavenProject mavenProject = new MavenProject(pomModel);
        Set<Dependency> dependencies = getArtifactsDependencies(mavenProject, booter, excludedScopes);
        Set<String> depsStrings = new HashSet<>(dependencies.size());
        // depsStrings.add("mvn:" + pomModel.getGroupId() + "/" + pomModel.getArtifactId() + "/" +
        // pomModel.getVersion());
        for (Dependency dep : dependencies) {
            depsStrings.add("mvn:" + dep.getArtifact().getGroupId() + "/" + dep.getArtifact().getArtifactId() + "/" //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
                    + dep.getArtifact().getVersion() + "/"
                    + (dep.getArtifact().getExtension().equals("") ? "" : dep.getArtifact().getExtension())
                    + (dep.getArtifact().getClassifier().equals("") ? "" : ("/" + dep.getArtifact().getClassifier())));
        }
        return depsStrings;
    }

    public Set<Dependency> getArtifactsDependencies(MavenProject project, MavenBooter booter, String... excludedScopes)
            throws DependencyCollectionException, org.eclipse.aether.resolution.DependencyResolutionException {
        DefaultArtifact pomArtifact = new DefaultArtifact(project.getGroupId(), project.getArtifactId(), project.getPackaging(),
                null, project.getVersion());
        // check the cache if we already have computed the dependencies for this pom.
        if (dependenciesCache.containsKey(pomArtifact)) {
            return dependenciesCache.get(pomArtifact);
        }
        RepositorySystem repoSystem = booter.newRepositorySystem();
        DefaultRepositorySystemSession repoSession = booter.newRepositorySystemSession(repoSystem);
        DependencySelector depFilter = new AndDependencySelector(new ScopeDependencySelector(null, Arrays.asList(excludedScopes)),
                new OptionalDependencySelector(), new ExclusionDependencySelector());
        repoSession.setDependencySelector(depFilter);

        List<RemoteRepository> remoteRepos = booter.getRemoteRepositoriesWithAuthentification(repoSystem, repoSession);

        CollectRequest collectRequest = new CollectRequest(new Dependency(pomArtifact, "runtime"), remoteRepos);
        // collectRequest.setRequestContext(scope);
        CollectResult collectResult = repoSystem.collectDependencies(repoSession, collectRequest);
        DependencyNode root = collectResult.getRoot();
        Set<Dependency> ret = new HashSet<>();
        ret.add(root.getDependency());
        flattenDeps(root, ret);
        dependenciesCache.put(pomArtifact, ret);
        return ret;
    }

    /**
     * DOC sgandon Comment method "flattenDeps".
     * 
     * @param node
     * @param ret
     */
    private static void flattenDeps(DependencyNode node, Set<Dependency> ret) {
        List<DependencyNode> children = node.getChildren();
        for (DependencyNode dn : children) {
            Dependency dep = dn.getDependency();
            ret.add(dep);
            if (!dn.getChildren().isEmpty()) {
                flattenDeps(dn, ret);
            }
        }
    }

    Model loadPom(final InputStream pomStream, MavenBooter booter, List<String> profilesList) throws ModelBuildingException {

        RepositorySystem system = booter.newRepositorySystem();
        RepositorySystemSession session = booter.newRepositorySystemSession(system);
        ModelBuildingRequest modelRequest = new DefaultModelBuildingRequest();
        modelRequest.setValidationLevel(ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL);
        modelRequest.setProcessPlugins(false);
        modelRequest.setTwoPhaseBuilding(false);
        modelRequest.setSystemProperties(toProperties(session.getUserProperties(), session.getSystemProperties()));
        // modelRequest.setModelCache( DefaultModelCache.newInstance( session ) );
        ProjectModelResolver projectModelResolver = new ProjectModelResolver(session, null, system,
                new DefaultRemoteRepositoryManager(), booter.getRemoteRepositoriesWithAuthentification(system, session), null,
                null);
        modelRequest.setModelResolver(projectModelResolver);
        modelRequest.setActiveProfileIds(profilesList);
        modelRequest.setModelSource(new ModelSource() {

            @Override
            public InputStream getInputStream() throws IOException {
                return pomStream;
            }

            @Override
            public String getLocation() {
                return "";// FIXME return the component name
            }
        });
        if (modelBuilder == null) {
            modelBuilder = new DefaultModelBuilderFactory().newInstance();
        }
        ModelBuildingResult builtModel = modelBuilder.build(modelRequest);
        LOGGER.debug("built problems:" + builtModel.getProblems());
        return builtModel.getEffectiveModel();
    }

    private Properties toProperties(Map<String, String> dominant, Map<String, String> recessive) {
        Properties props = new Properties();
        if (recessive != null) {
            props.putAll(recessive);
        }
        if (dominant != null) {
            props.putAll(dominant);
        }
        return props;
    }

}
