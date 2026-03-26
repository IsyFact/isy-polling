# 5.0.0
### FEATURES
- `IFS-4713`: Dokumentation mit Stand aus `isyfact-standards` zusammengeführt und technische Schulden behoben.
- `IFS-4714`: Zentrale Versionierung eingeführt.
- `IFS-4578`: Portierung in entkoppelten Baustein isy-polling
    - Portierung der Tickets IFS-4367, ISY-1025, IFS-3740 ins entkoppelte Repository
- `IFS-4583`: Wiedereinführung der Quality-Gates

### BREAKING CHANGES
- `IFS-4922`: Aktualisierung von Java 17 auf 25
- `IFS-4859`: Spring-Boot Update auf Version 4 (inkludiert Update auf Spring Framework 7)

### DEPENDENCY UPGRADES
- Update IsyFact/isy-github-actions-templates/.github/workflows/maven_dependency_scan_template.yml von Version 2.1.1 auf 2.3.0
- Update org.apache.maven.plugins:maven-compiler-plugin von Version 3.14.1 auf 3.15.0
- Update org.springframework.boot:spring-boot-dependencies von Version 4.0.2 auf 4.0.3
- Update IsyFact/isy-github-actions-templates/.github/workflows/maven_dependency_scan_template.yml von Version 2.0.0 auf 2.1.1
- Update IsyFact/isy-github-actions-templates/.github/workflows/docs_build_template.yml von Version 2.1.0 auf 2.1.1
- Update IsyFact/isy-github-actions-templates/.github/workflows/dependabot_auto_merge_template.yml von Version 2.0.0 auf 2.1.1
- Update IsyFact/isy-github-actions-templates/.github/workflows/maven_build_template.yml von Version 2.0.0 auf 2.1.1
- Update IsyFact/isy-github-actions-templates/.github/workflows/next_version.yml von Version 2.0.0 auf 2.1.1
- Update org.apache.maven.plugins:maven-surefire-plugin von Version 3.1.2 auf 3.5.4
- Update org.codehaus.mojo:flatten-maven-plugin von Version 1.7.1 auf 1.7.3
- Update org.apache.maven.plugins:maven-jar-plugin von Version 3.3.0 auf 3.5.0
- Update org.springframework.boot:spring-boot-dependencies von Version 3.5.6 auf 3.5.9
- Update org.sonatype.central:central-publishing-maven-plugin von Version 0.8.0 auf 0.10.0
- Update org.apache.maven.plugins:maven-source-plugin von Version 3.2.1 auf 3.4.0
- Update org.apache.maven.plugins:maven-gpg-plugin von Version 3.0.1 auf 3.2.8
- Update org.apache.maven.plugins:maven-enforcer-plugin von Version 3.6.0 auf 3.6.2
- Update com.github.spotbugs:spotbugs-maven-plugin von Version 4.9.8.1 auf 4.9.8.2
- Update IsyFact/isy-github-actions-templates/.github/workflows/dependabot_auto_changelog_template.yml von Version 1.8.0 auf 2.1.1
- Update IsyFact/isy-github-actions-templates/.github/workflows/commit_message_checker_template.yml von Version 2.0.0 auf 2.1.1
- Update IsyFact/isy-github-actions-templates/.github/workflows/dependency_review_template.yml von Version 2.0.0 auf 2.1.1
- Update IsyFact/isy-github-actions-templates/.github/workflows/maven_create_release_template.yml von Version 2.0.0 auf 2.1.1
- Update IsyFact/isy-github-actions-templates/.github/workflows/maven_deploy_template.yml von Version 2.0.0 auf 2.1.1
- `IFS-4655`: Update von Maven Checkstyle Plugin auf Version 3.6.0
- `IFS-4531`: Update von Flatten Maven Plugin auf Version 1.7.1
    * Hinzufügen von Maven Enforcer Plugin auf Version 3.6.0
    * Setzen der Maven Version auf 3.6.3
- `IFS-4580`: Spring-Boot Update auf Version 3.4.5 
- `IFS-4864`: Spring-Boot Update auf Version 3.5.6 
