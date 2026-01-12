# 5.0.0
### FEATURES
- `IFS-4713`: Dokumentation mit Stand aus `isyfact-standards` zusammengeführt und technische Schulden behoben.
- `IFS-4714`: Zentrale Versionierung eingeführt.
- `IFS-4578`: Portierung in entkoppelten Baustein isy-polling
    - Portierung der Tickets IFS-4367, ISY-1025, IFS-3740 ins entkoppelte Repository
- `IFS-4583`: Wiedereinführung der Quality-Gates

### BREAKING CHANGES
- `IFS-4922`: Aktualisierung von Java 17 auf 25

### DEPENDENCY UPGRADES
- Update org.apache.maven.plugins:maven-gpg-plugin von Version 3.0.1 auf 3.2.8
- `IFS-4655`: Update von Maven Checkstyle Plugin auf Version 3.6.0
- `IFS-4531`: Update von Flatten Maven Plugin auf Version 1.7.1
    * Hinzufügen von Maven Enforcer Plugin auf Version 3.6.0
    * Setzen der Maven Version auf 3.6.3
- `IFS-4580`: Spring-Boot Update auf Version 3.4.5 
- `IFS-4864`: Spring-Boot Update auf Version 3.5.6 
